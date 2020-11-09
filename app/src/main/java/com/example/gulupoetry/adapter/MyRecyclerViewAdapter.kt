package com.example.gulupoetry.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MyRecyclerViewAdapter(private var poetryList: ArrayList<*>?) :
    RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>() {

    constructor() : this(null)

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    var viewHolder: ((parent: ViewGroup, viewType: Int) -> ViewHolder)? = null
    private var bindViewHolder: ((holder: MyRecyclerViewAdapter.ViewHolder, position: Int) -> Unit)? =
        null


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyRecyclerViewAdapter.ViewHolder {
        return viewHolder!!.invoke(parent, viewType)
    }

    override fun onBindViewHolder(holder: MyRecyclerViewAdapter.ViewHolder, position: Int) {
        return bindViewHolder!!.invoke(holder, position)
    }

    override fun getItemCount(): Int = poetryList!!.size

    class Builder {
        private val myRecyclerViewAdapter: MyRecyclerViewAdapter = MyRecyclerViewAdapter()
        fun setDate(poetryList: ArrayList<*>?): Builder {
            myRecyclerViewAdapter.poetryList = poetryList
            return this
        }

        fun setViewHolder(viewHolder: ((parent: ViewGroup, viewType: Int) -> ViewHolder)): Builder {
            myRecyclerViewAdapter.viewHolder = viewHolder
            return this
        }

        fun setBindViewHolder(bindViewHolder: ((holder: MyRecyclerViewAdapter.ViewHolder, position: Int) -> Unit)): Builder {
            myRecyclerViewAdapter.bindViewHolder = bindViewHolder
            return this
        }

        fun create(): MyRecyclerViewAdapter {
            return myRecyclerViewAdapter
        }
    }

}