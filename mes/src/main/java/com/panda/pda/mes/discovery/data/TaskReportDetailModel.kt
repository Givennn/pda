package com.panda.pda.mes.discovery.data

import com.panda.pda.mes.common.ModelProperty
import com.panda.pda.mes.common.data.model.FileInfoModel

/**
 * created by AnJiwei 2021/9/1
 */
data class TaskReportDetailModel(
    val fileList: List<FileInfoModel>,
    val id: Int,
    @ModelProperty(1, "主计划编号")
    val planNo: String,
    @ModelProperty(2, "工单编号")
    val workOrderCode: String,
    @ModelProperty(5, "工作令号")
    val workNo: String,
    @ModelProperty(7, "批次号")
    val batchNo: String,
    @ModelProperty(8, "派工单编号")
    val dispatchOrderCode: String,
    @ModelProperty(9, "派工单描述")
    val dispatchOrderDesc: String,
    @ModelProperty(10, "产品编号")
    val productCode: String,
    @ModelProperty(11, "产品描述")
    val productName: String,
    @ModelProperty(12, "型号代号")
    val productModel: String,
    @ModelProperty(13, "派工单数量")
    val dispatchOrderNum: Int,
    @ModelProperty(14, "报工数量")
    val reportNumber: Int,
    @ModelProperty(15, "操作工")
    val createName: String,
    val reportTime: Int,
    val remark: String,

)