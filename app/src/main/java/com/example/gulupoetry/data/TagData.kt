package com.example.gulupoetry.data

data class TagData(
    val code: Int,
    val `data`: TagDataList,
    val msg: String
)

data class TagDataList(
    val tags: List<String>
)