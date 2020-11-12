package com.example.gulupoetry.functions

import android.content.Context
import java.io.*

class FileOP {
    fun save(cxt : Context, fileName : String, fileData : String ): Boolean {
        var saveFlag = false
        try {
            val output = cxt.openFileOutput(fileName,
                Context.MODE_PRIVATE
            )
            val writer = BufferedWriter(
                OutputStreamWriter(output)
            )
            writer.use {
                it.write(fileData)
            }
            saveFlag = true
        }catch (e : IOException){
            e.printStackTrace()
        }
        return saveFlag
    }
    fun read(cxt : Context, fileName : String): String {
        val str = StringBuilder()
        try {
            val input = cxt.openFileInput(fileName)
            val reader = BufferedReader(InputStreamReader(input))
            reader.use {
                reader.forEachLine {
                    str.append(it)
                }
            }
        }catch (e : IOException){
            e.printStackTrace()
        }
        return str.toString()
    }
}