package com.example.gulupoetry.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.gulupoetry.LoginActivity

open class BaseActivity : AppCompatActivity() {
    private val receiver = ForceOfflineReceiver()
    private val receiver2 = ForceOfflineReceiver2()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCollector.addActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.example.broadcastbestpractice.FORCE_OFFLINE")
        registerReceiver(receiver, intentFilter)

        val intentFilter2 = IntentFilter()
        intentFilter2.addAction("com.example.broadcastbestpractice2.FORCE_OFFLINE")
        registerReceiver(receiver2, intentFilter2)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
        unregisterReceiver(receiver2)
    }

    inner class ForceOfflineReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            AlertDialog.Builder(context).apply {
                setTitle("警告")
                setMessage("你确定退出当前的账号嘛？")
                setPositiveButton("是") { _, _ ->
                    ActivityCollector.finishAllActivity()
                    val i = Intent(context, LoginActivity::class.java)
                    i.putExtra("message", "请登录")
                    context.startActivity(i)
                }
                show()
            }
        }
    }

    inner class ForceOfflineReceiver2 : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            ActivityCollector.finishAllActivity()
            val i = Intent(context, LoginActivity::class.java)
            i.putExtra("message", "身份已过期，请重新登录！")
            context.startActivity(i)
        }
    }
}