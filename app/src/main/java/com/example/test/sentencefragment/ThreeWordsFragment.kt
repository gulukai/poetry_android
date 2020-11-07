package com.example.test.sentencefragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.PoetryDetailsActivity
import com.example.test.R
import com.example.test.adapter.MyRecyclerViewAdapter
import com.example.test.data.SentenceData
import com.example.test.data.SentenceItem
import com.example.test.data.TextData
import com.example.test.fragment.BaseFragment
import com.example.test.netWork.CommonTask
import com.google.gson.Gson
import com.scwang.smart.refresh.header.MaterialHeader
import kotlinx.android.synthetic.main.sentence_one_layout.view.*
import kotlinx.android.synthetic.main.sentence_two_layout.view.*
import kotlinx.android.synthetic.main.three_words_fragment_layout.*


class ThreeWordsFragment : BaseFragment(R.layout.three_words_fragment_layout) {

    private val sentenceList = ArrayList<SentenceItem>()
    private val sentenceId = ArrayList<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // http://www.gulukai.cn/poetry/getpoetry/?p1=getsentenceid&p2=105,222,764,1024,

        getSentence()
        refresh_three_words_layout.setRefreshHeader(MaterialHeader(this.context))
        refresh_three_words_layout.setOnRefreshListener { refreshlayout ->
            refreshlayout.finishRefresh(800 /*,false*/) //传入false表示刷新失败
            getSentence()
        }

    }

    private fun getSentence() {
        sentenceList.clear()
        sentenceId.clear()
        var str = ""
        for (i in 0..4) {
            val num = (105..2197).random()
            sentenceId.add(num.toString())
        }
        for (i in sentenceId) {
            str += "$i,"
        }
        val commonTask = CommonTask()
        commonTask.url = "http://www.gulukai.cn/poetry/getpoetry/?p1=getsentenceid&p2=$str"
        commonTask.setCallback {
            val info = Gson().fromJson(it, SentenceData::class.java)
            val sentenceList2 = info.data
            for (sentence in sentenceList2) {
                val textArrayList = ArrayList<TextData>()
                val textList = sentence.sentence.replace("，", "，￥")
                    .replace("。", "。￥").replace("“", "")
                    .replace("”", "").replace("？", "？￥")
                    .replace("！", "！￥").split("￥")
                for (text in 0..textList.size - 2) {
                    textArrayList.add(TextData(textList[text]))
                }
                sentenceList.add(
                    SentenceItem(
                        sentence.source,
                        sentence.poetry_id,
                        textArrayList
                    )
                )
            }
            recycler_three_words.adapter =
                MyRecyclerViewAdapter.Builder().setDate(sentenceList).setViewHolder { parent, _ ->
                    return@setViewHolder MyRecyclerViewAdapter().ViewHolder(
                        LayoutInflater.from(parent.context)
                            .inflate(R.layout.sentence_one_layout, parent, false)
                    )
                }.setBindViewHolder { holder, position ->
                    holder.itemView.text_sentence_one.text = sentenceList[position].source

                    holder.itemView.recycler_sentence_one.adapter =
                        MyRecyclerViewAdapter.Builder().setDate(sentenceList[position].list)
                            .setViewHolder { parent, _ ->
                                return@setViewHolder MyRecyclerViewAdapter().ViewHolder(
                                    LayoutInflater.from(parent.context)
                                        .inflate(R.layout.sentence_two_layout, parent, false)
                                )
                            }.setBindViewHolder { holder2, position2 ->
                                holder2.itemView.text_sentence_two.text =
                                    sentenceList[position].list[position2].string
                                holder2.itemView.setOnClickListener {
                                    val intent = Intent(activity, PoetryDetailsActivity::class.java)
                                    intent.putExtra("poetryId", sentenceList[position].poetry_id)
                                    context!!.startActivity(intent)
                                }
                            }.create()
                    holder.itemView.recycler_sentence_one.layoutManager =
                        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

                    holder.itemView.setOnClickListener {
                        val intent = Intent(activity, PoetryDetailsActivity::class.java)
                        intent.putExtra("poetryId", sentenceList[position].poetry_id)
                        context!!.startActivity(intent)
                    }
                }.create()
            recycler_three_words.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        }
        commonTask.execute()
    }
}