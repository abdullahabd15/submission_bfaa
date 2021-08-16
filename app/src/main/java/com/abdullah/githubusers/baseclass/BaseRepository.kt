package com.abdullah.githubusers.baseclass

import android.content.Context
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

open class BaseRepository(private val context: Context) {
    fun assetsFileToJson(filePath: String): String {
        return try {
            val inputStream = context.assets.open(filePath)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charset.forName(StandardCharsets.UTF_8.name()))
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}