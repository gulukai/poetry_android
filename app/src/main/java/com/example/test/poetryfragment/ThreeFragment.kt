package com.example.test.poetryfragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.PoetryDetailsActivity
import com.example.test.PoetryListActivity
import com.example.test.R
import com.example.test.adapter.MyRecyclerViewAdapter
import com.example.test.data.AllCommentDataItem
import com.example.test.data.GetAllCommentList
import com.example.test.fragment.BaseFragment
import com.example.test.functions.Common
import com.example.test.netWork.CommonTask
import com.google.gson.Gson
import kotlinx.android.synthetic.main.all_comment_list_layout.view.*
import kotlinx.android.synthetic.main.poetry_list_item_layout.view.*
import kotlinx.android.synthetic.main.three_fragment_layout.*

class ThreeFragment : BaseFragment(R.layout.three_fragment_layout) {

    private val allCommentList = arrayListOf<AllCommentDataItem>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val commonTask = CommonTask()
        commonTask.url = "http://www.gulukai.cn/comment/getcommentlist/"
        commonTask.setCallback {
            val info = Gson().fromJson(it, GetAllCommentList::class.java)
            if (info.count != 0) {
                val commentList = info.data
                for (comment in commentList) {
                    val head = comment.head
                    val nickname = comment.nickname
                    val time = comment.time
                    val text = comment.text
                    val title = comment.poetry_name
                    val poetryId = comment.poetry_id
                    allCommentList.add(
                        AllCommentDataItem(
                            head, nickname, time, text, title,poetryId
                        )
                    )
                }
                recycler_three_fragment.adapter =
                    MyRecyclerViewAdapter.Builder().setDate(allCommentList)
                        .setViewHolder { parent, _ ->
                            return@setViewHolder MyRecyclerViewAdapter().ViewHolder(
                                LayoutInflater.from(parent.context)
                                    .inflate(R.layout.all_comment_list_layout, parent, false)
                            )
                        }.setBindViewHolder { holder, position ->
//                        holder.itemView.all_head_image_comment_list= allCommentList[position].
                            holder.itemView.all_nickname_comment_list.text =
                                allCommentList[position].nickname
                            holder.itemView.all_text_comment_list.text =
                                allCommentList[position].all_text
                            holder.itemView.all_time_comment_list.text =
                                Common().changeTime(allCommentList[position].time)
                            holder.itemView.all_title_comment_list.text =
                                "《${allCommentList[position].all_title}》"
                            holder.itemView.all_title_comment_list.setOnClickListener {
                                val intent = Intent(activity, PoetryDetailsActivity::class.java)
                                intent.putExtra("poetryId", allCommentList[position].poetry_id)
                                startActivity(intent)
                            }
                        }.create()
                recycler_three_fragment.layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            }
        }
        commonTask.execute()
    }

}