package com.panda.pda.mes.operation.ems.data.model

/**
 * outhor:yinzhilin
 * 创建时间：2022/2/22 1:47 下午
 * Describe: 小组人员对象
 */
data class EquipmentPersonChooseModel(
    //人员id
    val id: Int,
    //人员姓名
    val realName: String,
    val userName: String,
    //人员角色
    val roleNames: String,
    //部门名称
    val orgName: String,
    //车间名称
    val workshopNames: String,
    //是否选中
    var isChecked: Boolean,
)