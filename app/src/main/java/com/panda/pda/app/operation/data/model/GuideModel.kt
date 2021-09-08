package com.panda.pda.app.operation.data.model

import java.io.Serializable

/**
 * created by AnJiwei 2021/9/1
 */
data class GuideListModel(
    val dataList: List<GuideInfoModel>,
    val total: Int
)

data class GuideInfoModel(
    val fileId: Int,
    val fileName: String,
    val fileUrl: String,
    val procedureCode: String,
    val procedureDesc: String,
    val procedureId: Int,
    val productCode: String,
    val productName: String,
    val technicsCode: String,
    val technicsDesc: String,
    val technicsVersion: String
) : Serializable