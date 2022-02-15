package com.panda.pda.mes.common

/**
 * created by AnJiwei 2021/9/16
 */
object DateUtils {
    fun getManHour(minute: Int): String {
        var result = ""
        val hours: Int = minute / 60
        val minutes: Int = minute % 60
        if (hours != 0) {
            result += "${hours}h"
        }
        if (minutes != 0) {
            result += "${minutes}min"
        }
        return if (result.isEmpty()) "0min" else result
    }
}