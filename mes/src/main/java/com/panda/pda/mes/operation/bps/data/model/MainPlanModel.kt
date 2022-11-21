package com.panda.pda.mes.operation.bps.data.model

/**
 * created by AnJiwei 2022/8/8
 */
data class MainPlanModel(
    val createId: Int,
    val createName: String,
    val createTime: String,
    val customerId: Int,
    val customerName: String,
    val deliveryTime: String,
    val finishTime: String,
    val id: Int,
    val planNo: String,
    val planNumber: Int,
    val planStatus: Int,
    val productCode: String,
    val productId: Int,
    val productModel: String,
    val productName: String,
    val remark: String,
    val reportList: List<ReportModel>?,
    val updateId: Int,
    val updateName: String,
    val updateTime: String,
    val workNo: String,
    val planSource: Int,
    val selfInspection: Int,
    val specialInspection: Int
)

data class ReportModel(
    val createId: Int,
    val createName: String,
    val createTime: String,
    val id: Int,
    val remark: String
)