package com.panda.pda.app.operation.qms.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * created by AnJiwei 2021/9/26
 */

/**
 * @property id 问题记录id
 * @property problemCode 问题记录编号
 * @property productBarCode 产品条码
 * @property productDesc 产品描述
 * @property occurrencePlace 发生地
 * @property qualityCode 质检任务编号
 * @property productCode 任务编号
 * @property productName 任务描述
 * @property planCode 计划编号
 * @property taskId 任务id
 * @property workNo 工作令号
 * @property batchNo 批次号
 * @property orderCode 订单编号
 * @property adverseCause 不良原因
 * @property conclusion 质检结论
 * @property inspector 质检人
 * @property inspectorTime 质检时间
 * @property traceTime 跟踪时间
 * @property traceDepartment 跟踪部门
 * @property causeAnalysis 原因分析
 * @property solution 解决对策
 * @property optimization 优化建议
 * @property processCycle 处理周期
 * @property status 状态 1-未处理 2-已处理
 * @property remark 备注
 * @property updateId 更新人id
 * @property updateName 更新人名称
 * @property updateTime 更新时间
 */

data class QualityProblemRecordModel(
    @Json(name = "id") @field:Json(name = "id") val id: Int,
    @Json(name = "problemCode") @field:Json(name = "problemCode") val problemCode: String,
    @Json(name = "productBarCode") @field:Json(name = "productBarCode") val productBarCode: String,
    @Json(name = "productDesc") @field:Json(name = "productDesc") val productDesc: String,
    @Json(name = "occurrencePlace") @field:Json(name = "occurrencePlace") val occurrencePlace: String,
    @Json(name = "qualityCode") @field:Json(name = "qualityCode") val qualityCode: String,
    @Json(name = "productCode") @field:Json(name = "productCode") val productCode: String,
    @Json(name = "productName") @field:Json(name = "productName") val productName: String,
    @Json(name = "planCode") @field:Json(name = "planCode") val planCode: String,
    @Json(name = "taskId") @field:Json(name = "taskId") val taskId: Int,
    @Json(name = "workNo") @field:Json(name = "workNo") val workNo: String,
    @Json(name = "batchNo") @field:Json(name = "batchNo") val batchNo: String,
    @Json(name = "orderCode") @field:Json(name = "orderCode") val orderCode: String,
    @Json(name = "adverseCause") @field:Json(name = "adverseCause") val adverseCause: String,
    @Json(name = "conclusion") @field:Json(name = "conclusion") val conclusion: String,
    @Json(name = "inspector") @field:Json(name = "inspector") val inspector: String,
    @Json(name = "inspectorTime") @field:Json(name = "inspectorTime") val inspectorTime: String,
    @Json(name = "traceTime") @field:Json(name = "traceTime") val traceTime: String,
    @Json(name = "traceDepartment") @field:Json(name = "traceDepartment") val traceDepartment: String,
    @Json(name = "causeAnalysis") @field:Json(name = "causeAnalysis") val causeAnalysis: Int,
    @Json(name = "solution") @field:Json(name = "solution") val solution: String,
    @Json(name = "optimization") @field:Json(name = "optimization") val optimization: String,
    @Json(name = "processCycle") @field:Json(name = "processCycle") val processCycle: String,
    @Json(name = "status") @field:Json(name = "status") val status: String,
    @Json(name = "remark") @field:Json(name = "remark") val remark: String,
    @Json(name = "updateId") @field:Json(name = "updateId") val updateId: Int,
    @Json(name = "updateName") @field:Json(name = "updateName") val updateName: String,
    @Json(name = "updateTime") @field:Json(name = "updateTime") val updateTime: String,
    val traceUser: String,
    val qualitySolutionCode: String,
    val qualitySolutionName: String
    )
