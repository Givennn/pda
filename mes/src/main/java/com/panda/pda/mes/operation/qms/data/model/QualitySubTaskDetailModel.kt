package com.panda.pda.mes.operation.qms.data.model

import com.panda.pda.mes.common.ModelProperty
import java.io.Serializable

/**
 * created by AnJiwei 2021/9/26
 */
data class QualitySubTaskDetailModel(
    @ModelProperty(1, "质检任务编号")
    val qualityTaskCode: String,
    @ModelProperty(2, "质检任务描述")
    val qualityDesc: String,
    @ModelProperty(3, "质检方案")
    val qualitySolutionName: String,
    @ModelProperty(4, "质检类型", dataParameterType = "QUALITY_TYPE")
    val qualityType: Int,
    @ModelProperty(5, "质检方式", dataParameterType = "QUALITY_METHOD")
    val qualityMethod: Int,
    @ModelProperty(6, "产品编码")
    val productCode: String,
    @ModelProperty(7, "产品描述")
    val productName: String,
    @ModelProperty(8, "质检人")
    val inspectorName: String,
    @ModelProperty(9, "派发数量")
    val distributedNum: String,
    @ModelProperty(10, "已质检数量")
    val inspectedNum: String,

    val id: Int,
    @ModelProperty(11, "待质检数量")
    val qualityNotInspectedNum: String,
    @ModelProperty(12, "合格数量")
    val qualifiedNum: String,
    @ModelProperty(13, "让步合格数量")
    val concessionNum: String,
    @ModelProperty(14, "待检数量")
    val notInspectedNum: String,
    @ModelProperty(15, "不合格数量")
    val unqualifiedNum: String,
    @ModelProperty(20, "暂控数量")
    val temporaryControlNum: String,
    @ModelProperty(21, "预计开始时间")
    val expectStartTime: String,
    @ModelProperty(22, "预计完成时间")
    val expectEndTime: String,
    @ModelProperty(23, "实际开始时间")
    val realStartTime: String,
    @ModelProperty(24, "实际完成时间")
    val realEndTime: String,
    val conclusion: String
): Serializable