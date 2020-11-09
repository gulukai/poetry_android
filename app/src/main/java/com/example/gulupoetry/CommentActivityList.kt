package com.example.gulupoetry

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.gulupoetry.adapter.MyRecyclerViewAdapter
import com.example.gulupoetry.base.BaseActivity
import com.example.gulupoetry.base.User
import com.example.gulupoetry.data.CommentData
import com.example.gulupoetry.data.CommentDataItem
import com.example.gulupoetry.functions.Common
import com.example.gulupoetry.mydialog.CommentDialog
import com.example.gulupoetry.mydialog.LoginDialog
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_comment_list.*
import kotlinx.android.synthetic.main.comment_list_layout.view.*
import okhttp3.*
import java.io.IOException
import java.lang.Exception

class CommentActivityList : BaseActivity() {
    private val commentList = arrayListOf<CommentDataItem>()
    private var handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_list)
        val poetryId = intent.getIntExtra("poetryId", 0)
        edit_text_comment_list.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                if (User.user_no == 0L) {
                    edit_text_comment_list.clearFocus()
                    val loginDialog = LoginDialog(this)
                    loginDialog.setCancelable(true)
                    loginDialog.show()
                    loginDialog.setStyle { _, cancel, release ->
                        cancel.setOnClickListener {
                            edit_text_comment_list.clearFocus()
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
                    Common().setDontShowSoftInputWhenFocused(edit_text_comment_list)
                    val dialog = CommentDialog(this)
                    dialog.setCancelable(false)
                    dialog.show()
                    dialog.setStyle { comment, cancel, release ->
                        release.setOnClickListener {

                            /*
                            * commentList.add(
                                    CommentDataItem(
                                        headUrl,
                                        item.nickname,
                                        item.time,
                                        item.text
                                    )
                                )
                            * */

                            postComment(
                                poetryId.toString(),
                                User.user_no.toString(),
                                comment.text.toString()
                            )
                            edit_text_comment_list.clearFocus()
                            dialog.dismiss()
                        }
                        cancel.setOnClickListener {
                            edit_text_comment_list.clearFocus()
                            dialog.dismiss()
                        }
                    }
                }
            }
        }

        action_comment_list_activity.setStyle { back, txt, hear, collection, share ->
            back.setOnClickListener {
                finish()
            }
            txt.text = "评论列表"
            hear.visibility = View.GONE
            collection.visibility = View.GONE
            share.visibility = View.GONE
        }


        getCommentList(poetryId)
        handler = Handler {
            when (it.what) {
                1 -> {
                    val str = it.obj.toString()
                    val info = Gson().fromJson(str, CommentData::class.java)
                    if (info.code == 202) {
                        Common.myToast(this@CommentActivityList, "该篇古诗暂无评论，你来占个沙发吧！")
                    } else {
                        for (item in info.data) {
                            val headUrl = "http://www.gulukai.cn${item.head}"
                            commentList.add(
                                CommentDataItem(
                                    headUrl,
                                    item.nickname,
                                    item.time,
                                    item.text
                                )
                            )
                        }
                        recycler_comment_activity.adapter =
                            MyRecyclerViewAdapter.Builder().setDate(commentList)
                                .setViewHolder { parent, _ ->
                                    return@setViewHolder MyRecyclerViewAdapter().ViewHolder(
                                        LayoutInflater.from(parent.context).inflate(
                                            R.layout.comment_list_layout,
                                            parent,
                                            false
                                        )
                                    )
                                }.setBindViewHolder { holder, position ->
                                    Glide.with(this).load(commentList[position].headUrl)
                                        .into(holder.itemView.head_image_comment_list)
                                    holder.itemView.nickname_comment_list.text =
                                        commentList[position].nickname
                                    holder.itemView.time_comment_list.text =
                                        Common().changeTime(commentList[position].time)
                                    holder.itemView.text_comment_list.text =
                                        commentList[position].text
                                }.create()
                        recycler_comment_activity.layoutManager = LinearLayoutManager(
                            this@CommentActivityList,
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                    }
                }
            }
            return@Handler true
        }
    }

    private fun getCommentList(poetryId: Int) {
        try {
            val params = mapOf(
                "method" to "get",
                "poetry_id" to "$poetryId"
            )
            val body = Common().buildParams(params)
            val client = OkHttpClient()
            val request =
                Request.Builder().url("http://www.gulukai.cn/comment/postcomment/").post(body)
                    .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {
                    val responseData = response.body?.string()
                    if (responseData != null) {
                        val message = Message()
                        message.what = 1
                        message.obj = responseData
                        handler.sendMessage(message)
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun postComment(poetryId: String, gollum: String, text: String) {
        val time = System.currentTimeMillis().toString()
        try {
            val params = mapOf(
                "method" to "post",
                "poetry_id" to poetryId,
                "user_id" to gollum,
                "text" to text,
                "time" to time
            )
            val body = Common().buildParams(params)
            val client = OkHttpClient()
            val request =
                Request.Builder().url("http://www.gulukai.cn/comment/postcomment/").post(body)
                    .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {
                    val responseData = response.body?.string()
                    if (responseData != null) {
                        Log.i("Tag", responseData)
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}