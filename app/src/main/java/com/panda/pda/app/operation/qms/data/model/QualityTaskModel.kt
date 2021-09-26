package com.panda.pda.app.operation.qms.data.model

/**
 * created by AnJiwei 2021/9/24
 */
data class QualityTaskModel(
    val batchNo: String,
    val createId: Int,
    val createName: String,
    val createTime: String,
    val deliverId: String,
    val deliverName: String,
    val deliverNum: Int,
    val deliverTime: String,
    val distributedNum: Int,
    val id: Int,
    val orderCode: String,
    val planCode: String,
    val planEndTime: String,
    val planStartTime: String,
    val productCode: String,
    val productName: String,
    val qualityCode: String,
    val qualityDesc: String,
    val qualityMethod: String,
    val qualityNum: Int,
    val qualitySolutionCode: String,
    val qualitySolutionName: String,
    val qualityType: String,
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