package com.example.gulupoetry

import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.gulupoetry.base.BaseActivity
import com.example.gulupoetry.data.MyPoetryDetailsDataList
import com.example.gulupoetry.data.PoetryWithFirst
import com.example.gulupoetry.functions.Common
import com.example.gulupoetry.netWork.CommonTask
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_poetry_list.*

class PoetryListActivity : BaseActivity() {
    private val poetryArrayList = arrayListOf<PoetryWithFirst>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poetry_list)
        val bundle = intent.extras
        //http://www.gulukai.cn/poetry/getpoetry/?p1=tag&p2=思乡
        for (key in bundle!!.keySet()) {
            Log.i("Tag", "Key=" + key + ", content=" + bundle.getString(key))
            when (key) {
                "全局搜索" -> {
                    val str = bundle.getString("全局搜索")
                }
                "朝代" -> {
                    val str = bundle.getString("朝代")
                    getPoetryList("dynasty", str.toString())
                }
                "题目" -> {
                    val str = bundle.getString("题目")
                    getPoetryList("title", str.toString())
                }
                "作者" -> {
                    val str = bundle.getString("作者")
                    getPoetryList("author", str.toString())
                }
                "标签" -> {
                    val str = bundle.getString("标签")
                    getPoetryList("tag", str.toString())
                }
            }

            action_bar_poetry_list.setStyle { back, txt, hear, collection, share ->
                back.setOnClickListener {
                    finish()
                }
                txt.text = "古诗词列表"
                hear.visibility = View.GONE
                share.visibility = View.GONE
                collection.visibility = View.GONE
            }
        }
    }

    private fun getPoetryList(label: String, keyword: String) {
        //http://www.gulukai.cn/poetry/getpoetry/?p1=tag&p2=思乡
        val commonTask = CommonTask()
        commonTask.url = "http://www.gulukai.cn/poetry/getpoetry/?p1=$label&p2=$keyword"
        commonTask.setCallback {
            val info = Gson().fromJson(it, MyPoetryDetailsDataList::class.java)
            if (info.code == 200) {
                val poetryList = info.data
                for (poetry in poetryList) {
                    val first = poetry.text.split("。")[0] + "。"
                    poetryArrayList.add(
                        PoetryWithFirst(
                            poetry.no,
                            poetry.author,
                            poetry.dynasty,
                            poetry.title,
                            first
                        )
                    )
                }
                Common().getPoetryWithFirst(
                    recycler_poetry_list_activity,
                    poetryArrayList,
                    R.layout.poetry_item_with_first_layout,
                    this,
                    this
                )
            } else {
                text_poetry_list.visibility = View.VISIBLE
            }

        }
        commonTask.execute()
    }
}