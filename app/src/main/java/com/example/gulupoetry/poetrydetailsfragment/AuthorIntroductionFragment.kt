package com.example.gulupoetry.poetrydetailsfragment

import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.gulupoetry.R
import com.example.gulupoetry.data.AuthorIntroductionData
import com.example.gulupoetry.fragment.BaseFragment
import com.example.gulupoetry.netWork.CommonTask
import com.google.gson.Gson
import kotlinx.android.synthetic.main.author_introduction_fragment_layout.*

class AuthorIntroductionFragment(private val author: String) :
    BaseFragment(R.layout.author_introduction_fragment_layout) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        name_author_introduction_fragment.text = author
        //http://www.gulukai.cn/poetry/getpoetry/?p1=getauthorintroduction&p2=李白
        val commTask = CommonTask()
        commTask.url =
            "http://www.gulukai.cn/poetry/getpoetry/?p1=getauthorintroduction&p2=$author"
        commTask.setCallback {
            Log.i("Tag", "进来了！")
            val info = Gson().fromJson(it, AuthorIntroductionData::class.java)
            val data = info.code
            if (data == 201) {
                context_author_introduction_fragment.text = "该作者暂无介绍。"
            } else {
                context_author_introduction_fragment.text =
                    "\t\t\t\t" + info.data.author_introduction
            }
        }
        commTask.execute()
    }
}