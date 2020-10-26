package com.example.test.data

data class PoetryDetailsData(
    val code: Int,
    val `data`: PoetryDetailsDataList,
    val msg: String
)

data class PoetryDetailsDataList(
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