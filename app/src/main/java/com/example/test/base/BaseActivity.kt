package com.example.test.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.test.LoginActivity

open class BaseActivity : AppCompatActivity() {
    private val receiver = ForceOfflineReceiver()
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
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
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

}