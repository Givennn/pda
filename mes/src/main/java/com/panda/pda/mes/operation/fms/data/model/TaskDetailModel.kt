package com.panda.pda.mes.operation.fms.data.model

import com.panda.pda.mes.common.ModelProperty

/**
 * created by AnJiwei 2021/8/26
 */
data class TaskDetailModel(

    @ModelProperty(1, "派工单编号")
    val dispatchOrderCode: String,
    @ModelProperty(2, "派工单描述")
    val dispatchOrderDesc: String,
    @ModelProperty(3, "派工单数量")
    val dispatchOrderNum: Int,
    @ModelProperty(4, "产品编码")
    val productCode: String,
    @ModelProperty(5, "产品描述")
    val productName: String,
    @ModelProperty(10, "型号代号")
    val productModel: String,
    @ModelProperty(13, "派工单状态", dataParameterType = "TASK_STATUS")
    val dispatchOrderStatus: Int,
    val batchNo: String,
    val equipmentDesc: String,
    val id: Int,
    val jockeyName: String,
    val planCode: String,
    val planEndTime: String,
    val planStartTime: String,
    val realEndTime: String,
    val realStartTime: String,
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
    val repairFlag: Int,
    val workCenterId: Int?,
    val remark: String,
    val inspectFlag:Int
) {
}

data class IncludeDispatchOrder(
    val dispatchOrderCode: String,
    val dispatchOrderDesc: String,
    val id: String
)