package com.example.test

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.adapter.MyRecyclerViewAdapter
import com.example.test.base.ActivityCollector
import com.example.test.base.BaseActivity
import com.example.test.base.User
import com.example.test.data.UserItemData
import com.example.test.db.MyDbHelper
import com.example.test.mydialog.LoginDialog
import kotlinx.android.synthetic.main.activity_comment_list.*
import kotlinx.android.synthetic.main.activity_user_message_manage.*
import kotlinx.android.synthetic.main.user_manage_item_layout.view.*

class UserMessageManageActivity : BaseActivity(), View.OnClickListener {
    private val dbHelper = MyDbHelper(this, "User.db", 1)
    private val longTime = 60 * 60 * 24 * 30
    private val userList = arrayListOf<UserItemData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_message_manage)
        action_user_manage.setStyle { back, txt, hear, collection, share ->
            back.setOnClickListener {
                finish()
            }
            txt.text = "账号管理"
            hear.visibility = View.GONE
            collection.visibility = View.GONE
            share.visibility = View.GONE
        }

        cancel_user_manage.setOnClickListener(this)
        sign_out_user_manage.setOnClickListener(this)

        add_user_manage.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("message", "请登录")
            startActivity(intent)
        }
        val db = dbHelper.writableDatabase
        val cursor = db.query("User", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                val gollum = cursor.getString(cursor.getColumnIndex("gollum"))
                val username = cursor.getString(cursor.getColumnIndex("username"))
                if (gollum.toLong() == User.user_no) {
                    userList.add(
                        UserItemData(
                            username, gollum.toInt(), 1
                        )
                    )
                } else {
                    userList.add(
                        UserItemData(
                            username, gollum.toInt()
                        )
                    )
                }

            } while (cursor.moveToNext())
        }
        cursor.close()
        recycler_user_manage.adapter =
            MyRecyclerViewAdapter.Builder().setDate(userList).setViewHolder { parent, _ ->
                return@setViewHolder MyRecyclerViewAdapter().ViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.user_manage_item_layout, parent, false)
                )
            }.setBindViewHolder { holder, position ->
                holder.itemView.nickname_manage_item.text = userList[position].nickname
                holder.itemView.gollum_manage_item.text = userList[position].gollum.toString()
                if (userList[position].flag == 1) {
                    holder.itemView.checked_manage_item.visibility = View.VISIBLE
                }
                holder.itemView.setOnClickListener {
                    var time = ""
                    if (userList[position].flag != 1) {
                        val cursor2 = db.query(
                            "User",
                            null,
                            "gollum = ?",
                            arrayOf(userList[position].gollum.toString()),
                            null,
                            null,
                            null
                        )
                        while (cursor2.moveToNext()) {
                            time = cursor2.getString(cursor2.getColumnIndex("current"))
                        }
                        val time2 = time.toLong()
                        val timeOfNow = System.currentTimeMillis()
                        val timeDifferent = (timeOfNow - time2) / 1000
                        if (timeDifferent > longTime || timeDifferent < 0) {
                            val values = ContentValues()
                            values.put("is_login", 0)
                            values.put("pwd", "")
                            values.put("current", timeOfNow)
                            db.update(
                                "User",
                                values,
                                "gollum = ?",
                                arrayOf(userList[position].gollum.toString())
                            )
                            val loginDialog = LoginDialog(this)
                            loginDialog.setCancelable(true)
                            loginDialog.show()
                            loginDialog.setStyle { comment, cancel, release ->
                                comment.text = "该账号长期未登录密码已失效！"
                                cancel.setOnClickListener {
                                    loginDialog.dismiss()
                                }
                                release.setOnClickListener {
                                    val intent = Intent(this, LoginActivity::class.java)
                                    intent.putExtra("message", "请登录")
                                    startActivity(intent)
                                    loginDialog.dismiss()
                                }
                            }
                        } else {
                            User.user_no = userList[position].gollum.toLong()
                            Log.i("Tag", "userList${userList[position].gollum.toLong()}")
                            val values2 = ContentValues().apply {
                                put("is_login", 0)
                                put("current", System.currentTimeMillis().toString())
                            }

                            for (user in userList) {
                                if (user.flag == 1) {
                                    db.update(
                                        "User",
                                        values2,
                                        "gollum = ?",
                                        arrayOf(user.gollum.toString())
                                    )
                                }
                            }
                            val values = ContentValues().apply {
                                put("is_login", 1)
                                put("current", (System.currentTimeMillis() + 1000).toString())
                            }
                            db.update(
                                "User",
                                values,
                                "gollum = ?",
                                arrayOf(userList[position].gollum.toString())
                            )
                            val intent =
                                Intent(this, MainActivity::class.java)
                            intent.putExtra(
                                "user_no",
                                userList[position].gollum.toString().toLong()
                            )
                            intent.putExtra("login", 99)
                            startActivity(intent)
                        }
                        cursor2.close()
                    }
                }
            }.create()
        recycler_user_manage.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    override fun onClick(v: View?) {
        when (v) {
            cancel_user_manage -> {
                ActivityCollector.finishAllActivity()
            }
            sign_out_user_manage -> {
                val intent = Intent("com.example.broadcastbestpractice.FORCE_OFFLINE")
                intent.setPackage(packageName)
                sendBroadcast(intent)
            }
        }
    }
}