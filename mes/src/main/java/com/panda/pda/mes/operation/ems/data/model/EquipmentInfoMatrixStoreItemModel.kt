package com.panda.pda.mes.operation.ems.data.model

/**
 * outhor:yinzhilin
 * 创建时间：2022/2/22 1:47 下午
 * Describe: 模具出入库记录item信息
 */
data class EquipmentInfoMatrixStoreItemModel(
    //
    val id: Int,
    //工单编号
    val workOrderCode: String,
    //负责部门id
    val orgId: Int,
    //负责部门名称
    val orgName: String,
    //处理小组id
    val teamId: Int,
    //处理小组名称
    val teamName: String,
    //填报人
    val submitId: Int,
    //填报人名称
    val submitName: String,
    //入库人id
    val completeId: String,
    //入库人名称
    val completeName: String,
    //操作描述内容
    val operateDesc: String,
    //功能状态
    val workOrderStatus: String,
    //描述
    val remark: String,
    //设备编号
    val facilityCode: String,
    //设备type 1设备  2模具
    val facilityType: String,
    //设备名称
    val facilityName: String,
    //设备型号代号
    val facilityModel: String,
    //更新人
    val updateName: String,
    )