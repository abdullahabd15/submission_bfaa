package com.abdullah.githubusers.utils

import android.content.Context
import com.abdullah.githubusers.R
import com.abdullah.githubusers.const.Const

object StringUtil {
    fun formatWithNumericUnit(context: Context?, count: Int?): String {
        if (count == null) return "${Const.VALUE_0}"
        val startIndex = 0
        var strCount = count.toString()
        return when {
            count <= Const.MAX_VALUE_BELOW_1K -> "$count"
            count <= Const.MAX_VALUE_BELOW_1M -> {
                val endIndexToCut = strCount.length - 3
                strCount = strCount.substring(startIndex, endIndexToCut)
                "${strCount}${context?.getString(R.string.kilo)}"
            }
            count >= Const.VALUE_1M -> {
                val endIndexToCut = strCount.length - 6
                strCount = strCount.substring(startIndex, endIndexToCut)
                "${strCount}${context?.getString(R.string.million)}"
            }
            else -> "$count"
        }
    }
}