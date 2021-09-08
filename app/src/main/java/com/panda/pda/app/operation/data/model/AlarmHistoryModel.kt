package com.panda.pda.app.operation.data.model

/**
 * created by AnJiwei 2021/9/1
 */

data class AlarmHistoryListModel(
    val dataList: List<AlarmHistoryModel>,
    val total: Int,
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
    val status: Int,
    val closeTime: String?,
) {
    val alarmStatus: AlarmStatus get() = if (status == 1) AlarmStatus.Open else AlarmStatus.Close
}

enum class AlarmStatus(val type: String) {
    Open("未关闭"),
    Close("已关闭")
}
