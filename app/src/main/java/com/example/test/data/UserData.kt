package com.example.test.data

/*
   " id integer primary key autoincrement," +
                   "is_login integer," +
                   "username text," +
                   "gollum text," +
                   "pwd text)"
   * */
data class UserData(
    var isLogin: Int,
    var username: String,
    var gollum: String,
    var pwd: String,
    var current: Long
)