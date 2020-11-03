package com.example.test.mydialog

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import com.example.test.R
import kotlinx.android.synthetic.main.comment_dialog_layout.*
import kotlinx.android.synthetic.main.edit_dialog_layout.*
import kotlinx.android.synthetic.main.login_dialog_layout.*


class EditDialog(context: Context) : Dialog(context) {


    private var style: ((comment: EditText, cancel: TextView, release: TextView) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_dialog_layout)

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

    fun setStyle(style: ((comment: EditText, cancel: TextView, release: TextView) -> Unit)) {
        this.style = style
        this.style!!.invoke(text_edit_dialog, cancel_edit_dialog, release_edit_dialog)
    }
}