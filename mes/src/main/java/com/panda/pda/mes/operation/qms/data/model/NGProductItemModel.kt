package com.panda.pda.mes.operation.qms.data.model

/**
 * created by AnJiwei 2022/11/18
 */
data class NGProductItemModel(
    val dealTime: String,
    val dealType: Int,
    val id: Int,
    val procedureBadList: List<ProcedureItem>,
    val procedureReturnList: List<ProcedureItem>,
    val productBarCode: String,
    val productCount: String,
    val qualityTaskId: String,
    val realFinishTime: String,
    val remark: String,
    val status: String
)

data class ProcedureItem(
    val procedureCode: String,
    val procedureName: String
)