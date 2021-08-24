package com.panda.pda.app.base.retrofit

/**
 * created by AnJiwei 2020/10/15
 */
data class BaseResponse<T> (
    val message: String?,
    var data: T?,
    var code: Int
)


data class DataListNode<T>(
    val dataList: List<T>
)