package com.example.gulupoetry.data

data class MyPoetryDetailsDataList(
    val code: Int,
    val `data`: List<Data>,
    val msg: String
)

data class Data(
    val `annotation`: String,
    val appreciation: String,
    val author: String,
    val dynasty: String,
    val no: Int,
    val tag: List<String>,
    val text: String,
    val title: String,
    val translation: String
)