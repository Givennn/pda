package com.panda.pda.mes.operation.ems.data.model

import com.panda.pda.mes.common.data.model.FileInfoModel

//ems中所有requestbody
//工单填报入参
data class WorkOrderAddRequest(
    //设备id
    val facilityId: Int,
    //设备类型
    val facilityType: String,
    //工单类型  1-维护，2-保养，3-出库
    val functionType: String,
    //工单子类型   功能类型的id
    val functionSubType: String,
    //关联设备id
    val relatedEquipId: String,
    //关联供应商
    val relateSupplierId: String,
    //描述
    val remark: String,
    //图片列表
    val fileList: List<FileInfoModel>,
)

//工单分配
data class WorkOrderFenpeiRequest(
    //工单id
    val id: String,
    //分配的人员列表
    val userIds: List<Int>,
    //预计完工时间
    val expectCompleteDate: String,
    //预计工时
    val expectWorkTime: Int,
    //1-有样品，2-无
    val sampleProvided: Int,
    //描述
    val remark: String,
)

//工单维保完工
data class WorkOrderWangongRequest(
    //工单id
    val id: String,
    //描述
    val remark: String,
    //图片列表
    val fileList: List<FileInfoModel>,
)

//工单维保确认
data class WorkOrderWBConfirmRequest(
    //工单id
    val id: String,
    //是否合格  1-合格，2-限度生产，3-延期
    val conclusion: Int,
    //描述
    val remark: String,
    //图片列表
    val fileList: List<FileInfoModel>,
)

//工单出库确认
data class WorkOrderOutStoreConfirmRequest(
    //工单id
    val id: String,
    //描述
    val remark: String,
    //图片列表
    val fileList: List<FileInfoModel>,
)

//工单提交入库
data class WorkOrderInStoreSubmitRequest(
    //工单id
    val id: String,
    //产品列表
    val productIdList: List<Int>,
    //产品次数
    val productTimes: Int,
    //描述
    val remark: String,
    //图片列表
    val fileList: List<FileInfoModel>,
)
data class MessageReadRequest(
    val msgIdList: MutableList<Int>,
)

//工单入库确认
data class WorkOrderInStoreConfirmRequest(
    //工单id
    val id: String,
    //是否合格1-完好，2-否
    val intectFlag: Int,
    //坐标
    val coordinate: String,
    //描述
    val remark: String,
    //图片列表
    val fileList: List<FileInfoModel>,
)
