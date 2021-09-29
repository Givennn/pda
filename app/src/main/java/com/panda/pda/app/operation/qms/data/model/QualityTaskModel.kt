package com.panda.pda.app.operation.qms.data.model

import com.panda.pda.app.common.ModelProperty

/**
 * created by AnJiwei 2021/9/24
 */
data class QualityTaskModel(
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
    val deliverNum: Int,
    @ModelProperty(9, "需质检数量")
    val qualityNum: Int,
    @ModelProperty(10, "送检人")
    val deliverName: String,
    @ModelProperty(11, "送检时间")
    val deliverTime: String,
    val batchNo: String,
    val createId: Int,
    val createName: String,
    val createTime: String,
    val deliverId: String,
    val distributedNum: Int,
    val id: Int,
    val orderCode: String,
    val planCode: String,
    val planEndTime: String,
    val planStartTime: String,
    val qualitySolutionCode: String,
    val status: String,
    val taskCode: String,
    val taskDesc: String,
    val taskId: Int,
    val updateId: Int,
    val updateName: String,
    val updateTime: String,
    val workNo: String
)

enum class QualityTaskModelType(val code: Int) {
    Task(1),
    Review(2),
    Distribute(3),
    Sign(4),
    Execute(5),
    Finish(6)
}