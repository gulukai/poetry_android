package com.example.test.poetrydetailsfragment

import android.os.Bundle
import android.view.View
import com.example.test.R
import com.example.test.fragment.BaseFragment
import kotlinx.android.synthetic.main.translation_fragment_layout.*

class TranslationFragment(private val title: String, private val translation: String) :
    BaseFragment(R.layout.translation_fragment_layout) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title_translation_fragment.text = title
        if (translation == "") {
            context_translation_fragment.text = "该古诗尚无翻译"
        } else {
            context_translation_fragment.text = "\t\t\t\t" + translation
        }

    }
}