package com.example.gulupoetry.data

data class VoiceData(
    val `data`: VoiceDataList,
    val msg: String,
    val ret: Int
)

data class VoiceDataList(
    val format: Int,
    val md5sum: String,
    val speech: String
)