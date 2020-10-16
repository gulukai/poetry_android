package com.example.test

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.test.adapter.MyViewPagerAdapter
import com.example.test.base.BaseActivity
import com.example.test.screenfragment.AuthorFragment
import com.example.test.screenfragment.DynastyFragment
import com.example.test.screenfragment.TagFragment
import com.example.test.screenfragment.TitleFragment
import kotlinx.android.synthetic.main.activity_screen_acticity.*

class ScreenActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_acticity)
        val fragmentList =
            listOf<Fragment>(
                TitleFragment(),
                DynastyFragment(),
                AuthorFragment(),
                TagFragment()
            )
        val tagList = arrayListOf("题目", "朝代", "作者", "标签")
        view_pager_screen.adapter = MyViewPagerAdapter(supportFragmentManager, fragmentList)
        tab_layout_screen.setupWithViewPager(view_pager_screen)
        for (i in 0 until tagList.size) {
            tab_layout_screen.getTabAt(i)?.text = tagList[i]
        }
        action_bar_screen.setStyle { back, txt, share, collection ->
            back.setOnClickListener {
                finish()
            }
            txt.text = "筛选"
            share.visibility = View.GONE
            collection.visibility = View.GONE
        }
    }
}