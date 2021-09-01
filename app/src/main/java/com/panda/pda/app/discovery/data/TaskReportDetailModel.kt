package com.panda.pda.app.discovery.data

import com.panda.pda.app.common.data.model.FileInfoModel

/**
 * created by AnJiwei 2021/9/1
 */
data class TaskReportDetailModel(
    val batchNo: String,
    val fileList: List<FileInfoModel>,
    val id: Int,
    val orderNo: String,
    val planCode: String,
    val productCode: String,
    val productName: String,
    val remark: String,
    val reportNumber: Int,
    val reportTime: Int,
    val taskCode: String,
    val taskDesc: String,
    val taskNum: Int,
    val workNo: String
)