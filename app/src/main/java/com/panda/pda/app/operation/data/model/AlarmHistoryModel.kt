package com.panda.pda.app.operation.data.model

/**
 * created by AnJiwei 2021/9/1
 */

data class AlarmHistoryListModel(
    val dataList: List<AlarmHistoryModel>,
    val total: Int
)

data class AlarmHistoryModel(
    val alarmCode: String,
    val alarmDetail: String,
    val closeId: Int,
    val closeName: String,
    val closeRemark: String,
    val createName: String,
    val createTime: String,
    val id: Int,
    val status: Int
)