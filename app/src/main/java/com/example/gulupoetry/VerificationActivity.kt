package com.example.gulupoetry

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.gulupoetry.base.BaseActivity
import com.example.gulupoetry.data.GetUserAllMessage
import com.example.gulupoetry.functions.Common
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_verification.*
import okhttp3.*
import java.io.IOException

class VerificationActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)
        action_bar_verification_activity.setStyle { back, txt, hear, collection, share ->
            back.setOnClickListener {
                finish()
            }
            txt.text = "验证账号"
            hear.visibility = View.GONE
            share.visibility = View.GONE
            collection.visibility = View.GONE
        }
        button_verification_activity.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            button_verification_activity -> {
                try {
                    val params = mapOf(
                        "gollum" to gollum_verification_activity.text.toString()
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
                                val info =
                                    Gson().fromJson(responseData, GetUserAllMessage::class.java)
                                Log.i("Tag",responseData)
                                if (info.code == 200){
                                    if (email_verification_activity.text.toString() == info.data.email && tel_verification_activity.text.toString() == info.data.tel) {
                                        val intent = Intent(
                                            this@VerificationActivity,
                                            ModifyActivity::class.java
                                        )
                                        intent.putExtra("gollum",gollum_verification_activity.text.toString())
                                        startActivity(intent)
                                    }else{
                                        Common.myToast(this@VerificationActivity,"请输入正确的信息！！")
                                    }
                                }else{
                                    Common.myToast(this@VerificationActivity,"输入正确的账号！")
                                }
                            }
                        }

                    })
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}