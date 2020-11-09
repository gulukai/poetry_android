package com.example.gulupoetry

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.gulupoetry.base.ActivityCollector
import com.example.gulupoetry.base.BaseActivity
import com.example.gulupoetry.base.User
import com.example.gulupoetry.data.UserData
import com.example.gulupoetry.db.MyDbHelper
import com.example.gulupoetry.fragment.MineFragment
import com.example.gulupoetry.fragment.PoetryFragment
import com.example.gulupoetry.fragment.SentenceFragment
import com.example.gulupoetry.functions.Common
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), View.OnClickListener {
    private var time1: Long = 0
    private var time2: Long = 0
    private val dbHelper = MyDbHelper(this, "User.db", 1)
    private val userData = ArrayList<UserData>()
    private val longTime = 60 * 60 * 24 * 30
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        poetry_rb_main.isChecked = true
        changeTab(PoetryFragment())
        poetry_rb_main.setOnClickListener(this)
        sentence_rb_main.setOnClickListener(this)
        mine_rb_main.setOnClickListener(this)
        val num = intent.getIntExtra("login", 1)
        if (num == 99) {
            val gollum = intent.getLongExtra("user_no", 1)
            User.user_no = gollum
            mine_rb_main.isChecked = true
            changeTab(MineFragment(gollum))
        }
        getUserMain()
    }

    private fun changeTab(fragment: Fragment, container: Int = R.id.container_main) {
        supportFragmentManager.beginTransaction().replace(container, fragment)
            .commitAllowingStateLoss()
    }

    override fun onClick(v: View?) {
        when (v) {
            poetry_rb_main -> {
                changeTab(PoetryFragment())
            }
            sentence_rb_main -> {
                changeTab(SentenceFragment())
            }
            mine_rb_main -> {
                userData.clear()
                var loginNum = 0
                var gollumNum: Long = 0
                val db = dbHelper.writableDatabase
                val cursor = db.query("User", null, null, null, null, null, null)
                if (cursor.moveToFirst()) {
                    var a: Long = 0
                    do {
                        val isLogin = cursor.getInt(cursor.getColumnIndex("is_login"))
                        val pwd = cursor.getString(cursor.getColumnIndex("pwd"))
                        val username = cursor.getString(cursor.getColumnIndex("username"))
                        val gollum = cursor.getString(cursor.getColumnIndex("gollum"))
                        val current = cursor.getString(cursor.getColumnIndex("current"))
                        val currentMarked = current.toLong()
                        userData.add(
                            UserData(
                                isLogin,
                                username,
                                gollum,
                                pwd,
                                currentMarked
                            )
                        )
                        for (user in userData) {
                            if (user.current > a) {
                                a = user.current
                                loginNum = user.isLogin
                                gollumNum = user.gollum.toLong()
                            }
                        }
                    } while (cursor.moveToNext())
                    val timeOfNow = System.currentTimeMillis()
                    val timeDifferent = (timeOfNow - a) / 1000
                    if (timeDifferent > longTime || timeDifferent < 0) {
                        val values = ContentValues()
                        values.put("is_login", 2)
                        values.put("pwd", "")
                        values.put("current", timeOfNow)
                        val rows =
                            db.update("User", values, "gollum = ?", arrayOf(gollumNum.toString()))
                        Log.i("Tag", "rows:$rows")
                        loginNum = 2
                    }
                }
                cursor.close()
                when (loginNum) {
                    0 -> {
                        //未登录过账号
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.putExtra("message", "请登录")
                        startActivity(intent)
                    }
                    1 -> {
                        //两次登录的时间间隔可以，，直接跳转我的页面
                        changeTab(MineFragment(gollumNum))

                    }
                    2 -> {
                        //时间间隔太长，重新登录
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.putExtra("message", "密码失效，请重新登录")
                        intent.putExtra("gollum", gollumNum)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        time1 = time2
        time2 = System.currentTimeMillis()
        if (time2 - time1 > 3 * 1000) {
            Common.myToast(this, "是否退出？")
        } else {
            ActivityCollector.finishAllActivity()
        }
    }

    private fun getUserMain() {
        userData.clear()
        var loginNum = 0
        var gollumNum: Long = 0
        val db = dbHelper.writableDatabase
        val cursor = db.query("User", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            var a: Long = 0
            do {
                val isLogin = cursor.getInt(cursor.getColumnIndex("is_login"))
                val pwd = cursor.getString(cursor.getColumnIndex("pwd"))
                val username = cursor.getString(cursor.getColumnIndex("username"))
                val gollum = cursor.getString(cursor.getColumnIndex("gollum"))
                val current = cursor.getString(cursor.getColumnIndex("current"))
                val currentMarked = current.toLong()
                userData.add(
                    UserData(
                        isLogin,
                        username,
                        gollum,
                        pwd,
                        currentMarked
                    )
                )
                for (user in userData) {
                    if (user.current > a) {
                        a = user.current
                        loginNum = user.isLogin
                        gollumNum = user.gollum.toLong()
                        User.user_no = user.gollum.toLong()
                    }
                }
            } while (cursor.moveToNext())
            val timeOfNow = System.currentTimeMillis()
            val timeDifferent = (timeOfNow - a) / 1000
            if (timeDifferent > longTime || timeDifferent < 0) {
                val values = ContentValues()
                values.put("is_login", 2)
                val rows =
                    db.update("User", values, "gollum = ?", arrayOf(gollumNum.toString()))
                Log.i("Tag", "rows:$rows")
                loginNum = 2
            }
            if (loginNum == 1) {
                val values = ContentValues()
                values.put("current", timeOfNow)
                val rows =
                    db.update("User", values, "gollum = ?", arrayOf(gollumNum.toString()))
                Log.i("Tag", "rows:$rows")
            }
        }
        cursor.close()
    }
}
