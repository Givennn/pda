package com.panda.pda.mes.operation.qms.data.model

/**
 * created by AnJiwei 2022/11/18
 */
data class NGProductItemModel(
    val dealTime: String? = null,
    var dealType: Int? = null,
    val id: Int,
    var procedureBadList: List<ProcedureItem> = listOf(),
    var procedureReturnList: List<ProcedureItem> = listOf(),
    var productBarCode: String? = null,
    var productCount: String? = null,
    var qualityTaskId: String? = null,
    val realFinishTime: String? = null,
    var remark: String? = null,
    var status: Int? = null
) {
    constructor(status: Int): this(null, null, -1, listOf(), listOf(), null, null ,null, null, null, status)
}

data class ProcedureItem(
    val procedureCode: String,
    val procedureName: String
) {
    override fun toString(): String {
        return "$procedureCode \t $procedureName"
    }
}