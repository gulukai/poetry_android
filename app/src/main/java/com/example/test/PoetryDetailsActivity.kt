package com.example.test

import android.graphics.drawable.DrawableContainer
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.test.base.BaseActivity
import com.example.test.data.PoetryDetailsData
import com.example.test.netWork.CommonTask
import com.example.test.poetrydetailsfragment.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_poetry_details.*

class PoetryDetailsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poetry_details)
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
            action_bar_poetry_details.setStyle { back, txt, share, collection ->
                back.setOnClickListener {
                    finish()
                }
                txt.text = "古诗详情"
                collection.setBackgroundResource(R.drawable.share)
                share.setBackgroundResource(R.drawable.headset_selected)

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