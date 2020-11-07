package com.example.test.data

data class SentenceData(
    val code: Int,
    val `data`: List<SentenceDataList>,
    val msg: String
)

data class SentenceDataList(
    val poetry_id: Int,
    val sentence: String,
    val source: String,
    val tag: String
)