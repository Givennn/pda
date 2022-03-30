package com.panda.pda.mes.operation.ems.data.model

/**
 * outhor:yinzhilin
 * 创建时间：2022/2/22 1:47 下午
 * Describe: 厂商选择对象
 */
data class EquipmentSupplierChooseModel(
    //id
    val id: String,
    //供应商编码
    val supplierCode: String,
    //供应商名称
    val supplierDesc: String,
    val remark: String,
    //是否选中
    var isChecked: Boolean,
)