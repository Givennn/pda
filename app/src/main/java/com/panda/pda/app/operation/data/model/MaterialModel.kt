package com.panda.pda.app.operation.data.model

/**
 * created by AnJiwei 2021/8/31
 */
data class MaterialModel(
    val materialCode: String,
    val materialName: String,
    val materialNo: String,
    val materialNum: String,
    val bindProductBarCode: String?, //已绑定产品条码
    val materialSerialCode: String?
)