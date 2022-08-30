package com.panda.pda.mes.operation.bps.data.model

import com.panda.pda.mes.common.ModelProperty
import com.panda.pda.mes.common.data.CommonParameters
import com.panda.pda.mes.common.data.DataParamType

/**
 * created by AnJiwei 2022/8/8
 */
data class MainPlanDetailModel(
    val createId: Int,
    val customerId: Int,
    val id: Int,
    @ModelProperty(1, "主计划编号")
    val planNo: String,
    @ModelProperty(2, "产品编码")
    val productCode: String,
    val productId: Int,
    @ModelProperty(3, "产品名称")
    val productName: String,
    @ModelProperty(4, "型号代号")
    val productModel: String,
    @ModelProperty(5, "主计划状态", dataParameterType = "BPS_PLAN_STATUS")
    val planStatus: Int,
    @ModelProperty(11, "主计划数量")
    val planNumber: Int,
    @ModelProperty(12, "工作令号")
    val workNo: String,
    @ModelProperty(13, "交货时间")
    val deliveryTime: String,
    @ModelProperty(14, "客户")
    val customerName: String,
    @ModelProperty(15, "完成时间")
    val finishTime: String,
    @ModelProperty(21, "创建人")
    val createName: String,
    @ModelProperty(22, "创建时间")
    val createTime: String,
    val remark: String,
    val updateId: Int,
    @ModelProperty(23, "更新人")
    val updateName: String,
    @ModelProperty(24, "更新时间")
    val updateTime: String,
    val workOrderCode: String
)