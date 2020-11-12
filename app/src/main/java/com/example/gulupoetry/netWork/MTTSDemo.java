package com.example.gulupoetry.netWork;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class MTTSDemo implements TextToSpeech.OnInitListener {
    private TextToSpeech mTTS;
    private Context mContext;

    public MTTSDemo(Context mContext) {
        this.mContext = mContext;
        //监听器就直接传入本类
        this.mTTS = new TextToSpeech(mContext, this);
    }

    /**
     * 初始化
     *
     * @param status
     */
    @Override
    public void onInit(int status) {
        //判断是否转化成功
        if (status == TextToSpeech.SUCCESS) {
            Log.i("Tag", "进来了init");
            //设置语言为中文
            int languageCode = mTTS.setLanguage(Locale.CHINA);
            //判断是否支持这种语言，Android原生不支持中文，使用科大讯飞的tts引擎就可以了
            Log.i("Tag", "languageCode:" + languageCode);
            if (languageCode == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.i("Tag", "onInit: 不支持这种语言，改成英文");
                mTTS.setLanguage(Locale.US);
            } else {
                //不支持就改成英文
                Log.i("Tag", "onInit: 看到这句话，忽略");
//                mTTS.setLanguage(Locale.US);
            }
        }
    }

    /**
     * 播报方法，需要传入播报的内容
     *
     * @param text 播报的内容
     */
    public void speak(String text) {
        //设置音调,值越大声音越尖（女生），值越小则变成男声,1.0是常规
        Log.i("Tag", "进来了！！");
        mTTS.setPitch(1.0f);
        //设置语速
        mTTS.setSpeechRate(1.0f);
        mTTS.speak(text, TextToSpeech.QUEUE_ADD, null, "");
    }

    /**
     * 销毁播报方法，直接调用
     */
    public void stopTTS() {
        mTTS.stop();
        mTTS.shutdown();
        mTTS = null;
    }

}


