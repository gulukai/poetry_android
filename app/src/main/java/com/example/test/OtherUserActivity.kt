package com.example.test

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.test.base.BaseActivity
import com.example.test.base.User
import com.example.test.data.Fan
import com.example.test.data.FansAndFollow
import com.example.test.data.OptionFollow
import com.example.test.data.UserMessageData
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

        if (gollum != 1L){
            try {
                val params = mapOf<String, String>(
                    "gollum" to gollum.toString()
                )
                val body = Common().buildParams(params)
                val client = OkHttpClient()
                val request =
                    Request.Builder().url("http://www.gulukai.cn/user/getuser/")
                        .post(body)
                        .build()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {

                    }

                    override fun onResponse(call: Call, response: Response) {
                        val responseData = response.body?.string()
                        if (responseData != null) {
                            val message = Message()
                            message.what = 2
                            message.obj = responseData
                            handler.sendMessage(message)
                        }
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

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
                    } else {
                        follow_toggle_activity.setBackgroundResource(R.drawable.follow)
                    }
                }
                2->{
                    val str = it.obj.toString()
                    val info = Gson().fromJson(str, UserMessageData::class.java)
                    val head = "http://www.gulukai.cn${info.data.head}"
                    val back = "http://www.gulukai.cn${info.data.background}"
                    user_name_other_user.text = info.data.nickname
                    if (info.data.gender) {
                        gender_other_user.setImageResource(R.drawable.man)
                    } else {
                        gender_other_user.setImageResource(R.drawable.woman)
                    }
                    Glide.with(this).load(head).into(head_portrait_other_user)
                    Glide.with(this).load(back).into(background_image_other_user)
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
                    followA("post", gollum.toString(), my_gollum.toString(), this)
                    buttonView.setBackgroundResource(R.drawable.followed)

                }
            } else {
                if (gollum != 1L && my_gollum != 0L) {
                    followA("delete", gollum.toString(), my_gollum.toString(), this)
                    buttonView.setBackgroundResource(R.drawable.follow)

                }
            }
        }
    }

    private fun followA(type: String, follow: String, fans: String, context: Context) {
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
                        val info = Gson().fromJson(responseData, OptionFollow::class.java)
                        when (info.code) {
                            200 -> {
                                Looper.prepare()
                                Toast.makeText(context,"关注成功！",Toast.LENGTH_SHORT).show()
                                Looper.loop()
                            }
                            202 -> {
                                Looper.prepare()
                                Toast.makeText(context,"取消关注！",Toast.LENGTH_SHORT).show()
                                Looper.loop()
                            }
                        }
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}