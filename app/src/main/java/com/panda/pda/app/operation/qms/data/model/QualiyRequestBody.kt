package com.panda.pda.app.operation.qms.data.model

/**
 * created by AnJiwei 2021/9/26
 */

/* id	integer
必须
质检任务id
inspectorId	integer
必须
质检人id
distributedNum	integer
必须
派发数量
expectStartTime	string
必须
计划开始时间
expectEndTime	string
必须
计划完成时间*/

data class QualityTaskDistributeRequest(
    val id: Int,
    val inspectorId: Int,
    val distributedNum: Int,
    val expectStartTime: String,
    val expectEndTime: String
)

