package com.panda.pda.mes.operation.qms.data.model

/**
 * created by AnJiwei 2022/11/18
 */
data class NGProductItemModel(
    val dealTime: String,
    val dealType: Int,
    val id: String,
    val procedureBadList: List<ProcedureBad>,
    val procedureReturnList: List<ProcedureReturn>,
    val productBarCode: String,
    val productCount: String,
    val qualityTaskId: String,
    val realFinishTime: String,
    val remark: String,
    val status: String
)

data class ProcedureBad(
    val procedureCode: String,
    val procedureName: String
)

data class ProcedureReturn(
    val procedureCode: String,
    val procedureName: String
)