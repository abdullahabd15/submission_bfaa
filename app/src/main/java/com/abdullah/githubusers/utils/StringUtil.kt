package com.abdullah.githubusers.utils

import android.content.Context
import com.abdullah.githubusers.R

object StringUtil {
    fun formatWithNumericUnit(context: Context?, count: Int?): String {
        if (count == null) return "0"
        when {
            count <= 999 -> return "$count"
            count <= 999999 -> {
                var strCount = count.toString()
                strCount = strCount.substring(0, (strCount.length - 3))
                return "${strCount}${context?.getString(R.string.kilo)}"
            }
            count <= 999999999 -> {
                var strCount = count.toString()
                strCount = strCount.substring(0, (strCount.length - 6))
                return "${strCount}${context?.getString(R.string.million)}"
            }
            count <= 999999999999 -> {
                var strCount = count.toString()
                strCount = strCount.substring(0, (strCount.length - 9))
                return "${strCount}${context?.getString(R.string.trillion)}"
            }
            else -> return "$count"
        }
    }
}