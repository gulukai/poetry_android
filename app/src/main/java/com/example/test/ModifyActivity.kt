package com.example.test

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.test.base.ActivityCollector
import com.example.test.base.BaseActivity
import com.example.test.base.User
import com.example.test.data.LoginData
import com.example.test.db.MyDbHelper
import com.example.test.functions.Common
import com.example.test.mydialog.LoginDialog
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_modify.*
import okhttp3.*
import java.io.IOException

class ModifyActivity : BaseActivity() {

    private val dbHelper = MyDbHelper(this, "User.db", 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify)
        action_bar_modify.setStyle { back, txt, hear, collection, share ->
            back.setOnClickListener {
                finish()
            }
            txt.text = "修改密码"
            hear.visibility = View.GONE
            collection.visibility = View.GONE
            share.visibility = View.GONE
        }
        val gollum = intent.getStringExtra("gollum")
        val db = dbHelper.writableDatabase
        // http://www.gulukai.cn/user/updatepassword/
        button_modify_activity.setOnClickListener {
            if (confirm_password_modify.text.toString() != "" && password_modify.text.toString() != "" && password_modify.text.toString() == confirm_password_modify.text.toString()) {
                val dialog = LoginDialog(this)
                dialog.show()
                dialog.setStyle { comment, cancel, release ->
                    comment.text = "你确认修改密码嘛？"
                    cancel.text = "再想想"
                    release.text = "确认"
                    cancel.setOnClickListener {
                        dialog.dismiss()
                    }
                    release.setOnClickListener {
                        try {
                            val params = mapOf(
                                "gollum" to gollum,
                                "password" to password_modify.text.toString()
                            )
                            val body = Common().buildParams(params)
                            val client = OkHttpClient()
                            val request =
                                Request.Builder().url("http://www.gulukai.cn/user/updatepassword/")
                                    .post(body)
                                    .build()
                            client.newCall(request).enqueue(object : Callback {
                                override fun onFailure(call: Call, e: IOException) {

                                }

                                override fun onResponse(call: Call, response: Response) {
                                    val responseData = response.body?.string()
                                    if (responseData != null) {
                                        val info =
                                            Gson().fromJson(responseData, LoginData::class.java)
                                        if (info.code == 200) {

                                            val timeNow = System.currentTimeMillis()
                                            val values = ContentValues()
                                            values.put("is_login", 2)
                                            values.put("pwd", password_modify.text.toString())
                                            values.put("current", timeNow)
                                            db.update("User", values, "gollum = ?", arrayOf(gollum))
                                            val intent =
                                                Intent("com.example.broadcastbestpractice2.FORCE_OFFLINE")
                                            intent.setPackage(packageName)
                                            sendBroadcast(intent)
                                        }
                                    }
                                }
                            })
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            } else {
                Common.myToast(this, "请保持两次密码一致！")
            }
        }
    }
}