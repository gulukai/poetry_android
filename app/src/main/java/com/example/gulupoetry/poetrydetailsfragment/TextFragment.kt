package com.example.gulupoetry.poetrydetailsfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gulupoetry.R
import com.example.gulupoetry.adapter.MyRecyclerViewAdapter
import com.example.gulupoetry.data.TextData
import com.example.gulupoetry.fragment.BaseFragment
import com.example.weatherapp.adapter.MyLayoutManager
import kotlinx.android.synthetic.main.text_fragment_layout.*
import kotlinx.android.synthetic.main.text_list_layout.view.*

class TextFragment(
    private val text: String,
    private val author: String,
    private val dynasty: String,
    private val title: String,
    private val tag: List<String>
) :
    BaseFragment(R.layout.text_fragment_layout) {
    private val textArrayList = arrayListOf<TextData>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title_text_fragment.text = title
        dynasty_text_fragment.text = dynasty
        author_text_fragment.text = author
        val textList = text.replace("，", "，￥")
            .replace("。", "。￥").replace("“", "")
            .replace("”", "").replace("？", "？￥")
            .replace("！", "！￥").split("￥")
        for (text in 0..textList.size - 2) {
            textArrayList.add(TextData(textList[text]))
        }
        recycler_text_fragment.adapter =
            MyRecyclerViewAdapter.Builder().setDate(textArrayList).setViewHolder { parent, _ ->
                return@setViewHolder MyRecyclerViewAdapter().ViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.text_list_layout, parent, false)
                )
            }.setBindViewHolder { holder, position ->
                holder.itemView.text_list.text = textArrayList[position].string
            }.create()
        recycler_text_fragment.layoutManager =
            MyLayoutManager(this.activity!!, LinearLayoutManager.VERTICAL, false)
    }
}