package com.panda.pda.app.operation.qms.data.model

import com.panda.pda.app.common.data.model.FileInfoModel

/**
 * created by AnJiwei 2021/9/26
 */
data class QualityProblemRecordDetailModel(
    val adverseCause: String,
    val batchNo: String,
    val causeAnalysis: String,
    val conclusion: String,
    val createId: String,
    val createName: String,
    val createTime: String,
    val fileList: List<FileInfoModel>,
    val id: Int,
    val inspector: String,
    val inspectorTime: String,
    val occurrencePlace: String,
    val optimization: String,
    val orderCode: String,
    val pictureList: List<FileInfoModel>,
    val planCode: String,
    val problemCode: String,
    val processCycle: String,
    val productBarCode: String,
    val productCode: String,
    val productDesc: String,
    val productName: String,
    val qualityCode: String,
    val remark: String,
    val solution: String,
    val status: String,
    val taskCode: String,
    val taskDesc: String,
    val taskId: Int,
    val traceDepartment: String,
    val traceTime: String,
    val traceUser: String,
    val updateId: String,
    val updateName: String,
    val updateTime: String,
    val workNo: String
)