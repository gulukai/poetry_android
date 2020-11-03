package com.example.test

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import com.example.test.base.BaseActivity
import com.example.test.base.User
import com.example.test.data.GetUserAllMessage
import com.example.test.functions.Common
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_data.*
import okhttp3.*
import java.io.IOException

class DataActivity : BaseActivity() {
    private var handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)

        my_action_bar_data.setStyle { back, txt, hear, collection, share ->
            back.setOnClickListener {
                finish()
            }
            txt.text = "编辑资料"
            hear.visibility = View.GONE
            collection.visibility = View.GONE
            share.visibility = View.GONE
        }
        handler = Handler {
            when (it.what) {
                1 -> {
                    val info = Gson().fromJson(it.obj.toString(), GetUserAllMessage::class.java)
                    nickname_data.setStyle { head, content ->
                        head.text = "昵称"
                        content.text = info.data.nickname
                    }
                    if (info.data.gender) {
                        gender_data.setStyle { head, content ->
                            head.text = "性别"
                            content.text = "男"
                        }
                    } else {
                        gender_data.setStyle { head, content ->
                            head.text = "性别"
                            content.text = "女"
                        }
                    }

                    birthday_data.setStyle { head, content ->
                        head.text = "生日"
                        content.text = info.data.birthday
                    }

                    tel_data.setStyle { head, content ->
                        head.text = "电话"
                        content.text = info.data.tel
                    }

                    email_data.setStyle { head, content ->
                        head.text = "邮箱"
                        content.text = info.data.email
                    }
                }
            }
            return@Handler true
        }

        try {
            val params = mapOf(
                "gollum" to User.user_no.toString()
            )
            val body = Common().buildParams(params)
            val client = OkHttpClient()
            val request =
                Request.Builder().url("http://www.gulukai.cn/user/getuserall/").post(body)
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
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}