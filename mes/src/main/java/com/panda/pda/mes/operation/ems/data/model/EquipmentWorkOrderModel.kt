package com.panda.pda.mes.operation.ems.data.model

import com.panda.pda.mes.common.data.model.FileInfoModel

/**
 * outhor:yinzhilin
 * 创建时间：2022/2/22 1:47 下午
 * Describe: 工单详情model
 */
data class EquipmentWorkOrderModel(
    //工单id
    val id: String,
    //设备编号
    val facilityCode: String,
    val facilityName: String,
    val facilityModel: String,
    val facilityType: String,
    val facilityId: Int,
    //工单号
    val workOrderCode: String,
    //填报人
    val submitId: String,
    //填报人名称
    val createName: String,
    //更新人
    val updateName: String,
    //填报时间
    val createTime: String,
    //部门id
    val orgId: String,
    //部门名称
    val orgName: String,
    //车间id
    val workShopId: String,
    //车间名称
    val workShopName: String,
    //位置(部门+区域拼接)
    val locationName: String,
    //坐标（设备/模具的独立属性）
    val coordinate: String,
    //类型
    val functionType: String,
    //状态
    val workOrderStatus: String,
    //是否提供样品
    val simpleProvided: String,
    //
    val expectCompleteTime: String,
    //
    val expectWorkTime: String,
    //关联设备
    val relatedEquipId: Int,
    //关联设备名称
    val relatedEquipDesc: String,
    //关联厂商
    val relatedSupplierId: Int,
    //关联厂商名称
    val relatedSupplierDesc: String,
    //
    val fileList: List<FileInfoModel>,
    //是否可操作  0代表不可操作，1代表可操作
    val operable: Int,
    //小组id
    val teamId: String,
    val remark: String,

    )

enum class EmsModelType(val code: Int) {
    Task(1),
    INFO(2),
    WORKORDER(3),
}