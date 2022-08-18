package com.panda.pda.mes.discovery.data

import com.panda.pda.mes.common.ModelProperty
import com.panda.pda.mes.common.data.model.FileInfoModel

/**
 * created by AnJiwei 2021/9/1
 */
data class TaskReportDetailModel(
    val fileList: List<FileInfoModel>,
    val id: Int,
    @ModelProperty(1, "订单编号")
    val planNo: String,
    @ModelProperty(2, "计划编号")
    val planCode: String,
    @ModelProperty(5, "工作令号")
    val workNo: String,
    @ModelProperty(7, "统一号")
    val batchNo: String,
    @ModelProperty(8, "任务编号")
    val dispatchOrderCode: String,
    @ModelProperty(9, "任务描述")
    val taskDesc: String,
    @ModelProperty(10, "产品编号")
    val productCode: String,
    @ModelProperty(11, "产品描述")
    val productName: String,
    @ModelProperty(12, "任务数量")
    val dispatchOrderNum: Int,
    @ModelProperty(13, "报工数量")
    val reportNumber: Int,
    val reportTime: Int,
    val remark: String,

)