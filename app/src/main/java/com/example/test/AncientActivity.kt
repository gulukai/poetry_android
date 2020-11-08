package com.example.test

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.test.base.BaseActivity
import kotlinx.android.synthetic.main.activity_ancient.*

class AncientActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ancient)

        action_bar_ancient.setStyle { back, txt, hear, collection, share ->
            back.setOnClickListener {
                finish()
            }
            txt.text = "作者介绍"
            hear.visibility = View.GONE
            collection.visibility = View.GONE
            share.visibility = View.GONE
        }
        val ancientName = intent.getStringExtra("ancientName")
        val ancientIntroduce = intent.getStringExtra("ancientIntroduce")
        author_poetry.setOnClickListener {
            val intent = Intent(this, PoetryListActivity::class.java)
            val bundle = Bundle()
            bundle.putString("作者", ancientName)
            intent.putExtras(bundle)
            startActivity(intent)
        }
        name_ancient.text = ancientName
        introduce_ancient.text = "\t\t\t\t" + ancientIntroduce

    }
}