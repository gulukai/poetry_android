package com.example.test.data

data class TitleData(
    val code: Int,
    val `data`: TitleDataList,
    val msg: String
)

data class TitleDataList(
    val titles: List<String>
)