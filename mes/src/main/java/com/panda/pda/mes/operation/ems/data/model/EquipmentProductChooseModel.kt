package com.panda.pda.mes.operation.ems.data.model

import com.panda.pda.mes.common.data.model.FileInfoModel

/**
 * outhor:yinzhilin
 * 创建时间：2022/2/22 1:47 下午
 * Describe: 小组人员对象
 */
data class EquipmentProductChooseModel(
    //产品id
    val id: Int,
    //产品编码
    val productCode: String,
    //产品名称
    val productName: String,
    //是否选中
    var isChecked: Boolean,
)