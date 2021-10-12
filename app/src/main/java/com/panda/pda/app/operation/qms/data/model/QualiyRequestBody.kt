package com.panda.pda.app.operation.qms.data.model

import com.squareup.moshi.Json

/**
 * created by AnJiwei 2021/9/26
 */


data class QualityTaskDistributeRequest(
    val id: Int,
    val inspectorId: Int,
    val distributedNum: Int,
    val expectStartTime: String,
    val expectEndTime: String
)

data class QualityTaskCommitRequest(
    @field:Json(name = "reviewerId") val verifierId: Int,
    val remark: String
)

data class QualityTaskTransferRequest(
    val id: Int,
    val verifierId: Int,
    val remark: String
)

data class QualityTaskReviewRequest(
    val id: Int,
    val conclusion: Int,
    val remark: String
)

