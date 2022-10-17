package com.panda.pda.mes.operation.fms.data.model

import com.panda.pda.mes.common.ModelProperty

/**
 * created by AnJiwei 2021/8/24
 */
data class DispatchOrderModel(
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
    @ModelProperty(13, "派工单状态", dataParameterType = "TASK_STATUS")
    val dispatchOrderStatus: Int,
    val batchNo: String,
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
    val productMode: String,

    val receiveCode: String,
    val receiveName: String,
    val remark: String,
    val reportNum: Int,
    val technicsCode: String,
    val technicsId: Int,
    @ModelProperty(14, "操作工")
    val jockeyName: String,
    val updateId: Int,
    val updateName: String,
    val updateTime: String,
    val workNo: String,
    val workOrderCode: String,
    val workOrderId: Int,
    val workOrderProcedureEquipmentId: Int,
    val workOrderProcedureId: Int,
    val workOrderReceiveId: Int,
    val workOrderReceiveName: String,
    val issueTime: String,
    val planEndTime: String,
    val planStartTime: String,
    val receiveTime: String?,
    val totalReportTime: Int?,
): IExchangeCardOperateItem {

    override fun getWorkOrder(): String {
        return workOrderCode
    }

    override fun filterExchangeCardCode(code: String, isWorkOrder: Boolean): Boolean {
        return if (isWorkOrder) {
            workOrderCode == code
        } else {
            dispatchOrderCode == code
        }
    }
}