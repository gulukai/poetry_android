package com.example.test.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import com.example.test.PoetryCollectionActivity
import com.example.test.R
import com.example.test.data.FansAndFollow
import com.example.test.functions.Common
import com.google.gson.Gson
import kotlinx.android.synthetic.main.my_fragment_layout.*
import okhttp3.*
import java.io.IOException

class MineFragment(private val gollum: Long) : BaseFragment(R.layout.my_fragment_layout) {
    private var handler = Handler()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            val params = mapOf<String, String>(
                "type" to "return",
                "follow" to gollum.toString(),
                "fans" to ""
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
                    fans_my_fragment.text = info.fanscount.toString()
                    follow_my_fragment.text = info.followscount.toString()
                }
            }
            return@Handler true
        }

        Log.i("Tag", gollum.toString())

        collection_my_fragment.setStyle { image, text ->
            image.setImageResource(R.drawable.collection)
            text.text = "我的收藏"
        }
        security_my_fragment.setStyle { image, text ->
            image.setImageResource(R.drawable.security)
            text.text = "安全设置"
        }
        system_my_fragment.setStyle { image, text ->
            image.setImageResource(R.drawable.system)
            text.text = "系统设置"
        }
        about_us_my_fragment.setStyle { image, text ->
            image.setImageResource(R.drawable.about)
            text.text = "关于我们"
        }

        collection_my_fragment.setOnClickListener {
            Common().goActivity(this.context!!, PoetryCollectionActivity::class.java)
        }
    }
}