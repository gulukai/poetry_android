package com.example.test.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.example.test.R
import kotlinx.android.synthetic.main.my_data_bar_layout.view.*

class MyDataBar(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    constructor(context: Context) : this(context, null)

    init {
        LayoutInflater.from(context).inflate(R.layout.my_data_bar_layout, this, true)
    }

    private var style: ((head: TextView, content: TextView) -> Unit)? = null


    fun setStyle(style: ((head: TextView, content: TextView) -> Unit)) {
        this.style = style
        style.invoke(head_my_data, content_my_data)
    }
}