package com.example.test

import android.content.ContentValues
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.test.base.BaseActivity
import com.example.test.data.PoetryDetailsData
import com.example.test.db.MyDbHelper
import com.example.test.functions.Common
import com.example.test.netWork.CommonTask
import com.example.test.poetrydetailsfragment.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_poetry_details.*

class PoetryDetailsActivity : BaseActivity() {
    private val myHelper = MyDbHelper(this, "User.db", 1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poetry_details)
        val db = myHelper.writableDatabase
        comment_poetry_details_activity.setStyle { image, text ->
            text.text = "这首诗有0条评论"
            image.visibility = View.GONE
        }

        val poetryId = intent.getIntExtra("poetryId", 1)
        val commTask = CommonTask()
        commTask.url = "http://www.gulukai.cn/poetry/getpoetry/?p1=getpoetrybyid&p2=$poetryId"
        commTask.setCallback {
            val info = Gson().fromJson(it, PoetryDetailsData::class.java)
            val no = info.data.no
            val annotation: String = info.data.annotation
            val appreciation: String = info.data.appreciation
            val author: String = info.data.author
            val dynasty: String = info.data.dynasty
            val text: String = info.data.text
            val title: String = info.data.title
            val translation: String = info.data.translation
            val tag: List<String> = info.data.tag
            action_bar_poetry_details.setStyle { back, txt, hear, collection, share ->
                val cursor = db.query(
                    "PoetryCollection",
                    null,
                    "poetry_no = ?",
                    arrayOf("$no"),
                    null,
                    null,
                    null,
                    null
                )
                if (cursor.moveToNext()) {
                    collection.isChecked = true
                    collection.setBackgroundResource(R.drawable.ic_collection_selected)
                } else {
                    collection.setBackgroundResource(R.drawable.ic_collection)
                }
                cursor.close()
                back.setOnClickListener {
                    finish()
                }
                txt.text = "古诗详情"
                hear.setBackgroundResource(R.drawable.headset)
                share.setBackgroundResource(R.drawable.share)
                collection.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        val values = ContentValues().apply {
                            put("poetry_no", no)
                        }
                        val row =
                            db.update("PoetryCollection", values, "poetry_no = ?", arrayOf("$no"))
                        if (row != 1) {
                            db.insert("PoetryCollection", null, values)
                        }
                        buttonView.setBackgroundResource(R.drawable.ic_collection_selected)
                        Common.myToast(this, "收藏古诗成功。")
                    } else {
                        db.delete("PoetryCollection", "poetry_no = ?", arrayOf("$no"))
                        buttonView.setBackgroundResource(R.drawable.ic_collection)
                        Common.myToast(this, "取消收藏。")
                    }
                }
            }
            text_radio_button.isChecked = true
            changeTabInDetails(TextFragment(text, author, dynasty, title, tag))
            annotation_radio_button.setOnClickListener {
                changeTabInDetails(AnnotationFragment(title, annotation))
            }
            appreciation_radio_button.setOnClickListener {
                changeTabInDetails(AppreciationFragment(title, appreciation))
            }
            author_radio_button.setOnClickListener {
                changeTabInDetails(AuthorIntroductionFragment(author))
            }
            text_radio_button.setOnClickListener {
                changeTabInDetails(TextFragment(text, author, dynasty, title, tag))
            }
            translation_radio_button.setOnClickListener {
                changeTabInDetails(TranslationFragment(title, translation))
            }
        }
        commTask.execute()
    }

    private fun changeTabInDetails(fragment: Fragment, container: Int = R.id.frame_poetry_details) {
        supportFragmentManager.beginTransaction().replace(container, fragment)
            .commitAllowingStateLoss()

    }
}