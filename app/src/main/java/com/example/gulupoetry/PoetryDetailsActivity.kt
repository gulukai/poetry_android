package com.example.gulupoetry

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import cn.sharesdk.onekeyshare.OnekeyShare
import cn.sharesdk.tencent.qq.QQ
import com.example.gulupoetry.base.BaseActivity
import com.example.gulupoetry.base.User
import com.example.gulupoetry.data.CommentData
import com.example.gulupoetry.data.PoetryDetailsData
import com.example.gulupoetry.db.MyDbHelper
import com.example.gulupoetry.functions.Common
import com.example.gulupoetry.mydialog.LoginDialog
import com.example.gulupoetry.mydialog.TheDialog
import com.example.gulupoetry.netWork.CommonTask
import com.example.gulupoetry.poetrydetailsfragment.*
import com.google.gson.Gson
import com.iflytek.cloud.SpeechConstant
import com.iflytek.cloud.SpeechUtility
import com.mob.MobSDK
import com.tencent.qcloudtts.LongTextTTS.LongTextTtsController
import com.tencent.qcloudtts.callback.QCloudPlayerCallback
import com.tencent.qcloudtts.callback.TtsExceptionHandler
import com.tencent.qcloudtts.exception.TtsException
import kotlinx.android.synthetic.main.activity_poetry_details.*
import okhttp3.*
import java.io.IOException


class PoetryDetailsActivity : BaseActivity() {
    private val myHelper = MyDbHelper(this, "User.db", 1)
    private var handle = Handler()
    private val mTtsController = LongTextTtsController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poetry_details)

        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5fb1d551")

        val db = myHelper.writableDatabase
        val poetryId = intent.getIntExtra("poetryId", 1)
        getCommentCount(poetryId)

        handle = Handler {
            when (it.arg1) {
                200 -> {
                    comment_poetry_details_activity.setStyle { image, text ->
                        text.text = "这首诗有${it.arg2}条评论"
                        image.visibility = View.GONE
                    }
                }
                202 -> {
                    comment_poetry_details_activity.setStyle { image, text ->
                        text.text = "这首诗有${it.arg2}条评论"
                        image.visibility = View.GONE
                    }
                }
            }
            return@Handler true
        }
        comment_poetry_details_activity.setOnClickListener {
            val intent = Intent(this, CommentActivityList::class.java)
            intent.putExtra("poetryId", poetryId)
            startActivity(intent)
        }
        val commTask = CommonTask()
        commTask.url = "http://www.gulukai.cn/poetry/getpoetry/?p1=getpoetrybyid&p2=$poetryId"
        commTask.setCallback { it ->
            val info = Gson().fromJson(it, PoetryDetailsData::class.java)
            val no = info.data.no
            val annotation: String = info.data.annotation
            val appreciation: String = info.data.appreciation
            val author: String = info.data.author
            val dynasty: String = info.data.dynasty
            val text: String = info.data.text
            val title: String = info.data.title
            val translation: String = info.data.translation
            val tag: List<String> = info.data.tag
            action_bar_poetry_details.setStyle { back, txt, hear, collection, share ->
                val cursor = db.query(
                    "PoetryCollection",
                    null,
                    "poetry_no = ?",
                    arrayOf("$no"),
                    null,
                    null,
                    null,
                    null
                )
                if (cursor.moveToNext()) {
                    collection.isChecked = true
                    collection.setBackgroundResource(R.drawable.ic_collection_selected)
                } else {
                    collection.setBackgroundResource(R.drawable.ic_collection)
                }
                cursor.close()
                back.setOnClickListener {
                    finish()
                }
                txt.text = "古诗详情"
                hear.setBackgroundResource(R.drawable.headset)
                share.setBackgroundResource(R.drawable.share)
                share.visibility = View.GONE
                val str = "$title,$dynasty,$author,$text"


                hear.setOnClickListener {
                    mTtsController.init(
                        this,
                        1303862972,
                        "AKIDd4HEl3r0d0DA3i4t6NRHsPhKbNuTFiPs",
                        "f7KJn21mKdHivvgtra6cv73lDRQbfV6j"
                    )
                    //设置语速
                    mTtsController.setVoiceSpeed(0)
                    //设置音色
                    mTtsController.setVoiceType(0)
                    //设置音量
                    mTtsController.setVoiceVolume(0)
                    //设置语言
                    mTtsController.setVoiceLanguage(1)
                    //设置ProjectId
                    mTtsController.setProjectId(0)
                    //接收接口异常
                    val mTtsExceptionHandler: TtsExceptionHandler =
                        TtsExceptionHandler { e ->
                            Log.i("Tag", "tts onRequestException")
                            Toast.makeText(this@PoetryDetailsActivity, e.errMsg, Toast.LENGTH_SHORT)
                                .show()
                        }

                    mTtsController.startTts(
                        str,
                        mTtsExceptionHandler,
                        object : QCloudPlayerCallback {
                            //播放开始
                            override fun onTTSPlayStart() {
                            }
                            //音频缓冲中
                            override fun onTTSPlayWait() {
                            }
                            //缓冲完成，继续播放
                            override fun onTTSPlayResume() {
                            }
                            //连续播放下一句
                            override fun onTTSPlayNext() {
                            }
                            //播放中止
                            override fun onTTSPlayStop() {
                            }
                            //播放结束
                            override fun onTTSPlayEnd() {
                            }
                            override fun onTTSPlayProgress(p0: String?, p1: Int) {
                            }
                        })

                }

                collection.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (User.user_no == 0L) {
                        val loginDialog = LoginDialog(this)
                        loginDialog.setCancelable(true)
                        loginDialog.show()
                        loginDialog.setStyle { _, cancel, release ->
                            cancel.setOnClickListener {
                                loginDialog.dismiss()
                            }
                            release.setOnClickListener {
                                val intent = Intent(this, LoginActivity::class.java)
                                intent.putExtra("message", "请登录")
                                startActivity(intent)
                                loginDialog.dismiss()
                            }
                        }
                    } else {
                        if (isChecked) {
                            val values = ContentValues().apply {
                                put("gollum", User.user_no.toString())
                                put("poetry_no", no)
                            }
                            val row =
                                db.update(
                                    "PoetryCollection",
                                    values,
                                    "poetry_no = ?",
                                    arrayOf("$no")
                                )
                            if (row != 1) {
                                db.insert("PoetryCollection", null, values)
                            }
                            buttonView.setBackgroundResource(R.drawable.ic_collection_selected)
                            Common.myToast(this, "收藏古诗成功。")

                        } else {
                            db.delete("PoetryCollection", "poetry_no = ?", arrayOf("$no"))
                            buttonView.setBackgroundResource(R.drawable.ic_collection)
                            Common.myToast(this, "取消收藏。")
                        }
                    }
                }
            }
            text_radio_button.isChecked = true
            changeTabInDetails(TextFragment(text, author, dynasty, title, tag))
            annotation_radio_button.setOnClickListener {
                changeTabInDetails(AnnotationFragment(title, annotation))
            }
            appreciation_radio_button.setOnClickListener {
                changeTabInDetails(AppreciationFragment(title, appreciation))
            }
            author_radio_button.setOnClickListener {
                changeTabInDetails(AuthorIntroductionFragment(author))
            }
            text_radio_button.setOnClickListener {
                changeTabInDetails(TextFragment(text, author, dynasty, title, tag))
            }
            translation_radio_button.setOnClickListener {
                changeTabInDetails(TranslationFragment(title, translation))
            }
        }
        commTask.execute()
    }

    private fun getCommentCount(poetryId: Int) {
        try {
            val params = mapOf<String, String>(
                "method" to "get",
                "poetry_id" to "$poetryId"
            )
            val body = Common().buildParams(params)
            val client = OkHttpClient()
            val request =
                Request.Builder().url("http://www.gulukai.cn/comment/postcomment/").post(body)
                    .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {
                    val responseData = response.body?.string()
                    if (responseData != null) {
                        Log.i("Tag", responseData)
                        val info = Gson().fromJson(responseData, CommentData::class.java)
                        if (info.code == 202) {
                            val message = Message()
                            message.arg1 = 202
                            message.arg2 = info.count
                            handle.sendMessage(message)
                        } else {
                            val message = Message()
                            message.arg1 = 200
                            message.arg2 = info.count
                            handle.sendMessage(message)
                        }
                    }
                }

            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun changeTabInDetails(fragment: Fragment, container: Int = R.id.frame_poetry_details) {
        supportFragmentManager.beginTransaction().replace(container, fragment)
            .commitAllowingStateLoss()
    }

    override fun onPause() {
        mTtsController.stop()
        super.onPause()
    }

    override fun onDestroy() {
        mTtsController.stop()
        super.onDestroy()
    }
}