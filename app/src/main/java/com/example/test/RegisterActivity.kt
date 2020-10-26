package com.example.test

import ResponseData
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.test.base.BaseActivity
import com.example.test.functions.Common
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.*
import java.io.IOException
import java.util.*

class RegisterActivity : BaseActivity(), View.OnClickListener {
    private var dateStr: String = ""
    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0
    private var genderChecked = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initTime()
        birthday_register.setOnClickListener(this)
        val gender = arrayOf("请选择您的性别", "男", "女")
        gender_register.adapter =
            ArrayAdapter(this, R.layout.my_spinner_layout, gender)
        gender_register.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    genderChecked = gender[position]
                }
            }
        }

        register.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            birthday_register -> {
                DatePickerDialog(
                    this,
                    AlertDialog.THEME_HOLO_LIGHT,
                    { _, selectedYear, selectedMonth, selectedDay ->
                        dateStr = "${selectedYear}-${selectedMonth + 1}-${selectedDay}"
                        birthday_register.text = dateStr
                    },
                    year,
                    month,
                    day
                ).show()
            }
            register -> {
                val nick = nick_name_register.text.toString()
                val pwd = password_register.text.toString()
                val confirm = confirm_password_register.text.toString()
                val gender = genderChecked
                val tel = tel_register.text.toString()
                val email = email_register.text.toString()
                val birthday = dateStr
                if (nick == "") {
                    Common.myToast(this, "请输入昵称！")
                } else if (pwd == "") {
                    Common.myToast(this, "请输入密码！")
                } else if (confirm != pwd) {
                    Common.myToast(this, "请保持两次输入的密码相同！")
                } else if (gender == "" || gender == "请选择您的性别") {
                    Common.myToast(this, "请选择性别！")
                } else if (tel == "") {
                    Common.myToast(this, "请输入电话！")
                } else if (!Common().isMobile(tel)) {
                    Log.i("Tag", Common().isMobile(tel).toString())
                    Common.myToast(this, "请输入合法的电话号码！")
                } else if (email == "") {
                    Common.myToast(this, "请输入输入邮箱地址！")
                } else if (birthday == "") {
                    Common.myToast(this, "请输入生日！")
                } else {
                    /*
                    user_nickname = request.POST.get("user_nickname", "")
                    user_password = request.POST.get("user_password", "")
                    user_gender = request.POST.get("user_gender")
                    user_tel = request.POST.get("user_tel", "")
                    user_birthday = request.POST.get("user_birthday", "1999-01-01")
                    user_email = request.POST.get("user_email")
                    * */
                    try {
                        val params = mapOf<String, String>(
                            "user_nickname" to nick,
                            "user_password" to pwd,
                            "user_gender" to gender,
                            "user_tel" to tel,
                            "user_birthday" to birthday,
                            "user_email" to email
                        )
                        val body = Common().buildParams(params)
                        val client = OkHttpClient()
                        val request =
                            Request.Builder().url("http://www.gulukai.cn/user/postuser/").post(body)
                                .build()
                        client.newCall(request).enqueue(object : Callback {
                            override fun onFailure(call: Call, e: IOException) {

                            }

                            override fun onResponse(call: Call, response: Response) {
                                val responseData = response.body?.string()
                                if (responseData != null) {
                                    val info =
                                        Gson().fromJson(responseData, ResponseData::class.java)
                                    val gollum = info.data.user_no
                                    val intent =
                                        Intent(this@RegisterActivity, LoginActivity::class.java)
                                    intent.putExtra("gollum", gollum)
                                    startActivity(intent)
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

    private fun initTime() {
        val ca = Calendar.getInstance()
        year = ca.get(Calendar.YEAR)
        month = ca.get(Calendar.MONTH)
        day = ca.get(Calendar.DAY_OF_MONTH)
    }
}