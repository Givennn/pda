package com.panda.pda.app.operation.qms.data.model

/**
 * created by AnJiwei 2021/9/26
 */
data class QualitySubTaskDetailModel(
    val concessionNum: String,
    val distributedNum: String,
    val expectEndTime: String,
    val expectStartTime: String,
    val id: String,
    val inspectedNum: String,
    val inspectorName: String,
    val notInspectedNum: String,
    val productCode: String,
    val productName: String,
    val qualifiedNum: String,
    val qualityMethod: String,
    val qualityNotInspectedNum: String,
    val qualitySolutionName: String,
    val qualityTaskCode: String,
    val qualityTaskDesc: String,
    val qualityType: String,
    val realEndTime: String,
    val realStartTime: String,
    val temporaryControlNum: String,
    val unqualifiedNum: String
)