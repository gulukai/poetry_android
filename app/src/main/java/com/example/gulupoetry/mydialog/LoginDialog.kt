package com.example.gulupoetry.mydialog

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.widget.TextView
import com.example.gulupoetry.R
import kotlinx.android.synthetic.main.login_dialog_layout.*


class LoginDialog(context: Context) : Dialog(context) {


    private var style: ((comment: TextView, cancel: TextView, release: TextView) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_dialog_layout)

        //设置dialog的宽度为屏幕宽度的百分之八十
        val m = window?.windowManager
        val d = m?.defaultDisplay
        val p = window?.attributes
        val size = Point()
        d?.getSize(size)
        p?.width = (size.x * 0.7).toInt()
        window?.attributes = p

    }

    override fun setCancelable(flag: Boolean) {
        super.setCancelable(flag)
    }

    fun setStyle(style: ((comment: TextView, cancel: TextView, release: TextView) -> Unit)) {
        this.style = style
        this.style!!.invoke(text_login_dialog, cancel_login_dialog, release_login_dialog)
    }
}