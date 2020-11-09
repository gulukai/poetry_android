package com.example.gulupoetry.data

data class GetAllCommentList(
    val code: Int,
    val count: Int,
    val `data`: List<AllCommentList>,
    val message: String
)

data class AllCommentList(
    val gollum: Int,
    val head: String,
    val nickname: String,
    val poetry_id: Int,
    val poetry_name: String,
    val text: String,
    val time: String
)