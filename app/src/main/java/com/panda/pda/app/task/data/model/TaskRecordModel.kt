package com.panda.pda.app.task.data.model

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