package com.example.test.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.test.functions.Common

class MyDbHelper(val context: Context, name: String, version: Int) :
    SQLiteOpenHelper(context, name, null, version) {
    private val userTable = "create table User (" +
            " id integer primary key autoincrement," +
            "is_login integer," +
            "username text," +
            "gollum text," +
            "current text," +
            "pwd text)"

    override fun onCreate(db: SQLiteDatabase) {
        Common.myToast(context, "建表成功了！")
        db.execSQL(userTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}