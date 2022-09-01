package com.panda.pda.mes.discovery.data

import com.panda.pda.mes.common.ModelProperty

/**
 * created by AnJiwei 2022/8/31
 */
data class WorkOrderDetailDiscoveryModel(
    @ModelProperty(1, "工单编号")
    val workOrderCode: String,
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
    @ModelProperty(13, "主计划编号")
    val planNo: String,
    @ModelProperty(14, "工作令号")
    val workNo: String,
    @ModelProperty(15, "批次号")
    val batchNo: String,
    @ModelProperty(16, "工单数量")
    val workOrderNum: Double,
    @ModelProperty(17, "工单状态", dataParameterType = "BPS_WORK_ORDER_STATUS")
    val status: Int,
    @ModelProperty(18, "生产负责人")
    val receiveName: String,
    @ModelProperty(20, "要求完成时间")
    val deadline: String,
    @ModelProperty(21, "计划开始时间")
    val planStartTime: String,
    @ModelProperty(22, "计划完成时间")
    val planEndTime: String,
    @ModelProperty(23, "实际开始时间")
    val realStartTime: String,
    @ModelProperty(24, "实际完成时间")
    val realEndTime: String,
    val batchNum: Double,
    val batchUsedTime: Int,
    val capacity: Double,
    val closeReason: String,
    val createId: Int,
    val createName: String,
    val createTime: String,
    val firstWorkTime: String,
    val handId: Int,
    val handName: String,
    val handTime: String,
    val id: Int,
    val planId: Int,
    val productId: Int,
    val receiveCode: String,
    val receiveId: Int,
    val receiveTime: String,
    val remark: String,
    val reportNum: Int,
    val stopAgoStatus: Int,
    val technicsCode: String,
    val technicsId: Int,
    val updateId: Int,
    val updateName: String,
    val updateTime: String
)