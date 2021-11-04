package com.panda.pda.app.operation.qms.data.model

import com.panda.pda.app.common.ModelProperty
import java.io.Serializable

/**
 * created by AnJiwei 2021/9/26
 */
data class QualitySubTaskDetailModel(
    @ModelProperty(1, "质检任务编号")
    val qualityTaskCode: String,
    @ModelProperty(2, "质检任务描述")
    val qualityTaskDesc: String,
    @ModelProperty(3, "质检方案")
    val qualitySolutionName: String,
    @ModelProperty(4, "质检类型")
    val qualityType: String,
    @ModelProperty(5, "质检方式")
    val qualityMethod: String,
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
    val expectEndTime: String,
    val expectStartTime: String,
    val id: Int,
    val notInspectedNum: String,
    val qualifiedNum: String,
    val concessionNum: String,
    val qualityNotInspectedNum: String,
    val realEndTime: String,
    val realStartTime: String,
    val temporaryControlNum: String,
    val unqualifiedNum: String
): Serializable