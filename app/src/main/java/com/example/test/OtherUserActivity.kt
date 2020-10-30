package com.example.test

import android.os.Bundle
import android.view.View
import com.example.test.base.BaseActivity
import com.example.test.functions.Common
import kotlinx.android.synthetic.main.activity_other_user.*

class OtherUserActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_user)

        action_other_user.setStyle { back, txt, hear, collection, share ->
            back.setOnClickListener {
                finish()
            }
            txt.text = "Ta的信息"
            hear.visibility = View.GONE
            collection.visibility = View.GONE
            share.visibility = View.GONE
        }

        collection_other_user.setStyle { image, text ->
            image.setBackgroundResource(R.drawable.collection)
            text.text = "TA收藏的诗集"
        }

        follow_toggle_activity.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                buttonView.setBackgroundResource(R.drawable.followed)
                Common.myToast(this, "关注成功！")
            } else {
                buttonView.setBackgroundResource(R.drawable.follow)
                Common.myToast(this, "取消关注！")
            }
        }
    }
}