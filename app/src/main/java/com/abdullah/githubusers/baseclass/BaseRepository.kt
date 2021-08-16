package com.abdullah.githubusers.baseclass

import android.content.Context
import java.nio.charset.Charset

open class BaseRepository(private val context: Context) {
    fun assetsFileToJson(filePath: String): String {
        return try {
            val inputStream = context.assets.open(filePath)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charset.forName("UTF-8"))
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}