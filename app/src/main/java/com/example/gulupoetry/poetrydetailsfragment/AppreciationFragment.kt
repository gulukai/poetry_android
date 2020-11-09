package com.example.gulupoetry.poetrydetailsfragment

import android.os.Bundle
import android.view.View
import com.example.gulupoetry.R
import com.example.gulupoetry.fragment.BaseFragment
import kotlinx.android.synthetic.main.appreciation_fragment_layout.*

class AppreciationFragment(private val title: String, private val appreciation: String) :
    BaseFragment(R.layout.appreciation_fragment_layout) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title_appreciation_fragment.text = title
        if (appreciation == "") {
            context_appreciation_fragment.text = "该古诗尚无赏析。"
        } else {
            context_appreciation_fragment.text = "\t\t\t\t" + appreciation
        }
    }
}