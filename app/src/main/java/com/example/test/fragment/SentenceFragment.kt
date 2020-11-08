package com.example.test.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.test.R
import com.example.test.adapter.MyViewPagerAdapter
import com.example.test.sentencefragment.TheAncientsFragment
import com.example.test.sentencefragment.ThreeWordsFragment
import kotlinx.android.synthetic.main.sentence_fragment_layout.*

class SentenceFragment : BaseFragment(R.layout.sentence_fragment_layout) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentList = listOf<Fragment>(ThreeWordsFragment(), TheAncientsFragment())
        val tabList = arrayListOf("五言佳句", "知古人")
        ViewPager_sentence_fragment.adapter = MyViewPagerAdapter(childFragmentManager, fragmentList)
        TabLayout_sentence_fragment.setupWithViewPager(ViewPager_sentence_fragment)
        for (i in 0 until tabList.size) {
            TabLayout_sentence_fragment.getTabAt(i)?.text = tabList[i]
        }
    }
}