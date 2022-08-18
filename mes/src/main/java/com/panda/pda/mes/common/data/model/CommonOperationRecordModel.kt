package com.panda.pda.mes.common.data.model

/**
 * created by AnJiwei 2022/8/8
 */
data class CommonOperationRecordModel(
    val createTime: String,
    val id: Int,
    val operateDetail: String,
    val operateName: String,
    val operateType: String,
    val remark: String,
)
