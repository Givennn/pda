package com.panda.pda.mes.operation.qms.data.model

import com.squareup.moshi.Json

/**
 * created by AnJiwei 2021/9/26
 */


data class QualityTaskDistributeRequest(
    val id: Int,
    val inspectorId: Int,
    val distributedNum: Int,
    val expectStartTime: String?,
    val expectEndTime: String?
)

data class QualityTaskCommitRequest(
    val id: Int,
    @field:Json(name = "reviewerId") val verifierId: Int,
    val remark: String
)

data class QualityTaskTransferRequest(
    val id: Int,
    val reviewerId: Int,
    val remark: String
)

data class QualityTaskDistributeCancelRequest(
    val id: Int,
    val cancelReason: String
)

data class QualityTaskDistributeTransferRequest(
    val id: Int,
    val distributeId: Int,
    val remark: String?
)

data class QualityTaskReviewRequest(
    val id: Int,
    val conclusion: Int,
    val remark: String
)

data class QualityTaskExecuteRequest(
    val id: Int,
    val conclusion: String,
    val productBarCode: String,
    val productCount: Int?,
    val badnessReasonIds: List<Int>?,
    val qualityItems: List<QualityItem>
)

data class QualityItem(
    val id: Int,
    val conclusion: String
)

