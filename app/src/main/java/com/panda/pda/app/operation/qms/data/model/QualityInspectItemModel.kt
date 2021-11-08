package com.panda.pda.app.operation.qms.data.model

/**
 * created by AnJiwei 2021/9/26
 */
data class QualityInspectItemModel(
    val defaultValue: String,
    val equipmentDesc: String,
    val fixtureDesc: String,
    val id: Int,
    val minusDeviation: String,
    val multiplicity: String,
    val positiveDeviation: String,
    val precision: String,
    val qualityClassifyName: String,
    val qualityCode: String,
    val qualityMethod: String,
    val qualityName: String,
    val standard: String,
    val valueType: Int,
    val qualityFillInRegulation: Int?
) {
    @Transient
    var conclusion: String? = null
}