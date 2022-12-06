package com.panda.pda.mes.operation.bps.data.model

import com.panda.pda.mes.common.ModelProperty
import com.panda.pda.mes.common.data.model.FileInfoModel

/**
 * created by AnJiwei 2022/11/2
 */
data class MainPlanReportHistoryModel(
    val fileList: List<FileInfoModel>,
    val id: Int,
    @ModelProperty(1, "主计划编号")
    val planNo: String,
    @ModelProperty(2, "产品编码")
    val productCode: String,
    val productId: Int,
    @ModelProperty(3, "产品描述")
    val productName: String,
    @ModelProperty(4, "型号代号")
    val productModel: String,
    @ModelProperty(5, "主计划状态", dataParameterType = "BPS_PLAN_STATUS")
    val planStatus: Int,
    @ModelProperty(6, "主计划数量")
    val planNumber: Int,
    val remark: String,
    val reportList: List<MainPlanReportModel>,
    val workNo: String
)

data class MainPlanReportModel(
    val equRealReportTime: Int,
    val equipmentStr: String,
    val jockeyNameStr: String,
    val realReportTime: Int,
    val reportNumber: Int,
    val reportTime: Double
)