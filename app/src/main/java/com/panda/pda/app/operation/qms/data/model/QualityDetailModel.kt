package com.panda.pda.app.operation.qms.data.model

import com.panda.pda.app.common.ModelProperty

/**
 * created by AnJiwei 2021/9/24
 */


data class QualityDetailModel(
    @ModelProperty(1, "质检任务编号")
    val qualityCode: String,
    @ModelProperty(2, "质检任务描述")
    val qualityDesc: String,
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
    @ModelProperty(8, "送检数量")
    val deliverNum: String,
    @ModelProperty(9, "需质检数量")
    val qualityNum: String,
    @ModelProperty(10, "任务编号", [QualityDetailTag])
    val taskCode: String,
    @ModelProperty(11, "任务描述", [QualityDetailTag])
    val taskDesc: String,
    @ModelProperty(12, "计划编号", [QualityDetailTag])
    val planCode: String,
    @ModelProperty(13, "工作令号", [QualityDetailTag])
    val workNo: String,
    @ModelProperty(14, "批次号", [QualityDetailTag])
    val batchNo: String,
    @ModelProperty(15, "订单编号", [QualityDetailTag])
    val orderCode: String,
    @ModelProperty(16, "状态", [QualityDetailTag])
    val status: String,
    @ModelProperty(17, "计划开始时间", [QualityDetailTag])
    val planStartTime: String,
    @ModelProperty(18, "计划完成时间", [QualityDetailTag])
    val planEndTime: String,
    @ModelProperty(19, "送检人")
    val deliverName: String,
    @ModelProperty(20, "送检时间")
    val deliverTime: String,
    @ModelProperty(21, "更新人", [QualityDetailTag])
    val updateTime: String,
    @ModelProperty(22, "更新时间", [QualityDetailTag])
    val updateName: String,
//    val qualifiedNum: String? = null
//    val reportNumber: Int,
//    val createId: Int,
//    val createName: String,
//    val createTime: String,
//    val deliverId: String,
//    val distributedNum: Int,
    val id: Int,
//    val qualitySolutionCode: String,
//    val taskId: Int,
//    val updateId: Int,
//    val inspectedNum: String,
//    val qualityNotInspectedNum: String,
) {
    companion object {
        const val QualityDetailTag = "QualityDetail"
    }
}