package com.panda.pda.mes.user.data.model

/**
 * created by AnJiwei 2022/9/2
 */
data class SystemLogModel(
    val createId: Int,
    val createName: String,
    val createTime: String,
    val functionId: Int,
    val id: Int,
    val moduleId: Int,
    val operateDetail: String,
    val operationFunction: String,
    val remark: String,
    val source: Int,
    val updateId: Int,
    val updateName: String,
    val updateTime: String
)