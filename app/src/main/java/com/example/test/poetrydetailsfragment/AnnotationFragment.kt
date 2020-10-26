package com.example.test.poetrydetailsfragment

import android.os.Bundle
import android.view.View
import com.example.test.R
import com.example.test.fragment.BaseFragment
import kotlinx.android.synthetic.main.annotation_fragment_layout.*

class AnnotationFragment(private val title: String, private val annotation: String) :
    BaseFragment(R.layout.annotation_fragment_layout) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title_annotation_fragment.text = title
        if (annotation == "") {
            context_annotation_fragment.text = "该古诗尚无注释。"
        } else {
            context_annotation_fragment.text = "\t\t\t\t" + annotation
        }

    }
}