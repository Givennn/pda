package com.panda.pda.mes.operation.ems.data.model

import com.panda.pda.mes.common.data.model.FileInfoModel

/**
 * outhor:yinzhilin
 * 创建时间：2022/2/22 1:47 下午
 * Describe: 设备信息model
 */
data class EquipmentInfoDeviceModel(
    //设备id
    val facilityId: Int,
    //设备编号
    val facilityCode: String,
    //设备type 1设备  2模具
    val facilityType: String,
    //设备名称
    val facilityDesc: String,
    //设备型号代号
    val facilityModel: String,
    //负责部门id
    val organizationId: Int,
    //负责部门名称
    val orgName: String,
    //处理小组id
    val teamId: String,
    //处理小组名称
    val teamName: String,
    //位置id
    val areaId: String,
    //位置名称
    val areaDesc: String,
    //功能状态 1-正常 2-维修 3-保养 4-报废 5-归还
    val functionStatus: Int,
    //位置状态 1-在库 2-出库----模具特有
    val locationStatus: Int,
    //保养计划使用时间
    val useDate: String,
    //启用状态 1 启用 2 停用
    val status: Int,
    //厂家/供应商id----设备特有
    val supplierId: String,
    //厂家/供应商名称----设备特有
    val supplierDesc: String,
    //客户id----模具特有
    val customerId: String,
    //客户名称----模具特有
    val customerDesc: String,
    //模次数----模具特有
    val totalModulus: String,
    //模具坐标----模具特有
    val coordinate: String,
    //文件附件列表
    val fileList: List<FileInfoModel>,
    //当前用户是否有保养报工权限
    val maintainAddPermission: Boolean,
    //当前用户是否有维修报工权限
    val repairAddPermission: Boolean,
    //当前用户是否有出入库报工权限
    val stockAddPerssion: Boolean,
    //当前用户是否有工单操作权限
    val handlePermission: Boolean,
    //工单id
    val workOrderId: String,
    //工单功能状态 1-待分配，2-待完工， 3-待确认， 4-合格，5-限度生产，6-延期 ，7-出库待确认， 8-待入库， 9-入库待确认，10-在库 11-作废
    val workOrderStatus: String,
    //功能类型 1-维修 2-保养 3-出库
    val workOrderFunctionType: String,
    var isChecked: Boolean,
)
