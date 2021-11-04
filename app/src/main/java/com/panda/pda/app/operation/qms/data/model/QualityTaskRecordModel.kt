package com.panda.pda.app.operation.qms.data.model

/**
 * created by AnJiwei 2021/9/24
 */
data class QualityTaskRecordModel(
    val createTime: String,
    val id: Int,
    val operateDetail: String,
    val operateName: String,
    val operateType: String,
    val remark: String
)