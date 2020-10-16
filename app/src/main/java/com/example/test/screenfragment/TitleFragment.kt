package com.example.test.screenfragment

import android.os.Bundle
import android.view.View
import com.example.test.netWork.CommonTask
import com.example.test.R
import com.example.test.data.ItemData
import com.example.test.data.TitleData
import com.example.test.fragment.BaseFragment
import com.example.test.functions.Common
import com.google.gson.Gson
import kotlinx.android.synthetic.main.title_fragment_layout.*

class TitleFragment : BaseFragment(R.layout.title_fragment_layout) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val titleArrayList = arrayListOf<ItemData>()
        val commonTask = CommonTask()
        commonTask.url = "http://www.gulukai.cn/poetry/getpoetry/?p1=search&p2=title"
        commonTask.setCallback {
            val json = Gson().fromJson(it, TitleData::class.java)
            val titleList = json.data.titles
            for (title in titleList) {
                titleArrayList.add(ItemData(title))
            }
            Common().getItem(
                recycler_title_fragment,
                titleArrayList,
                R.layout.poetry_list_item_layout,
                this.activity!!,
                this.context!!,
                "题目"
            )
        }
        commonTask.execute()
    }
}