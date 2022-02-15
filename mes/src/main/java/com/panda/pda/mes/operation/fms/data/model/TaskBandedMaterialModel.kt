package com.panda.pda.mes.operation.fms.data.model

/**
 * created by AnJiwei 2021/9/1
 */
data class TaskBandedMaterialModel(
    val bindList: List<MaterialModel>,
    val toBindList: List<MaterialModel>,
    val totalBindCount: Int,
    val totalToBindCount: Int
)