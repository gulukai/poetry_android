package com.example.test.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDbHelper(val context: Context, name: String, version: Int) :
    SQLiteOpenHelper(context, name, null, version) {
    private val userTable = "create table User (" +
            " id integer primary key autoincrement," +
            "is_login integer," +
            "username text," +
            "gollum text," +
            "current text," +
            "pwd text)"
    private val poetryCollectionTable = "create table PoetryCollection (" +
            " id integer primary key autoincrement," +
            "gollum text," +
            "poetry_no integer)"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(userTable)
        db.execSQL(poetryCollectionTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}