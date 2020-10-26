package com.example.test.data

data class AuthorIntroductionData(
    val code: Int,
    val `data`: AuthorIntroduction,
    val msg: String
)

data class AuthorIntroduction(
    val author_introduction: String,
    val author_name: String,
    val no: Int
)