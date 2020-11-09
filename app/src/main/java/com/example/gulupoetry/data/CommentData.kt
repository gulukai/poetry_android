package com.example.gulupoetry.data

data class CommentData(
    val code: Int,
    val count: Int,
    val `data`: List<CommentDataList>,
    val message: String
)

data class CommentDataList(
    val head: String,
    val nickname: String,
    val text: String,
    val time: String
)