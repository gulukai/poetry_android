package com.example.test

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.adapter.MyRecyclerViewAdapter
import com.example.test.base.BaseActivity
import com.example.test.base.User
import com.example.test.data.CommentData
import com.example.test.data.CommentDataItem
import com.example.test.functions.Common
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_comment_list.*
import kotlinx.android.synthetic.main.comment_list_layout.view.*
import okhttp3.*
import java.io.IOException
import java.lang.Exception

class CommentActivityList : BaseActivity() {
    private val commentList = arrayListOf<CommentDataItem>()
    private val a: Int = 1
    private var handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_list)

        Log.i("Tag", User.user_no.toString())

        action_comment_list_activity.setStyle { back, txt, hear, collection, share ->
            back.setOnClickListener {
                finish()
            }
            txt.text = "评论列表"
            hear.visibility = View.GONE
            collection.visibility = View.GONE
            share.visibility = View.GONE
        }

        val poetryId = intent.getIntExtra("poetryId", 0)
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
                            commentList.add(
                                CommentDataItem(
                                    item.heard,
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
//                                    holder.itemView.head_image_comment_list = commentList[position].head
                                    holder.itemView.nickname_comment_list.text =
                                        commentList[position].nickname
                                    holder.itemView.time_comment_list.text =
                                        commentList[position].time
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
            val params = mapOf<String, String>(
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
}