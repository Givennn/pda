package com.panda.pda.mes.operation.exchange_card.data.model

import com.panda.pda.mes.common.ModelProperty

/**
 * created by AnJiwei 2022/8/24
 */
data class WorkOrderModel(
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
    val technicsCode: String,
    val batchNo: String,
    val batchNum: Double,
    val batchUsedTime: Int,
    val closeReason: String,
    val createId: Int,
    val createName: String,
    val createTime: String,
    val deadline: String,
    val firstWorkTime: String,
    val handId: Int,
    val handName: String,
    val id: Int,
    val planId: Int,
    val planNo: String,
    val productId: Int,
    val receiveCode: String,
    val receiveId: Int,
    @ModelProperty(15, "生产负责人")
    val receiveName: String,
    val remark: String,
    val reportNum: Int,
    @ModelProperty(14, "工单状态", dataParameterType = "BPS_WORK_ORDER_STATUS")
    val status: Int,
    val stopAgoStatus: Int,
    val technicsId: Int,
    val updateId: Int,
    val updateName: String,
    val updateTime: String,
    val workNo: String,
    @ModelProperty(13, "工单数量")
    val workOrderNum: Double
)