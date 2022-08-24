package com.panda.pda.mes.operation.exchange_card.data.model

/**
 * created by AnJiwei 2022/8/24
 */
data class DispatchOrderModel(
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
    val receiveName: String,
    val workNo: String,
    var issueTime: String?,
    var prepareFinishTime: String?,
    val receiveTime: String?,
    val totalReportTime: Int?, //总工时min
    val workOrderCode: String
)
