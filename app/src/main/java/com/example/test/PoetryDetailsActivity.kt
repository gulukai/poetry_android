package com.example.test

import android.os.Bundle
import com.example.test.base.BaseActivity
import kotlinx.android.synthetic.main.activity_poetry_details.*

class PoetryDetailsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poetry_details)
        val poetryId = intent.getIntExtra("poetryId", 1)
        txt_poetry_details.text = poetryId.toString()
    }
}