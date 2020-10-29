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


class CommentDialog(context: Context) : Dialog(context) {


    private var style: ((comment: EditText, cancel: TextView, release: TextView) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.comment_dialog_layout)

        //设置dialog的宽度为屏幕宽度的百分之八十
        val m = window?.windowManager
        val d = m?.defaultDisplay
        val p = window?.attributes
        val size = Point()
        d?.getSize(size)
        p?.width = (size.x * 0.95).toInt()
        window?.attributes = p

    }

    override fun setCancelable(flag: Boolean) {
        super.setCancelable(flag)
    }

    override fun dismiss() {
        val view: View? = currentFocus
        if (view is EditText) {
            val mInputMethodManager: InputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mInputMethodManager.hideSoftInputFromWindow(
                view.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN
            )
        }
        super.dismiss()
    }

    fun setStyle(style: ((comment: EditText, cancel: TextView, release: TextView) -> Unit)) {
        this.style = style
        this.style!!.invoke(edit_comment_dialog, cancel_comment_dialog, release_comment_dialog)
    }
}