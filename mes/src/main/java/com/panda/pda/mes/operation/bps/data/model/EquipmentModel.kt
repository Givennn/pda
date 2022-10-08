package com.panda.pda.mes.operation.bps.data.model

/**
 * created by AnJiwei 2022/9/24
 */
data class EquipmentModel(
    val equipmentCode: String,
    val equipmentDesc: String,
    val equipmentModel: String,
    val id: Int,
    var isSelected: Boolean = false,
)