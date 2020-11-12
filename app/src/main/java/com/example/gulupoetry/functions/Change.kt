package com.example.gulupoetry.functions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

class Change {
    fun bitmapToString(bitmap: Bitmap, bitmapQuality: Int): String {
        // 将Bitmap转换成字符串
        var string: String? = null
        val bStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, bitmapQuality, bStream)
        val bytes = bStream.toByteArray()
        string = Base64.encodeToString(bytes, Base64.DEFAULT)
        return string
    }

    fun stringToBitmap(string: String): Bitmap? {
        // 将字符串转换成Bitmap类型
        var bitmap: Bitmap? = null
        try {
            val bitmapArray: ByteArray = Base64.decode(string, Base64.DEFAULT)
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.size)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return bitmap
    }
}