package com.example.test.sentencefragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.AncientActivity
import com.example.test.PoetryDetailsActivity
import com.example.test.R
import com.example.test.adapter.MyRecyclerViewAdapter
import com.example.test.data.TheAncientDataItem
import com.example.test.data.TheAncientsData
import com.example.test.fragment.BaseFragment
import com.example.test.netWork.CommonTask
import com.example.weatherapp.adapter.MyLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.poetry_item_layout.view.*
import kotlinx.android.synthetic.main.the_ancient_item.view.*
import kotlinx.android.synthetic.main.the_ancients_fragment_layout.*

class TheAncientsFragment : BaseFragment(R.layout.the_ancients_fragment_layout) {

    private val ancientList = ArrayList<TheAncientDataItem>()
    private val ancientId = ArrayList<String>()

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
                        intent.putExtra("ancientName",ancientList[position].ancient_name)
                        intent.putExtra("ancientIntroduce",ancientList[position].ancient_introduce)
                        startActivity(intent)
                    }
                }.create()
            recycler_the_ancients.layoutManager =
                LinearLayoutManager(this.context!!, LinearLayoutManager.VERTICAL, false)
        }
        commonTask.execute()
    }
}