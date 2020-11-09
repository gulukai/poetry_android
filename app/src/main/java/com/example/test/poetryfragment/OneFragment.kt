package com.example.test.poetryfragment

import android.os.Bundle
import android.view.View
import com.example.test.R
import com.example.test.data.PoetryData
import com.example.test.data.PoetryWithFirstData
import com.example.test.fragment.BaseFragment
import com.example.test.functions.Common
import com.example.test.functions.Lunar
import com.example.test.functions.SolarTerm
import com.example.test.netWork.CommonTask
import com.google.gson.Gson
import kotlinx.android.synthetic.main.one_fragment_layout.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class OneFragment : BaseFragment(R.layout.one_fragment_layout) {

    private val poetryList = ArrayList<PoetryData>()
    private val poetryId = ArrayList<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getImage()
        val today: Calendar = Calendar.getInstance()
        val lunar = Lunar(today)
        val mLunar = lunar.cyclical() + "年" + lunar.toString() //这样就能获取当前的农历信息
        text_lunar_one_fragment.text = mLunar
        poetryList.clear()
        poetryId.clear()
        var str = ""
        for (i in 0..4) {
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
                    PoetryData(poetry.title, poetry.dynasty, poetry.author, poetry.no)
                )
            }
            Common().getPoetry(
                recycler_one_fragment,
                poetryList,
                R.layout.poetry_item_layout,
                this.activity!!,
                this.context!!
            )
        }
        commonTask.execute()
    }


    private fun getImage() {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val format: String = sdf.format(System.currentTimeMillis())
        text_show_time_one_fragment.text = format
        val timeList = format.split("-")
        val timeList2 = mutableListOf<Int>()
        for (i in timeList) {
            timeList2.add(i.toInt())
        }
        val str = SolarTerm.getTermName(timeList2[0], timeList2[1], timeList2[2])
        when (str) {
            null -> {
                image_one_fragment.setImageResource(R.drawable.poetry_image)
            }
            "冬至" -> {
                image_one_fragment.setImageResource(R.drawable.dongzhi)
            }
            "小寒" -> {
                image_one_fragment.setImageResource(R.drawable.xiaohan)
            }
            "大寒" -> {
                image_one_fragment.setImageResource(R.drawable.dahan)
            }
            "立春" -> {
                image_one_fragment.setImageResource(R.drawable.lichun)
            }
            "雨水" -> {
                image_one_fragment.setImageResource(R.drawable.yushui)
            }
            "惊蛰" -> {
                image_one_fragment.setImageResource(R.drawable.jingzhe)
            }
            "春分" -> {
                image_one_fragment.setImageResource(R.drawable.chunfen)
            }
            "清明" -> {
                image_one_fragment.setImageResource(R.drawable.qingming)
            }
            "谷雨" -> {
                image_one_fragment.setImageResource(R.drawable.guyu)
            }
            "立夏" -> {
                image_one_fragment.setImageResource(R.drawable.lixia2)
            }
            "小满" -> {
                image_one_fragment.setImageResource(R.drawable.xiaoman)
            }
            "芒种" -> {
                image_one_fragment.setImageResource(R.drawable.mangzhong)
            }
            "夏至" -> {
                image_one_fragment.setImageResource(R.drawable.xiazhi)
            }
            "小暑" -> {
                image_one_fragment.setImageResource(R.drawable.xiaoshu)
            }
            "大暑" -> {
                image_one_fragment.setImageResource(R.drawable.dashu)
            }
            "立秋" -> {
                image_one_fragment.setImageResource(R.drawable.liqiu)
            }
            "处暑" -> {
                image_one_fragment.setImageResource(R.drawable.chushu)
            }
            "白露" -> {
                image_one_fragment.setImageResource(R.drawable.bailu)
            }
            "秋分" -> {
                image_one_fragment.setImageResource(R.drawable.qiufen)
            }
            "寒露" -> {
                image_one_fragment.setImageResource(R.drawable.hanlu)
            }
            "霜降" -> {
                image_one_fragment.setImageResource(R.drawable.shuangjiang)
            }
            "立冬" -> {
                image_one_fragment.setImageResource(R.drawable.lidong)
            }
            "小雪" -> {
                image_one_fragment.setImageResource(R.drawable.xiaoxue)
            }
            "大雪" -> {
                image_one_fragment.setImageResource(R.drawable.daxue)
            }
        }
    }
}