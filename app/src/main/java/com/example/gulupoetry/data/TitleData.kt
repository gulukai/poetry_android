package com.example.gulupoetry.data

data class TitleData(
    val code: Int,
    val `data`: TitleDataList,
    val msg: String
)

data class TitleDataList(
    val titles: List<String>
)