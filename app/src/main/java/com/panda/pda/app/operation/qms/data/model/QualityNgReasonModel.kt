package com.panda.pda.app.operation.qms.data.model

import java.io.Serializable

/**
 * created by AnJiwei 2021/9/26
 */
data class QualityNgReasonModel(
    val badnessReasonClassify: String,
    val badnessReasonCode: String,
    val badnessReasonName: String,
    val id: Int
): Serializable {
    @Transient
    var isChecked = false
}