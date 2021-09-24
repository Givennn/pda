package com.panda.pda.app.operation.fms.data.model

/**
 * created by AnJiwei 2021/9/1
 */
data class MaterialBindRequest(
    val taskId: Int,
    val productBarCode: String,
    val materialSerialCode: String
)

data class MaterialUnbindRequest(
    val productBarCode: String,
    val materialSerialCode: String
)

data class MaterialReplaceBindRequest(
    val productBarCode: String,
    val newMaterialSerialCode: String,
    val oldMaterialSerialCode: String
)