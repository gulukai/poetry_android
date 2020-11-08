package com.example.test.data

data class TheAncientsData(
    val code: Int,
    val `data`: List<TheAncientsDataList>,
    val msg: String
)

data class TheAncientsDataList(
    val ad_introduce: String,
    val ad_name: String,
    val ad_no: Int
)