package com.example.gulupoetry.data

data class AuthorData(
    val code: Int,
    val `data`: AuthorDataList,
    val msg: String
)

data class AuthorDataList(
    val authors: List<String>
)