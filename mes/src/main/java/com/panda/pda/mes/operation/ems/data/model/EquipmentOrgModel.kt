package com.panda.pda.mes.operation.ems.data.model

/**
 * outhor:yinzhilin
 * 创建时间：2022/2/22 1:47 下午
 * Describe: 二级部门列表
 */
data class EquipmentOrgModel(
    //部门id
    val id: String,
    //组织部门名称
    val orgName: String,
    //部门等级
    val orgLevel: String,
)