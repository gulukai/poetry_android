package com.example.gulupoetry.functions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gulupoetry.PoetryDetailsActivity
import com.example.gulupoetry.PoetryListActivity
import com.example.gulupoetry.adapter.MyRecyclerViewAdapter
import com.example.gulupoetry.data.ItemData
import com.example.gulupoetry.data.PoetryData
import com.example.gulupoetry.data.PoetryWithFirst
import com.example.weatherapp.adapter.MyLayoutManager
import kotlinx.android.synthetic.main.poetry_item_layout.view.*
import kotlinx.android.synthetic.main.poetry_item_with_first_layout.view.*
import kotlinx.android.synthetic.main.poetry_list_item_layout.view.*
import okhttp3.FormBody
import okhttp3.RequestBody
import java.lang.reflect.Method
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class Common {
    fun getPoetry(
        view: RecyclerView,
        poetryList: ArrayList<PoetryData>,
        layout: Int,
        activity: Activity,
        context: Context
    ) {
        view.adapter =
            MyRecyclerViewAdapter.Builder().setDate(poetryList).setViewHolder { parent, _ ->
                return@setViewHolder MyRecyclerViewAdapter().ViewHolder(
                    LayoutInflater.from(parent.context).inflate(layout, parent, false)
                )
            }.setBindViewHolder { holder, position ->
                holder.itemView.title_poetry_item.text = poetryList[position].title
                holder.itemView.dynasty_poetry_item.text = poetryList[position].dynasty
                holder.itemView.author_poetry_item.text = poetryList[position].author
                holder.itemView.setOnClickListener {
                    val intent = Intent(activity, PoetryDetailsActivity::class.java)
                    intent.putExtra("poetryId", poetryList[position].id)
                    context.startActivity(intent)
                }
            }.create()
        view.layoutManager = MyLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    fun getPoetryWithFirst(
        view: RecyclerView,
        poetryList: ArrayList<PoetryWithFirst>,
        layout: Int,
        activity: Activity,
        context: Context
    ) {
        view.adapter =
            MyRecyclerViewAdapter.Builder().setDate(poetryList).setViewHolder { parent, _ ->
                return@setViewHolder MyRecyclerViewAdapter().ViewHolder(
                    LayoutInflater.from(parent.context).inflate(layout, parent, false)
                )
            }.setBindViewHolder { holder, position ->
                holder.itemView.title_poetry_with_first_item.text = poetryList[position].title
                holder.itemView.dynasty_poetry_with_first_item.text = poetryList[position].dynasty
                holder.itemView.author_poetry_with_first_item.text = poetryList[position].author
                holder.itemView.first_poetry_with_first_item.text = poetryList[position].first
                holder.itemView.setOnClickListener {
                    val intent = Intent(activity, PoetryDetailsActivity::class.java)
                    intent.putExtra("poetryId", poetryList[position].no)
                    context.startActivity(intent)
                }
            }.create()
        view.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    fun getItem(
        view: RecyclerView,
        itemList: ArrayList<ItemData>,
        layout: Int,
        activity: Activity,
        context: Context,
        key: String
    ) {
        view.adapter =
            MyRecyclerViewAdapter.Builder().setDate(itemList).setViewHolder { parent, _ ->
                return@setViewHolder MyRecyclerViewAdapter().ViewHolder(
                    LayoutInflater.from(parent.context).inflate(layout, parent, false)
                )
            }.setBindViewHolder { holder, position ->
                holder.itemView.title_poetry_list_item.text = itemList[position].str
                holder.itemView.setOnClickListener {
                    val intent = Intent(activity, PoetryListActivity::class.java)
                    val bundle = Bundle()
                    bundle.putString(key, holder.itemView.title_poetry_list_item.text.toString())
                    intent.putExtras(bundle)
                    context.startActivity(intent)
                }
            }.create()
        view.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    fun goActivity(context: Context, cls: Class<*>) {
        val intent = Intent(context, cls)
        context.startActivity(intent)
    }

    companion object {
        var toast: Toast? = null
        fun myToast(context: Context, message: String) {
            if (toast == null) {
                toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            } else {
                toast!!.setText(message)
            }
            toast?.show()
        }
    }

    /**
     * 手机号验证
     * @param str
     * @return 验证通过返回true
     */
    fun isMobile(str: String): Boolean {
        var p: Pattern? = null
        var m: Matcher? = null
        var b = false
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$") // 验证手机号
        m = p.matcher(str)
        b = m.matches()
        return b
    }

    /**
     * 邮箱地址验证
     * @param str
     * @return 验证通过返回true
     */
    fun isEmail(string: String?): Boolean {
        if (string == null) return false
        val regEx1 =
            "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$"
        val p: Pattern
        val m: Matcher
        p = Pattern.compile(regEx1)
        m = p.matcher(string)
        return m.matches()
    }


    fun buildParams(param: Map<String, String>?): RequestBody {
        var params: Map<String, String>? = param
        if (params == null) {
            params = HashMap<String, String>()
        }
        val builder = FormBody.Builder()
        for (entry in params.entries) {
            val key = entry.key
            var value: String? = entry.value
            if (value == null) {
                value = ""
            }
            builder.add(key, value)
        }
        return builder.build()
    }

    fun setDontShowSoftInputWhenFocused(editText: EditText) {
        try {
            val setShowSoftInputOnFocus: Method = editText.javaClass.getMethod(
                "setShowSoftInputOnFocus",
                Boolean::class.javaPrimitiveType
            )
            setShowSoftInputOnFocus.isAccessible = true
            setShowSoftInputOnFocus.invoke(editText, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun changeTime(time: String): String {
        val currentTime = System.currentTimeMillis()
        val time2 = time.toLong()
        val x: Int = ((currentTime - time2) / (1000 * 60 * 60)).toInt()
        return if (x == 0) {
            "${((currentTime - time2) / (1000 * 60)).toInt().toString()}分钟前"
        } else if (x in 1..24) {
            "${x}小时前"
        } else if (x in 25..48) {
            "昨天"
        } else if (x > 48) {
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            return sdf.format(time2)
        } else {
            "空"
        }
    }

    /**
     * 隐藏键盘的方法
     *
     * @param context
     */
    fun hideKeyboard(context: Activity) {
        val imm: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(context.window.decorView.windowToken, 0)
    }

}