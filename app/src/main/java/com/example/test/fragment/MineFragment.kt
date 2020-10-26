package com.example.test.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import com.example.test.R
import com.example.test.functions.Change
import kotlinx.android.synthetic.main.my_fragment_layout.*

class MineFragment(private val gollum: Long) : BaseFragment(R.layout.my_fragment_layout) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.i("Tag", gollum.toString())
        collection_my_fragment.setStyle { image, text ->
            image.setImageResource(R.drawable.collection)
            text.text = "我的收藏"
        }
        comment_my_fragment.setStyle { image, text ->
            image.setImageResource(R.drawable.comment)
            text.text = "我的评论"
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

        head_portrait_my_fragment.setOnClickListener {
            try {
                val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(i, 99)
            } catch (e: Exception) {
                Log.d("Tag", e.toString())
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val revData = data?.extras
        when (requestCode) {
            99 -> {
                val bitMap: Bitmap = revData?.get("data") as Bitmap
                val str = Change().bitmapToString(bitMap, 100)
                Log.i("Tag", str)
                head_portrait_my_fragment.setImageBitmap(bitMap)
            }
        }

    }
}