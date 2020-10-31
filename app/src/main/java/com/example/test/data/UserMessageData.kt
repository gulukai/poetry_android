package com.example.test.data

data class UserMessageData(
    val code: Int,
    val `data`: UserMessageDataList,
    val msg: String
)

data class UserMessageDataList(
    val background: String,
    val gender: Boolean,
    val head: String,
    val nickname: String
)