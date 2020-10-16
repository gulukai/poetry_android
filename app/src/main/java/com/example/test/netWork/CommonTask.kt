package com.example.test.netWork

import android.os.AsyncTask
import android.view.View
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class CommonTask : AsyncTask<String, Int, String>() {

    var progressBar: View? = null
    var url: String? = null
    private var json: String? = null
    private var mCallback: ((String) -> Unit)? = null

    fun setCallback(mCallbackHandle: (String) -> Unit) {

        mCallback = mCallbackHandle
    }

    //后台任务执行前调用  读数据的时候显示progressBar
    override fun onPreExecute() {
        super.onPreExecute()
        progressBar?.visibility = View.VISIBLE
    }

    //运行在后台（子线程）
    override fun doInBackground(vararg params: String?): String? {
        val okHttpClient = OkHttpClient()
        val request = Request.Builder().get().url(url!!).build()
        val call = okHttpClient.newCall(request)
        try {
            val response = call.execute()
            if (response.isSuccessful) {
                json = response.body!!.string()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return json
    }

    //执行后调用
    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (result != null) {
            try {
                mCallback?.invoke(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        progressBar?.visibility = View.GONE
    }

}