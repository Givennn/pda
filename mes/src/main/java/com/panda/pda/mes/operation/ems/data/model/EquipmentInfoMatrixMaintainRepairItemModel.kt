package com.panda.pda.mes.operation.ems.data.model

/**
 * outhor:yinzhilin
 * 创建时间：2022/2/22 1:47 下午
 * Describe: 设备模具维保记录item信息
 */
data class EquipmentInfoMatrixMaintainRepairItemModel(
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
    //功能状态 1-报工（待分配）2-已分配（待完工） 3-已完工（待确认） 4-已确认
    val functionStatus: Int,
    //填报人
    val submitId: Int,
    //填报人名称
    val submitName: String,
    //
    val completeId: String,
    //
    val completeName: String,
    //维修人id
    val assignId: Int,
    //维修人名称
    val assignName: String,
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
    //工单功能状态 1-待分配，2-待完工， 3-待确认， 4-合格，5-限度生产，6-延期 ，7-出库待确认， 8-待入库， 9-入库待确认，10-在库
    val workOrderStatus: String,
    //1-合格 2-不合格
    val conclusion: String,
    //更新人
    val updateName: String,

    )