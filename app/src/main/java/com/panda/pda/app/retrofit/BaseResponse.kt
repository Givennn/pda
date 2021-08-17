package com.panda.pda.app.retrofit

/**
 * created by AnJiwei 2020/10/15
 */
data class BaseResponse<T> (
    val message: String?,
    var data: T?,
    var code: Int
)
