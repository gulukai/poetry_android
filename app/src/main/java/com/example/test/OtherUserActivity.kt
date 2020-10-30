package com.example.test

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import com.example.test.base.BaseActivity
import com.example.test.base.User
import com.example.test.data.Fan
import com.example.test.data.FansAndFollow
import com.example.test.functions.Common
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_other_user.*
import okhttp3.*
import java.io.IOException

class OtherUserActivity : BaseActivity() {
    private var handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_user)

        val gollum = intent.getLongExtra("user_no", 1)
        val my_gollum = User.user_no

        try {
            val params = mapOf<String, String>(
                "type" to "return",
                "follow" to gollum.toString(),
                "fans" to my_gollum.toString()
            )
            val body = Common().buildParams(params)
            val client = OkHttpClient()
            val request =
                Request.Builder().url("http://www.gulukai.cn/follow/postfollow/")
                    .post(body)
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

        handler = Handler {
            when (it.what) {
                1 -> {
                    val str = it.obj.toString()
                    val info = Gson().fromJson(str, FansAndFollow::class.java)
                    fans_other_user.text = info.fanscount.toString()
                    follow_other_user.text = info.followscount.toString()
                    if (info.fans.contains(Fan(my_gollum.toString()))) {
                        follow_toggle_activity.isChecked = true
                        follow_toggle_activity.setBackgroundResource(R.drawable.followed)
                    }else{
                        follow_toggle_activity.setBackgroundResource(R.drawable.follow)
                    }
                }
            }
            return@Handler true
        }

        action_other_user.setStyle { back, txt, hear, collection, share ->
            back.setOnClickListener {
                finish()
            }
            txt.text = "Ta的信息"
            hear.visibility = View.GONE
            collection.visibility = View.GONE
            share.visibility = View.GONE
        }

        collection_other_user.setStyle { image, text ->
            image.setBackgroundResource(R.drawable.collection)
            text.text = "TA收藏的诗集"
        }

        follow_toggle_activity.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (gollum != 1L && my_gollum != 0L) {
                    followA("post", gollum.toString(), my_gollum.toString())
                }
                buttonView.setBackgroundResource(R.drawable.followed)
                Common.myToast(this, "关注成功！")
            } else {
                if (gollum != 1L && my_gollum != 0L) {
                    followA("delete", gollum.toString(), my_gollum.toString())
                }
                buttonView.setBackgroundResource(R.drawable.follow)
                Common.myToast(this, "取消关注！")
            }
        }
    }

    private fun followA(type: String, follow: String, fans: String) {
        try {
            val params = mapOf<String, String>(
                "type" to type,
                "follow" to follow,
                "fans" to fans
            )
            val body = Common().buildParams(params)
            val client = OkHttpClient()
            val request =
                Request.Builder().url("http://www.gulukai.cn/follow/postfollow/")
                    .post(body)
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