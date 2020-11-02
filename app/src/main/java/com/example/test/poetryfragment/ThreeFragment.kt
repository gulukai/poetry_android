package com.example.test.poetryfragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.test.*
import com.example.test.adapter.MyRecyclerViewAdapter
import com.example.test.base.User
import com.example.test.data.AllCommentDataItem
import com.example.test.data.GetAllCommentList
import com.example.test.fragment.BaseFragment
import com.example.test.functions.Common
import com.example.test.netWork.CommonTask
import com.google.gson.Gson
import kotlinx.android.synthetic.main.all_comment_list_layout.view.*
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
                    val gollum = comment.gollum
                    val head = comment.head
                    val nickname = comment.nickname
                    val time = comment.time
                    val text = comment.text
                    val title = comment.poetry_name
                    val poetryId = comment.poetry_id
                    val headUrl = "http://www.gulukai.cn$head"
                    allCommentList.add(
                        AllCommentDataItem(
                            headUrl, nickname, time, text, title, poetryId,gollum
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
                            Glide.with(this).load(allCommentList[position].headUrl)
                                .into(holder.itemView.all_head_image_comment_list)
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
                            holder.itemView.all_head_image_comment_list.setOnClickListener {
                                if (User.user_no == allCommentList[position].gollum.toLong()){
                                    val intent =
                                        Intent(this.activity, MainActivity::class.java)
                                    intent.putExtra(
                                        "user_no",
                                        allCommentList[position].gollum.toLong()
                                    )
                                    intent.putExtra("login", 99)
                                    startActivity(intent)
                                }else{
                                    val intent =
                                        Intent(this.activity, OtherUserActivity::class.java)
                                    intent.putExtra(
                                        "user_no",
                                        allCommentList[position].gollum.toLong()
                                    )
                                    startActivity(intent)
                                }
                            }
                        }.create()
                recycler_three_fragment.layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            }
        }
        commonTask.execute()
    }

}