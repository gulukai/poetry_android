package com.example.test

import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.test.base.BaseActivity
import kotlinx.android.synthetic.main.activity_poetry_list.*

class PoetryListActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poetry_list)
        val bundle = intent.extras
        for (key in bundle!!.keySet()) {
            Log.i("Tag", "Key=" + key + ", content=" + bundle.getString(key))
            when (key) {
                "全局搜索" -> {
                    text_poetry_list.text = bundle.getString("全局搜索")
                }
                "朝代" -> {
                    text_poetry_list.text = bundle.getString("朝代")
                }
                "题目" -> {
                    text_poetry_list.text = bundle.getString("题目")
                }
                "作者" -> {
                    text_poetry_list.text = bundle.getString("作者")
                }
                "标签" -> {
                    text_poetry_list.text = bundle.getString("标签")
                }
            }
        }

        action_bar_poetry_list.setStyle { back, txt, share, collection ->
            back.setOnClickListener {
                finish()
            }
            txt.text = "古诗词列表"
            share.visibility = View.GONE
            collection.visibility = View.GONE
        }
    }
}