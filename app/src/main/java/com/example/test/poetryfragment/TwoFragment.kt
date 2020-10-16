package com.example.test.poetryfragment

import android.os.Bundle
import android.view.View
import com.example.test.R
import com.example.test.data.PoetryWithFirstData
import com.example.test.fragment.BaseFragment
import com.example.test.functions.Common
import kotlinx.android.synthetic.main.two_fragment_layout.*

class TwoFragment : BaseFragment(R.layout.two_fragment_layout) {
    private val poetryWithFirstList = arrayListOf<PoetryWithFirstData>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        poetryWithFirstList.clear()
        initData()
        Common().getPoetryWithFirst(
            recycler_two_fragment,
            poetryWithFirstList,
            R.layout.poetry_item_with_first_layout,
            this.activity!!,
            this.context!!
        )

    }

    private fun initData() {
        for (i in 1 until 20) {
            poetryWithFirstList.add(
                PoetryWithFirstData(
                    "静夜思",
                    "唐朝",
                    "李白",
                    i,
                    "床前明月光，疑是地上霜。床前明月光，疑是地上霜。"
                )
            )
        }
    }
}