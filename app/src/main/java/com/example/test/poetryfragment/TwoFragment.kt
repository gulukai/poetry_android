package com.example.test.poetryfragment

import android.os.Bundle
import android.view.View
import com.example.test.R
import com.example.test.adapter.MyRecyclerViewAdapter
import com.example.test.data.PoetryWithFirst
import com.example.test.data.PoetryWithFirstData
import com.example.test.fragment.BaseFragment
import com.example.test.functions.Common
import com.example.test.netWork.CommonTask
import com.google.gson.Gson
import com.scwang.smart.refresh.footer.BallPulseFooter
import kotlinx.android.synthetic.main.two_fragment_layout.*

class TwoFragment : BaseFragment(R.layout.two_fragment_layout) {

    private val poetryList = ArrayList<PoetryWithFirst>()
    private val poetryListItem = ArrayList<PoetryWithFirst>()
    private val poetryId = ArrayList<String>()
    private var adapter = MyRecyclerViewAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        poetryList.clear()
        poetryId.clear()
        var str = ""
        for (i in 0..9) {
            val num = (1..2070).random()
            poetryId.add(num.toString())
        }
        for (i in poetryId) {
            str += "$i,"
        }
        val commonTask = CommonTask()
        commonTask.url = "http://www.gulukai.cn/poetry/getpoetry/?p1=getauthorid&p2=$str"
        commonTask.setCallback {
            val info = Gson().fromJson(it, PoetryWithFirstData::class.java)
            val poetryWithFirstList = info.data
            for (poetry in poetryWithFirstList) {
                poetryList.add(
                    PoetryWithFirst(
                        poetry.no,
                        poetry.author,
                        poetry.dynasty,
                        poetry.title,
                        poetry.firstText
                    )
                )
            }
            Common().getPoetryWithFirst(
                recycler_two_fragment,
                poetryList,
                R.layout.poetry_item_with_first_layout,
                this.activity!!,
                this.context!!
            )
        }
        commonTask.execute()

        refreshLayout.setRefreshFooter(BallPulseFooter(this.context))
        refreshLayout.setOnLoadMoreListener { refreshlayout ->
            refreshlayout.finishLoadMore(3000 /*,false*/) //传入false表示加载失败
            poetryId.clear()
            var str2 = ""
            for (i in 0..9) {
                val num = (1..2070).random()
                poetryId.add(num.toString())
            }
            for (i in poetryId) {
                str2 += "$i,"
            }
            val commonTask2 = CommonTask()
            commonTask2.url = "http://www.gulukai.cn/poetry/getpoetry/?p1=getauthorid&p2=$str2"
            commonTask2.setCallback {
                val info = Gson().fromJson(it, PoetryWithFirstData::class.java)
                val poetryWithFirstList = info.data
                for (poetry in poetryWithFirstList) {
                    poetryListItem.add(
                        PoetryWithFirst(
                            poetry.no,
                            poetry.author,
                            poetry.dynasty,
                            poetry.title,
                            poetry.firstText
                        )
                    )
                }
                for (i in poetryListItem) {
                    poetryList.add(i)
                }
                adapter.notifyItemInserted(poetryList.size - 1)
                recycler_two_fragment.scrollToPosition(poetryList.size - 11)
            }
            commonTask2.execute()
        }
    }
}