package com.example.test.data

data class FansAndFollow(
    val code: Int,
    val fans: List<Fan>,
    val fanscount: Int,
    val follow: List<Follow>,
    val followscount: Int,
    val message: String
)

data class Fan(
    val fans_id: String
)

data class Follow(
    val follow_id: String
)