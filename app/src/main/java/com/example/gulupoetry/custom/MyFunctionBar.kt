package com.example.gulupoetry.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.gulupoetry.R
import kotlinx.android.synthetic.main.function_bar_layout.view.*

class MyFunctionBar(context: Context?, attr: AttributeSet?) : LinearLayout(context, attr) {
    constructor(context: Context?) : this(context, null)

    init {
        LayoutInflater.from(context).inflate(R.layout.function_bar_layout, this, true)
    }

    private var style: ((image: ImageView, text: TextView) -> Unit)? = null
    fun setStyle(style: ((image: ImageView, text: TextView) -> Unit)) {
        this.style = style
        style.invoke(image_function_bar_layout, text_function_bar_layout)
    }
}