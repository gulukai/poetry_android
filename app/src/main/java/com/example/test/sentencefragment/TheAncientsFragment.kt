package com.example.test.sentencefragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.AncientActivity
import com.example.test.R
import com.example.test.adapter.MyRecyclerViewAdapter
import com.example.test.data.AuthorIntroductionData
import com.example.test.data.TheAncientDataItem
import com.example.test.data.TheAncientsData
import com.example.test.fragment.BaseFragment
import com.example.test.functions.Common
import com.example.test.netWork.CommonTask
import com.google.gson.Gson
import kotlinx.android.synthetic.main.the_ancient_item.view.*
import kotlinx.android.synthetic.main.the_ancients_fragment_layout.*

class TheAncientsFragment : BaseFragment(R.layout.the_ancients_fragment_layout) {

    private var ancientList = ArrayList<TheAncientDataItem>()
    private val ancientId = ArrayList<String>()
    private var adapter = MyRecyclerViewAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // http://www.gulukai.cn/poetry/getpoetry/?p1=getauthorsid&p2=1,2,3,
        ancientList.clear()
        ancientId.clear()
        var str = ""
        for (i in 0..7) {
            val num = (1..514).random()
            ancientId.add(num.toString())
        }
        for (i in ancientId) {
            str += "$i,"
        }
        val commonTask = CommonTask()
        commonTask.url = "http://www.gulukai.cn/poetry/getpoetry/?p1=getauthorsid&p2=$str"
        commonTask.setCallback {
            val info = Gson().fromJson(it, TheAncientsData::class.java)
            val theAncientsList = info.data
            for (ancient in theAncientsList) {
                ancientList.add(
                    TheAncientDataItem(
                        ancient.ad_no,
                        ancient.ad_name,
                        ancient.ad_introduce
                    )
                )
            }
            getAdapter()
        }
        commonTask.execute()

        edit_text_ancient_activity.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                getData()
                return@setOnEditorActionListener false
            }
            return@setOnEditorActionListener false
        }

        text_ancient_activity.setOnClickListener {
            getData()
        }
    }

    private fun getAdapter() {
        recycler_the_ancients.adapter =
            MyRecyclerViewAdapter.Builder().setDate(ancientList).setViewHolder { parent, _ ->
                return@setViewHolder MyRecyclerViewAdapter().ViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.the_ancient_item, parent, false)
                )
            }.setBindViewHolder { holder, position ->
                holder.itemView.name_the_ancient_item.text = ancientList[position].ancient_name
                holder.itemView.introduce_the_ancient_item.text =
                    ancientList[position].ancient_introduce
                holder.itemView.setOnClickListener {
                    val intent = Intent(this.context, AncientActivity::class.java)
                    intent.putExtra("ancientName", ancientList[position].ancient_name)
                    intent.putExtra("ancientIntroduce", ancientList[position].ancient_introduce)
                    startActivity(intent)
                }
            }.create()
        recycler_the_ancients.layoutManager =
            LinearLayoutManager(this.context!!, LinearLayoutManager.VERTICAL, false)
    }

    private fun getData() {
        if (edit_text_ancient_activity.text.toString() == "") {
            Common.myToast(this.context!!, "请输入你需要搜索的关键字！")
        } else {
            val commTask2 = CommonTask()
            val string = edit_text_ancient_activity.text.toString()
            commTask2.url =
                "http://www.gulukai.cn/poetry/getpoetry/?p1=getauthorintroduction&p2=$string"
            commTask2.setCallback {
                val info = Gson().fromJson(it, AuthorIntroductionData::class.java)
                val data = info.code
                if (data == 201) {
                    Common.myToast(this.context!!, "暂未收集该作者信息！")
                } else {
                    for (i in 0 until ancientList.size) {
                        ancientList.removeAt(0)
                        adapter.notifyItemRemoved(0)
                        adapter.notifyItemRangeChanged(0, ancientList.size - 1)
                    }
                    ancientList.add(
                        TheAncientDataItem(
                            info.data.no,
                            info.data.author_name,
                            info.data.author_introduction
                        )
                    )
                    Common().hideKeyboard(this.context!! as Activity)
                    getAdapter()
                }
            }
            commTask2.execute()
        }
    }
}