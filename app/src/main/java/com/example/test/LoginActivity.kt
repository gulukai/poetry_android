package com.example.test

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.test.base.BaseActivity
import com.example.test.data.LoginData
import com.example.test.db.MyDbHelper
import com.example.test.functions.Common
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.*
import java.io.IOException

class LoginActivity : BaseActivity(), View.OnClickListener {
    private val dbHelper = MyDbHelper(this, "User.db", 1)
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
        val gollum = intent.getLongExtra("gollum", 1)
        if (gollum.toDouble() != 1.00) {
            gollum_login_activity.setText(gollum.toString())
        }
        val message = intent.getStringExtra("message")
        Common.myToast(this, message)

    }

    override fun onClick(v: View?) {
        when (v) {
            register_login_activity -> {
                Common().goActivity(this, RegisterActivity::class.java)
            }
            big_arrow_login -> {
                val db = dbHelper.writableDatabase
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
                                val info =
                                    Gson().fromJson(responseData, LoginData::class.java)
                                val nickName = info.data
                                when (info.code) {
                                    200 -> {
                                        Looper.prepare()
                                        val timeOfNow = System.currentTimeMillis()
                                        val values = ContentValues()
                                        values.put("is_login", 1)
                                        values.put("pwd", password_login_activity.text.toString())
                                        values.put("current", timeOfNow)
                                        val rows =
                                            db.update(
                                                "User",
                                                values,
                                                "gollum = ?",
                                                arrayOf(gollum_login_activity.text.toString())
                                            )
                                        Log.i("Tag", "rows:$rows")
                                        if (rows != 1){
                                            val value = ContentValues().apply {
                                                put("is_login", 1)
                                                put("username", nickName)
                                                put("gollum", gollum_login_activity.text.toString())
                                                put("current", timeOfNow.toString())
                                                put("pwd", password_login_activity.text.toString())
                                            }
                                            db.insert("User", null, value)
                                        }
                                        val intent =
                                            Intent(this@LoginActivity, MainActivity::class.java)
                                        intent.putExtra(
                                            "user_no",
                                            gollum_login_activity.text.toString().toLong()
                                        )
                                        intent.putExtra("login",99)
                                        startActivity(intent)
                                        Looper.loop()
                                    }
                                    201 -> {
                                        Looper.prepare()
                                        Common.myToast(this@LoginActivity, "账号不存在！")
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