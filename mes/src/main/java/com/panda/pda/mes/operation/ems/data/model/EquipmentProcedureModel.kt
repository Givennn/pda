package com.panda.pda.mes.operation.ems.data.model

/**
 * outhor:yinzhilin
 * 创建时间：2022/2/22 1:47 下午
 * Describe: 生产模次记录
 */
data class EquipmentProcedureModel(
    //id
    val id: String,
    //模具id
    val mouldId: String,
    //生产次数
    val procedureModulus: String,
    //总次数
    val totalModulus: String,
    //
    val createId: String,
    //创建时间
    val createTime: String,
    //创建人
    val createName: String,
)