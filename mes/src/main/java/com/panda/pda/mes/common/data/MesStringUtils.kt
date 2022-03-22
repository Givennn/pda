package com.panda.pda.mes.common.data

import android.text.TextUtils

/**
 * created by yinzl
 */
object MesStringUtils {
    /**
     * string 转int 可传默认值
     */
    fun stringToInt(str: String, default: Int): Int {
        if (str==null||TextUtils.isEmpty(str)) {
            return default
        }
        try {
            return str.toInt()
        } catch (e: Exception) {

        }
        return default
    }

    fun stringToInt(str: String): Int {
        if (str==null||TextUtils.isEmpty(str)) {
            return 0
        }
        return stringToInt(str, 0)
    }
}