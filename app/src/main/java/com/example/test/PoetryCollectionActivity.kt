package com.example.test

import android.os.Bundle
import android.view.View
import com.example.test.base.BaseActivity
import com.example.test.data.PoetryWithFirst
import com.example.test.data.PoetryWithFirstData
import com.example.test.db.MyDbHelper
import com.example.test.functions.Common
import com.example.test.netWork.CommonTask
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_poetry_collection.*

class PoetryCollectionActivity : BaseActivity() {
    private val dbHelper = MyDbHelper(this, "User.db", 1)
    private val poetryList = ArrayList<PoetryWithFirst>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poetry_collection)
        action_bar_collection.setStyle { back, txt, hear, collection, share ->
            back.setOnClickListener {
                finish()
            }
            txt.text = "收藏的诗集"
            hear.visibility = View.GONE
            collection.visibility = View.GONE
            share.visibility = View.GONE
        }
        var str = ""
        val db = dbHelper.writableDatabase
        val cursor = db.query("PoetryCollection", null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            val poetryNum = cursor.getInt(cursor.getColumnIndex("poetry_no"))
            str += "$poetryNum,"
        }
        cursor.close()
        if (str != "") {
            val commonTask = CommonTask()
            commonTask.url = "http://www.gulukai.cn/poetry/getpoetry/?p1=getauthorid&p2=$str"
            commonTask.setCallback {
                val info = Gson().fromJson(it, PoetryWithFirstData::class.java)
                val poetryWithFirstList = info.data
                for (poetry in poetryWithFirstList) {
                    poetryList.add(
                        PoetryWithFirst(
                            poetry.no,
                            poetry.author,
                            poetry.dynasty,
                            poetry.title,
                            poetry.firstText
                        )
                    )
                }
                Common().getPoetryWithFirst(
                    recycler_poetry_collection,
                    poetryList,
                    R.layout.poetry_item_with_first_layout,
                    this,
                    this
                )
            }
            commonTask.execute()
        }
    }
}