package com.example.gulupoetry.screenfragment

import android.os.Bundle
import android.view.View
import com.example.gulupoetry.netWork.CommonTask
import com.example.gulupoetry.R
import com.example.gulupoetry.data.ItemData
import com.example.gulupoetry.data.TagData
import com.example.gulupoetry.fragment.BaseFragment
import com.example.gulupoetry.functions.Common
import com.google.gson.Gson
import kotlinx.android.synthetic.main.tag_fragment_layout.*

class TagFragment : BaseFragment(R.layout.tag_fragment_layout) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tagArrayList = arrayListOf<ItemData>()
        val commonTask = CommonTask()
        commonTask.url = "http://www.gulukai.cn/poetry/getpoetry/?p1=search&p2=tag"
        commonTask.setCallback {
            val info = Gson().fromJson(it, TagData::class.java)
            val tagList = info.data.tags
            for (tag in tagList) {
                tagArrayList.add(ItemData(tag))
            }
            Common().getItem(
                recycler_tag_fragment,
                tagArrayList,
                R.layout.poetry_list_item_layout,
                this.activity!!,
                this.context!!,
                "标签"
            )
        }
        commonTask.execute()
    }
}