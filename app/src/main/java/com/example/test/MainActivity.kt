package com.example.test

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.test.base.ActivityCollector
import com.example.test.base.BaseActivity
import com.example.test.fragment.MineFragment
import com.example.test.fragment.PoetryFragment
import com.example.test.fragment.ZandingFragment
import com.example.test.functions.Common
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), View.OnClickListener {
    private var time1: Long = 0
    private var time2: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        poetry_rb_main.isChecked = true
        changeTab(PoetryFragment())
        poetry_rb_main.setOnClickListener(this)
        zanding_rb_main.setOnClickListener(this)
        mine_rb_main.setOnClickListener(this)
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
            zanding_rb_main -> {
                changeTab(ZandingFragment())
            }
            mine_rb_main -> {
                changeTab(MineFragment())
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
}