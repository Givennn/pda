package com.panda.pda.mes.operation.fms.data.model

/**
 * created by AnJiwei 2021/8/24
 */
data class TaskModel(
    val batchNo: String,
    val id: Int,
    val issueName: String,
    val planCode: String,
    val planEndTime: String,
    val planStartTime: String,
    val productCode: String,
    val productName: String,
    val realStartTime: String,
    val reportNum: Int,
    val dispatchOrderCode: String,
    val dispatchOrderDesc: String,
    val dispatchOrderNum: Int,
    val dispatchOrderStatus: Int,
    val workNo: String,
    var issueTime: String?,
    var prepareFinishTime: String?,
    val receiveTime: String?,
    val totalReportTime: Int? //总工时min
)