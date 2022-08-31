package com.panda.pda.mes.discovery.data

/**
 * created by AnJiwei 2021/9/1
 */
data class TaskReportModel(
    val createName: String,
    val createTime: String,
    val id: Int,
    val productCode: String,
    val productName: String,
    val productModel: String,
    val reportNumber: Int,
    val dispatchOrderCode: String?,
    val dispatchOrderDesc: String?,
    val taskDesc: String
)