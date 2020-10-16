package com.example.weatherapp.adapter

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MyLayoutManager(context: Context, orientation: Int, reverseLayout: Boolean) :
    LinearLayoutManager(context, orientation, reverseLayout) {
    constructor(context: Context) : this(context, LinearLayout.VERTICAL, false)
    constructor(context: Context, orientation: Int) : this(context, orientation, false)

    private var myMeasuredDimension = IntArray(2)

    override fun isAutoMeasureEnabled(): Boolean {
        return false
    }

    override fun onMeasure(
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State,
        widthSpec: Int,
        heightSpec: Int
    ) {
        fun init(
            recycler: RecyclerView.Recycler,
            position: Int,
            widthSpec: Int,
            heightSpec: Int,
            dimension: IntArray
        ) {
            val view = recycler.getViewForPosition(position)
            if (view != null) {
                val p = view.layoutParams as RecyclerView.LayoutParams
                val childHeightSpec =
                    ViewGroup.getChildMeasureSpec(heightSpec, paddingTop + paddingBottom, p.height)
                view.measure(widthSpec, childHeightSpec)
                dimension[0] = view.measuredWidth + p.leftMargin + p.rightMargin
                dimension[1] = view.measuredHeight + p.topMargin + p.bottomMargin
                recycler.recycleView(view)
            }
        }


        val widthMode = View.MeasureSpec.getMode(widthSpec)
        val heightMode = View.MeasureSpec.getMode(heightSpec)
        val widthSize = View.MeasureSpec.getSize(widthSpec)
        val heightSize = View.MeasureSpec.getSize(heightSpec)
        var width = 0
        var height = 0
        for (i in 0 until itemCount) {

            init(
                recycler,
                i,
                widthSpec,
                View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                myMeasuredDimension
            )




            if (orientation == LinearLayout.VERTICAL) {
                height += myMeasuredDimension[1]
                if (i == 0) {
                    width = myMeasuredDimension[0]
                }
            } else {
                width += myMeasuredDimension[0]
                if (i == 0) {
                    height = myMeasuredDimension[1]
                }
            }
        }
        when (widthMode) {
            View.MeasureSpec.EXACTLY -> {
            }
            View.MeasureSpec.AT_MOST -> {
            }
            View.MeasureSpec.UNSPECIFIED -> {
            }
        }
        when (heightMode) {
            View.MeasureSpec.EXACTLY -> {
                height = heightSize
            }
            View.MeasureSpec.AT_MOST -> {
            }
            View.MeasureSpec.UNSPECIFIED -> {
            }
        }
        setMeasuredDimension(widthSize, height)
        Log.i("myNews", "到这了")
    }

    override fun canScrollVertically(): Boolean {
        return false
    }
}