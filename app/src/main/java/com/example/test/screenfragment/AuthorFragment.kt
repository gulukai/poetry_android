package com.example.test.screenfragment

import android.os.Bundle
import android.view.View
import com.example.test.netWork.CommonTask
import com.example.test.R
import com.example.test.data.AuthorData
import com.example.test.data.ItemData
import com.example.test.fragment.BaseFragment
import com.example.test.functions.Common
import com.google.gson.Gson
import kotlinx.android.synthetic.main.author_fragment_layout.*

class AuthorFragment : BaseFragment(R.layout.author_fragment_layout) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val authorArrayList = arrayListOf<ItemData>()
        //http://www.gulukai.cn/poetry/getpoetry/?p1=search&p2=author
        val commonTask = CommonTask()
        commonTask.url = "http://www.gulukai.cn/poetry/getpoetry/?p1=search&p2=author"
        commonTask.setCallback {
            val info = Gson().fromJson(it, AuthorData::class.java)
            val authorList = info.data.authors
            for (author in authorList) {
                authorArrayList.add(ItemData(author))
            }
            Common().getItem(
                recycler_author_fragment,
                authorArrayList,
                R.layout.poetry_list_item_layout,
                this.activity!!,
                this.context!!,
                "作者"
            )
        }
        commonTask.execute()
    }
}