package com.example.test.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.test.PoetryCollectionActivity
import com.example.test.R
import com.example.test.functions.Common
import kotlinx.android.synthetic.main.my_fragment_layout.*

class MineFragment(private val gollum: Long) : BaseFragment(R.layout.my_fragment_layout) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.i("Tag", gollum.toString())

        collection_my_fragment.setStyle { image, text ->
            image.setImageResource(R.drawable.collection)
            text.text = "我的收藏"
        }
        security_my_fragment.setStyle { image, text ->
            image.setImageResource(R.drawable.security)
            text.text = "安全设置"
        }
        system_my_fragment.setStyle { image, text ->
            image.setImageResource(R.drawable.system)
            text.text = "系统设置"
        }
        about_us_my_fragment.setStyle { image, text ->
            image.setImageResource(R.drawable.about)
            text.text = "关于我们"
        }

        collection_my_fragment.setOnClickListener {
            Common().goActivity(this.context!!, PoetryCollectionActivity::class.java)
        }
    }
}