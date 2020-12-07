package com.example.gulupoetry

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gulupoetry.adapter.MyRecyclerViewAdapter
import com.example.gulupoetry.base.BaseActivity
import com.example.gulupoetry.data.ItemData
import com.example.gulupoetry.functions.Common
import com.example.gulupoetry.functions.SpeacesItemDecoration
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.hot_search_list_item_layout.view.*

class SearchActivity : BaseActivity(), View.OnClickListener {
    private val hotSearchList = arrayListOf<ItemData>()
    private val list = arrayListOf("李白", "李商隐", "杜甫", "白居易", "李煜", "崔郊", "陆游", "苏轼", "孟郊")
    private var checked = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchList = arrayOf("题目", "作者", "朝代", "标签")
        spinner_search_activity.adapter =
            ArrayAdapter(this, R.layout.my_spinner_layout, searchList)
        spinner_search_activity.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    checked = searchList[position]
                }
            }

        back_search_activity.setOnClickListener(this)
        classified_search_activity.setOnClickListener(this)
        text_search_activity.setOnClickListener {
            if (edit_text_search_activity.text.toString() == "") {
                Common.myToast(this, "请输入你需要搜索的关键字！")
            } else {
                when (checked) {
                    "不限" -> {
                        val intent = Intent(this, PoetryListActivity::class.java)
                        val bundle = Bundle()
                        bundle.putString("全局搜索", edit_text_search_activity.text.toString())
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }
                    "题目" -> {
                        val intent = Intent(this, PoetryListActivity::class.java)
                        val bundle = Bundle()
                        bundle.putString("题目", edit_text_search_activity.text.toString())
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }
                    "作者" -> {
                        val intent = Intent(this, PoetryListActivity::class.java)
                        val bundle = Bundle()
                        bundle.putString("作者", edit_text_search_activity.text.toString())
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }
                    "朝代" -> {
                        val intent = Intent(this, PoetryListActivity::class.java)
                        val bundle = Bundle()
                        bundle.putString("朝代", edit_text_search_activity.text.toString())
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }
                    "标签" -> {
                        val intent = Intent(this, PoetryListActivity::class.java)
                        val bundle = Bundle()
                        bundle.putString("标签", edit_text_search_activity.text.toString())
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }
                }
            }
        }
        edit_text_search_activity.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                if (edit_text_search_activity.text.toString() == "") {
                    Common.myToast(this, "请输入你需要搜索的关键字！")
                } else {
                    when (checked) {
                        "题目" -> {
                            searchToActivity("题目")
                        }
                        "作者" -> {
                            searchToActivity("作者")
                        }
                        "朝代" -> {
                            searchToActivity("朝代")
                        }
                        "标签" -> {
                            searchToActivity("标签")
                        }
                    }
                }
                return@setOnEditorActionListener false
            }
            return@setOnEditorActionListener false
        }
        initData()
        recycler_search_activity.adapter =
            MyRecyclerViewAdapter.Builder().setDate(hotSearchList)
                .setViewHolder { parent, _ ->
                    return@setViewHolder MyRecyclerViewAdapter().ViewHolder(
                        LayoutInflater.from(parent.context)
                            .inflate(R.layout.hot_search_list_item_layout, parent, false)
                    )
                }
                .setBindViewHolder { holder, position ->
                    holder.itemView.title_poetry_hot_search_item.text = hotSearchList[position].str
                    holder.itemView.setOnClickListener {
                        val intent = Intent(this, PoetryListActivity::class.java)
                        val bundle = Bundle()
                        bundle.putString(
                            "作者",
                            holder.itemView.title_poetry_hot_search_item.text.toString()
                        )
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }
                }.create()
        recycler_search_activity.layoutManager =
            GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        recycler_search_activity.addItemDecoration(SpeacesItemDecoration(10))
    }

    override fun onClick(v: View?) {
        when (v) {
            back_search_activity -> {
                finish()
            }
            classified_search_activity -> {
                Common().goActivity(this, ScreenActivity::class.java)
            }
        }
    }

    private fun initData() {
        for (i in list) {
            hotSearchList.add(
                ItemData(
                    i
                )
            )
        }
    }


    private fun searchToActivity(str: String) {
        val intent = Intent(this, PoetryListActivity::class.java)
        val bundle = Bundle()
        bundle.putString(str, edit_text_search_activity.text.toString())
        intent.putExtras(bundle)
        startActivity(intent)
    }
}