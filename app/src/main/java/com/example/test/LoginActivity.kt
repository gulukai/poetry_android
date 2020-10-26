package com.example.test

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.test.base.BaseActivity
import com.example.test.data.LoginData
import com.example.test.functions.Common
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.*
import java.io.IOException

class LoginActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        var gollumFlag = false
        var passwordFlag = false
        gollum_login_activity.addTextChangedListener {
            gollumFlag = gollum_login_activity.text.isNotEmpty()
            if (gollumFlag && passwordFlag) {
                big_arrow_login.setBackgroundResource(R.drawable.big_right_arrow_true)
                big_arrow_login.isEnabled = true
            } else {
                big_arrow_login.setBackgroundResource(R.drawable.big_right_arrow_false)
                big_arrow_login.isEnabled = true
            }
        }
        password_login_activity.addTextChangedListener {
            passwordFlag = password_login_activity.text.isNotEmpty()
            if (gollumFlag && passwordFlag) {
                big_arrow_login.setBackgroundResource(R.drawable.big_right_arrow_true)
                big_arrow_login.isEnabled = true
            } else {
                big_arrow_login.setBackgroundResource(R.drawable.big_right_arrow_false)
                big_arrow_login.isEnabled = true
            }
        }

        register_login_activity.setOnClickListener(this)
        big_arrow_login.setOnClickListener(this)
        val gollum = intent.getIntExtra("gollum", 1)
        if (gollum != 1) {
            gollum_login_activity.setText(gollum.toString())
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            register_login_activity -> {
                Common().goActivity(this, RegisterActivity::class.java)
            }
            big_arrow_login -> {
                try {
                    val params = mapOf<String, String>(
                        "user_no" to gollum_login_activity.text.toString(),
                        "user_password" to password_login_activity.text.toString()
                    )
                    val body = Common().buildParams(params)
                    val client = OkHttpClient()
                    val request =
                        Request.Builder().url("http://www.gulukai.cn/user/login/").post(body)
                            .build()
                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {

                        }

                        override fun onResponse(call: Call, response: Response) {
                            val responseData = response.body?.string()
                            if (responseData != null) {
                                Log.i("Tag", responseData)
                                val info =
                                    Gson().fromJson(responseData, LoginData::class.java)
                                val nickName = info.data
                                when (info.code) {
                                    200 -> {
                                        Looper.prepare()
                                        Common.myToast(this@LoginActivity, "登录成功！")
                                        val intent =
                                            Intent(this@LoginActivity, MainActivity::class.java)
                                        intent.putExtra(
                                            "user_no",
                                            gollum_login_activity.text.toString()
                                        )
                                        startActivity(intent)
                                        Looper.loop()
                                    }
                                    201 -> {
                                        Looper.prepare()
                                        Toast.makeText(
                                            this@LoginActivity,
                                            "账号不存在",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        Looper.loop()
                                    }
                                    202 -> {
                                        Looper.prepare()
                                        Common.myToast(this@LoginActivity, "密码错误！")
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
    }
}