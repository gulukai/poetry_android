package com.example.test

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.test.base.BaseActivity
import com.example.test.base.User
import com.example.test.data.GetUserAllMessage
import com.example.test.functions.Common
import com.example.test.mydialog.EditDialog
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_data.*
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.*
import java.io.IOException
import java.util.*

class DataActivity : BaseActivity(), View.OnClickListener {

    private var handler = Handler()
    private var dateStr: String = ""
    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0
    private var genderChecked = ""
    private var nick = ""
    private var gender2 = ""
    private var tel = ""
    private var email = ""
    private var birthday = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)

        nickname_data.setOnClickListener(this)
        birthday_data.setOnClickListener(this)
        tel_data.setOnClickListener(this)
        email_data.setOnClickListener(this)
        confirm_data.setOnClickListener(this)
        initTime()
        val gender = arrayOf("男", "女")
        gender_content_my_data.adapter =
            ArrayAdapter(this, R.layout.my_spinner_layout, gender)
        gender_content_my_data.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    genderChecked = gender[position]
                    gender2 = genderChecked
                }
            }

        my_action_bar_data.setStyle { back, txt, hear, collection, share ->
            back.setOnClickListener {
                val intent =
                    Intent(this, MainActivity::class.java)
                intent.putExtra(
                    "user_no",
                    User.user_no
                )
                intent.putExtra("login", 99)
                startActivity(intent)
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
                        nick = info.data.nickname
                    }
                    if (info.data.gender) {
                        gender_head_my_data.text = "性别"
                        gender_content_my_data.setSelection(0)
                        gender2 = gender[0]
                    } else {
                        gender_head_my_data.text = "性别"
                        gender_content_my_data.setSelection(1)
                        gender2 = gender[1]
                    }

                    birthday_data.setStyle { head, content ->
                        head.text = "生日"
                        content.text = info.data.birthday
                        birthday = info.data.birthday
                    }

                    tel_data.setStyle { head, content ->
                        head.text = "电话"
                        content.text = info.data.tel
                        tel = info.data.tel
                    }

                    email_data.setStyle { head, content ->
                        head.text = "邮箱"
                        content.text = info.data.email
                        email = info.data.email
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

    override fun onClick(v: View?) {
        when (v) {
            nickname_data -> {
                val editDialog = EditDialog(this)
                editDialog.setCancelable(false)
                editDialog.show()
                editDialog.setStyle { comment, cancel, release ->
                    comment.setRawInputType(InputType.TYPE_CLASS_TEXT)
                    cancel.setOnClickListener {

                        editDialog.dismiss()
                    }
                    release.setOnClickListener {
                        nickname_data.setStyle { head, content ->
                            head.text = "昵称"
                            content.text = comment.text
                            nick = comment.text.toString()
                        }
                        editDialog.dismiss()
                    }
                }
            }
            birthday_data -> {
                DatePickerDialog(
                    this,
                    AlertDialog.THEME_HOLO_LIGHT,
                    { _, selectedYear, selectedMonth, selectedDay ->
                        dateStr = "${selectedYear}-${selectedMonth + 1}-${selectedDay}"
                        birthday_data.setStyle { head, content ->
                            head.text = "生日"
                            content.text = dateStr
                            birthday = dateStr
                        }
                    },
                    year,
                    month,
                    day
                ).show()
            }
            tel_data -> {
                val editDialog = EditDialog(this)
                editDialog.setCancelable(false)
                editDialog.show()
                editDialog.setStyle { comment, cancel, release ->
                    comment.setRawInputType(InputType.TYPE_CLASS_PHONE)
                    cancel.setOnClickListener {

                        editDialog.dismiss()
                    }
                    release.setOnClickListener {
                        if (!Common().isMobile(comment.text.toString())) {
                            Common.myToast(this, "请输入合法的电话号码！")
                        } else {
                            tel_data.setStyle { head, content ->
                                head.text = "电话"
                                content.text = comment.text
                                tel = comment.text.toString()
                            }
                            editDialog.dismiss()
                        }
                    }
                }
            }
            email_data -> {
                val editDialog = EditDialog(this)
                editDialog.setCancelable(false)
                editDialog.show()
                editDialog.setStyle { comment, cancel, release ->
                    comment.setRawInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                    cancel.setOnClickListener {

                        editDialog.dismiss()
                    }
                    release.setOnClickListener {
                        if (!Common().isEmail(comment.text.toString())) {
                            Common.myToast(this, "请输入合法的邮箱地址！")
                        } else {
                            email_data.setStyle { head, content ->
                                head.text = "邮箱"
                                content.text = comment.text
                                email = comment.text.toString()
                            }
                            editDialog.dismiss()
                        }
                    }
                }
            }
            confirm_data -> {
                try {
                    val params2 = mapOf(
                        "gollum" to User.user_no.toString(),
                        "user_nickname" to nick,
                        "user_gender" to gender2,
                        "user_tel" to tel,
                        "user_birthday" to birthday,
                        "user_email" to email
                    )
                    val body = Common().buildParams(params2)
                    val client = OkHttpClient()
                    val request =
                        Request.Builder().url("http://www.gulukai.cn/user/updateuser/").post(body)
                            .build()
                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {

                        }

                        override fun onResponse(call: Call, response: Response) {
                            val responseData = response.body?.string()
                            if (responseData != null) {
                                Looper.prepare()
                                Common.myToast(this@DataActivity, "修改成功！")
                                Looper.loop()
                            }
                        }

                    })
                } catch (e: Exception) {
                    e.printStackTrace()
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