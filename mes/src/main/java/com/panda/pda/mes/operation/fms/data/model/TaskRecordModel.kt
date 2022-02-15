package com.panda.pda.mes.operation.fms.data.model

/**
 * created by AnJiwei 2021/8/26
 */
data class TaskRecordModel(
    val createTime: String,
    val id: Int,
    val operateDetail: String,
    val operateName: String,
    val operateType: String
)