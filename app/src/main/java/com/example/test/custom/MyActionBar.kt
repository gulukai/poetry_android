package com.example.test.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.example.test.R
import kotlinx.android.synthetic.main.action_bar_item_layout.view.*


class MyActionBar(context: Context?, attrs: AttributeSet?) :
    LinearLayout(context, attrs) {
    constructor(context: Context?) : this(context, null)

    init {
        LayoutInflater.from(context).inflate(R.layout.action_bar_item_layout, this, true)
    }

    private var style: ((back: TextView, txt: TextView, share: TextView, collection: TextView) -> Unit)? =
        null

    fun setStyle(style: ((back: TextView, txt: TextView, share: TextView, collection: TextView) -> Unit)) {
        this.style = style
        style.invoke(back_action_bar, text_action_bar, share_action_bar, collection_action_bar)
    }
}