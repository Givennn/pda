package com.panda.pda.mes.user.data.model

/**
 * created by AnJiwei 2022/9/2
 */
data class WorkHourModel(
    val createId: Int,
    val createName: String,
    val createTime: String,
    val dispatchOrderCode: String,
    val dispatchOrderDesc: String,
    val id: Int,
    val performanceId: Int,
    val planNo: String,
    val remark: String,
    val updateId: Int,
    val updateName: String,
    val updateTime: String,
    val workOrderCode: String,
    val workTimeHour: Double,
    val workTimePerformance: Double
)