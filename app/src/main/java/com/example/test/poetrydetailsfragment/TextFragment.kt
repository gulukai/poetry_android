package com.example.test.poetrydetailsfragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.adapter.MyRecyclerViewAdapter
import com.example.test.data.TextData
import com.example.test.fragment.BaseFragment
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
//        Log.i("Tag", "'$text'")
        Log.i("Tag", textList.size.toString())
        for (text in 0..textList.size - 2) {
            Log.i("Tag", textList[text])
            textArrayList.add(TextData(textList[text]))
        }
//        initData(textList)
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

//    private fun initData(list: List<String>) {
//        for (text in 0..list.size - 2) {
//            Log.i("Tag",list.size)
//            textArrayList.add(TextData(list[text]))
//        }
//    }
}