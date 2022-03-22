package com.panda.pda.mes.operation.ems.data.model

import com.panda.pda.mes.common.data.model.FileInfoModel

data class EquipmentInfoMatrixModel(
    //客户名称
    val customerDesc: String,
    //客户id
    val customerId: Int,
    //设备编号
    val equipmentCode: String,
    //设备名称
    val equipmentDesc: String,
    //设备型号代号
    val equipmentModel: String,
    //文件附件列表
    val equipmentFileList: List<FileInfoModel>,
    //功能状态 1-正常 2-维修 3-保养 4-报废 5-归还
    val functionStatus: Int,
    //
    val id: Int,
    //负责部门名称
    val orgName: String,
    //负责部门id
    val organizationId: Int,
    //启用状态 1 启用 2 停用
    val status: Int,
    //处理小组id
    val teamId: Int,
    //处理小组名称
    val teamName: String,
    //模次数
    val totalModulus: Int,
    //保养计划使用时间
    val useDate: Int
)