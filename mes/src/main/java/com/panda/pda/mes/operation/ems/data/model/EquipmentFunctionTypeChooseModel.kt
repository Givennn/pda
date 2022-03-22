package com.panda.pda.mes.operation.ems.data.model

import com.panda.pda.mes.common.data.model.FileInfoModel

/**
 * outhor:yinzhilin
 * 创建时间：2022/2/22 1:47 下午
 * Describe: 填报工单时，类型选择对象
 */
data class EquipmentFunctionTypeChooseModel(
    //id
    val id: String,
    //字典名称
    val functionName: String,
    //字典类型 1:维修 2:保养 3:出库
    val functionType: String,
    //类型 1:设备 2:厂商 3:其他
    val functionRealAttr: String,
    //1:启用 2:禁用
    val status: String,

)