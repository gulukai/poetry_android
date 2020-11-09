package com.example.gulupoetry.mydialog

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.ViewGroup
import com.example.gulupoetry.R
import kotlinx.android.synthetic.main.takephoto_dialog.*

abstract class TheDialog(private val activity: Activity) : Dialog(activity, R.style.ActionSheetDialogStyle){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.takephoto_dialog)

        text3.setOnClickListener{
            btnPickByTake()
            cancel()
        }
        text4.setOnClickListener{
            btnPickBySelect()
            cancel()
        }
        text5.setOnClickListener {
            cancel()
        }
        setViewLocation()
        setCanceledOnTouchOutside(true) //外部点击取消
    }
    /**
     * 设置dialog位于屏幕底部
     */
    private fun setViewLocation() {
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        val height = dm.heightPixels
        val window = this.window!!
        val lp = window.attributes
        lp.x = 0
        lp.y = height
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT
        // 设置显示位置
        onWindowAttributesChanged(lp)
    }

    abstract fun btnPickBySelect()
    abstract fun btnPickByTake()

}