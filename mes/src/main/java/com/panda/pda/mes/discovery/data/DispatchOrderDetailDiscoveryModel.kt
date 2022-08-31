package com.panda.pda.mes.discovery.data

import com.panda.pda.mes.common.ModelProperty

/**
 * created by AnJiwei 2022/8/31
 */
data class DispatchOrderDetailDiscoveryModel(
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
    @ModelProperty(11, "生产工艺")
    val technicsName: String,
    @ModelProperty(12, "工艺版本")
    val technicsVersion: String,
    @ModelProperty(13, "工单编号")
    val workOrderCode: String,
    @ModelProperty(14, "工作令号")
    val workNo: String,
    @ModelProperty(15, "批次号")
    val batchNo: String,
    @ModelProperty(20, "派工单状态", dataParameterType = "TASK_STATUS")
    val dispatchOrderStatus: Int,
    @ModelProperty(21, "计划开始时间")
    val planStartTime: String,
    @ModelProperty(22, "计划完成时间")
    val planEndTime: String,
    val createId: Int,
    val createName: String,
    val createTime: String,
    val id: Int,
    val issueName: String,
    val issueNo: String,
    val jockeyNo: String,
    val pauseStatus: Int,
    val planId: Int,
    val productId: Int,
    @ModelProperty(23, "生产负责人")
    val receiveName: String,
    @ModelProperty(24, "生产方式", dataParameterType = "PRODUCT_MODE")
    val productMode: Int,
    val receiveCode: String,
    val remark: String,
    val reportNum: Int,
    val technicsCode: String,
    val technicsId: Int,
    @ModelProperty(25, "操作工")
    val jockeyName: String,
    val updateId: Int,
    val updateName: String,
    val updateTime: String,
    val workOrderId: Int,
    val workOrderProcedureEquipmentId: Int,
    val workOrderProcedureId: Int,
    val workOrderReceiveId: Int,
    val workOrderReceiveName: String,
    val issueTime: String,
    val receiveTime: String?,
    val totalReportTime: Int?,
)
