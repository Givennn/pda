package com.panda.pda.app.operation.fms.data.model

/**
 * created by AnJiwei 2021/8/26
 */
data class TaskDetailModel(
    val batchNo: String,
    val equipmentDesc: String,
    val id: Int,
    val jockeyName: String,
    val planCode: String,
    val planEndTime: String,
    val planStartTime: String,
    val productCode: String,
    val productName: String,
    val realEndTime: String,
    val realStartTime: String,
    val taskCode: String,
    val taskDesc: String,
    val taskNum: Int,
    val taskStatus: Int,
    val workNo: String,
    val reportNum: Int
)