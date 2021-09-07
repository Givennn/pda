package com.panda.pda.app.common.data.model

/**
 * created by AnJiwei 2021/9/6
 */
data class DataParamModel(
    val id: Int,
    val paramDesc: String,
    val paramName: String,
    val paramValue: String,
    val parentId: Int
)