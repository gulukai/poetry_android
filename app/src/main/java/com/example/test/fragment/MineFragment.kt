package com.example.test.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.test.LoginActivity
import com.example.test.PoetryCollectionActivity
import com.example.test.R
import com.example.test.UserMessageManageActivity
import com.example.test.base.User
import com.example.test.data.FansAndFollow
import com.example.test.data.UserMessageData
import com.example.test.functions.Change
import com.example.test.functions.Common
import com.example.test.mydialog.TheDialog
import com.google.gson.Gson
import kotlinx.android.synthetic.main.my_fragment_layout.*
import okhttp3.*
import java.io.File
import java.io.IOException


class MineFragment(private val gollum: Long) : BaseFragment(R.layout.my_fragment_layout),
    View.OnClickListener {
    private val takePhoto = 1
    private val fromAlbum = 2
    lateinit var imageUri: Uri
    lateinit var outputImage: File
    private var handler = Handler()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        head_portrait_my_fragment.setOnClickListener {
            object : TheDialog(this.requireActivity()) {
                override fun btnPickBySelect() {
                    // 打开文件选择器
                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                    intent.addCategory(Intent.CATEGORY_OPENABLE)
                    // 指定只显示照片
                    intent.type = "image/*"
                    startActivityForResult(intent, fromAlbum)
                }

                override fun btnPickByTake() {
                    //创建File对象，用于存储拍照后的图片  externalCacheDir,
                    outputImage = File(this.context.externalCacheDir, "output_image.jpg")
                    if (outputImage.exists()) {
                        outputImage.delete()
                    }
                    outputImage.createNewFile()
                    imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        FileProvider.getUriForFile(
                            this.context,
                            "com.example.test.fileprovider",
                            outputImage
                        )
                    } else {
                        Uri.fromFile(outputImage)
                    }
                    //启动相机程序
                    val intent = Intent("android.media.action.IMAGE_CAPTURE")
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                    startActivityForResult(intent, takePhoto)
                }
            }.show()
        }
        val params = mapOf<String, String>(
            "gollum" to gollum.toString()
        )

        getUserMessage(params, 4, "http://www.gulukai.cn/user/getuser/")


        val params2 = mapOf(
            "type" to "return",
            "follow" to gollum.toString(),
            "fans" to ""
        )

        getUserMessage(params2, 3, "http://www.gulukai.cn/follow/postfollow/")

        handler = Handler {
            when (it.what) {
                3 -> {
                    val str = it.obj.toString()
                    val info = Gson().fromJson(str, FansAndFollow::class.java)
                    fans_my_fragment.text = info.fanscount.toString()
                    follow_my_fragment.text = info.followscount.toString()
                }
                4 -> {
                    val str = it.obj.toString()
                    val info = Gson().fromJson(str, UserMessageData::class.java)
                    val head = "http://www.gulukai.cn${info.data.head}"
                    val back = "http://www.gulukai.cn${info.data.background}"
//                    User.user_head = head
                    user_name_my_fragment.text = info.data.nickname
                    if (info.data.gender) {
                        gender_my_fragment.setImageResource(R.drawable.man)

                    } else {
                        gender_my_fragment.setImageResource(R.drawable.woman)
                    }
                    Glide.with(this)
                        .load(head)
                        .apply(
                            RequestOptions()
                                .dontAnimate()
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                        )
                        .into(head_portrait_my_fragment)
                    Glide.with(this)
                        .load(back)
                        .apply(
                            RequestOptions()
                                .dontAnimate()
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                        )
                        .into(background_image_my_fragment)
                }
            }
            return@Handler true
        }
        collection_my_fragment.setStyle { image, text ->
            image.setImageResource(R.drawable.collection)
            text.text = "我的收藏"
        }
        security_my_fragment.setStyle { image, text ->
            image.setImageResource(R.drawable.security)
            text.text = "账号管理"
        }
        system_my_fragment.setStyle { image, text ->
            image.setImageResource(R.drawable.system)
            text.text = "系统设置"
        }
        about_us_my_fragment.setStyle { image, text ->
            image.setImageResource(R.drawable.about)
            text.text = "关于我们"
        }

        collection_my_fragment.setOnClickListener {
            Common().goActivity(this.context!!, PoetryCollectionActivity::class.java)
        }

        security_my_fragment.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            takePhoto -> {
                if (resultCode == Activity.RESULT_OK) {
                    //将拍摄的照片显示出来
                    val bitmap =
                        BitmapFactory.decodeStream(
                            this.context!!.contentResolver.openInputStream(
                                imageUri
                            )
                        )
                    val str = Change().bitmapToString(bitmap, 100)
                    postPhoto(str)
                    head_portrait_my_fragment.setImageBitmap(bitmap)
                }
            }
            fromAlbum -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    data.data?.let { uri ->
                        // 将选择的照片显示
                        val bitmap = getBitmapFromUri(uri)
                        val str = Change().bitmapToString(bitmap!!, 100)
                        postPhoto(str)
                        head_portrait_my_fragment.setImageBitmap(bitmap)
                    }
                }
            }
        }

    }

    private fun getBitmapFromUri(uri: Uri) = this.context!!.contentResolver.openFileDescriptor(
        uri,
        "r"
    )?.use {
        BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
    }

    private fun postPhoto(str: String) {
        try {
            val params = mapOf<String, String>(
                "photo" to str,
                "gollum" to User.user_no.toString()
            )
            val body = Common().buildParams(params)
            val client = OkHttpClient()
            val request =
                Request.Builder().url("http://www.gulukai.cn/user/update/").post(body)
                    .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {
                    val responseData = response.body?.string()
                    if (responseData != null) {
                        Log.i("Tag", responseData)
                    }
                }

            })
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun getUserMessage(params: Map<String, String>, num: Int, url: String) {
        try {
            val body = Common().buildParams(params)
            val client = OkHttpClient()
            val request =
                Request.Builder().url(url)
                    .post(body)
                    .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {
                    val responseData = response.body?.string()
                    if (responseData != null) {
                        val message = Message()
                        message.what = num
                        message.obj = responseData
                        handler.sendMessage(message)
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            security_my_fragment -> {
                Common().goActivity(this.context!!, UserMessageManageActivity::class.java)
            }
        }
    }
}




