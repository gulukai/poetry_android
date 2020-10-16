package com.example.test.screenfragment

import android.os.Bundle
import android.view.View
import com.example.test.R
import com.example.test.data.ItemData
import com.example.test.fragment.BaseFragment
import com.example.test.functions.Common
import kotlinx.android.synthetic.main.dynasty_fragment_layout.*

class DynastyFragment : BaseFragment(R.layout.dynasty_fragment_layout) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dynastyList = ArrayList<ItemData>()
        val dynastyArrayList = arrayListOf(
            "先秦",
            "汉朝",
            "魏晋",
            "南北朝",
            "隋朝",
            "唐朝",
            "五代",
            "宋朝",
            "金朝",
            "元朝",
            "明朝",
            "清朝",
            "现代",
            "近现代",
            "未知"
        )
        dynastyList.clear()
        for (i in dynastyArrayList) {
            dynastyList.add(
                ItemData(
                    i
                )
            )
        }
        Common().getItem(
            recycler_dynasty_fragment,
            dynastyList,
            R.layout.poetry_list_item_layout,
            this.activity!!,
            this.context!!,
            "朝代"
        )
    }
}