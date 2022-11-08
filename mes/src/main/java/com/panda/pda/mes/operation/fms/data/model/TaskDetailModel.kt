package com.panda.pda.mes.operation.fms.data.model

/**
 * created by AnJiwei 2021/8/26
 */
data class TaskDetailModel(
    val batchNo: String,
    val equipmentDesc: String,
    val id: Int,
    val jockeyName: String,
    val planCode: String,
    val planEndTime: String,
    val planStartTime: String,
    val productCode: String,
    val productName: String,
    val realEndTime: String,
    val realStartTime: String,
    val dispatchOrderCode: String,
    val dispatchOrderDesc: String,
    val dispatchOrderNum: Int,
    val dispatchOrderStatus: Int,
    val workNo: String,
    val reportNum: Int,
    val receiveName: String?,
    val workOrderCode: String,
    val productMode: Int,
    val originalDispatchOrderCode: String,
    val reportExcessRate: Double,
    val includeDispatchOrderList: List<IncludeDispatchOrder>?,
    val selfInspection: Int,
    val specialInspection: Int,
    val repairFlag: Int
)

data class IncludeDispatchOrder(
    val dispatchOrderCode: String,
    val dispatchOrderDesc: String,
    val id: String
)