package com.panda.pda.mes.user.data.model

/**
 * created by AnJiwei 2022/9/2
 */
data class PersonalPerformanceModel(
    val createId: Int,
    val createName: String,
    val createTime: String,
    val deployDate: String,
    val dutyCode: String,
    val dutyName: String,
    val id: Int,
    val orgId: Int,
    val orgName: String,
    val performance: Int,
    val realAmount: String,
    val remark: String,
    val statisticsTime: String,
    val status: Int,
    val updateId: Int,
    val updateName: String,
    val updateTime: String,
    val userId: Int,
    val userName: String,
    val workCode: String
)