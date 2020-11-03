package com.example.test.data

data class GetUserAllMessage(
    val code: Int,
    val `data`: GetUserAllMessageData,
    val msg: String
)

data class GetUserAllMessageData(
    val birthday: String,
    val email: String,
    val gender: Boolean,
    val nickname: String,
    val tel: String
)