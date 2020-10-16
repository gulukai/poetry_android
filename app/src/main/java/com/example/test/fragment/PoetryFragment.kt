package com.example.test.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.test.R
import com.example.test.ScreenActivity
import com.example.test.SearchActivity
import com.example.test.adapter.MyViewPagerAdapter
import com.example.test.functions.Common
import com.example.test.poetryfragment.OneFragment
import com.example.test.poetryfragment.ThreeFragment
import com.example.test.poetryfragment.TwoFragment
import kotlinx.android.synthetic.main.poetry_fragment_layout.*

class PoetryFragment : BaseFragment(R.layout.poetry_fragment_layout), View.OnClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentList = listOf<Fragment>(OneFragment(), TwoFragment(), ThreeFragment())
        val tabList = arrayListOf("一首", "诗词", "与君语")
        ViewPager_poetry_fragment.adapter = MyViewPagerAdapter(childFragmentManager, fragmentList)
        TabLayout_poetry_fragment.setupWithViewPager(ViewPager_poetry_fragment)
        for (i in 0 until tabList.size) {
            TabLayout_poetry_fragment.getTabAt(i)?.text = tabList[i]
        }
        classification_poetry_fragment.setOnClickListener(this)
        search_poetry_fragment.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            classification_poetry_fragment -> {
                Common().goActivity(this.context!!, ScreenActivity::class.java)
            }
            search_poetry_fragment->{
                Common().goActivity(this.context!!,SearchActivity::class.java)
            }
        }
    }
}