package com.example.gulupoetry.data

data class PoetryWithFirstData(
    val code: Int,
    val `data`: List<PoetryWithFirstList>,
    val msg: String
)

data class PoetryWithFirstList(
    val author: String,
    val dynasty: String,
    val firstText: String,
    val no: Int,
    val title: String
)