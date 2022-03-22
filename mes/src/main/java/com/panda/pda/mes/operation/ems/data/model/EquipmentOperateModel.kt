package com.panda.pda.mes.operation.ems.data.model

import com.panda.pda.mes.common.data.model.FileInfoModel

/**
 * outhor:yinzhilin
 * 创建时间：2022/2/22 1:47 下午
 * Describe: 操作记录
 */
data class EquipmentOperateModel(
    val completeFlag: Int,
    //是否合格  1-合格，2-限度生产，3-延期
    val conclusion: Int,
    val coordinate: String,
    val createId: Int,
    val createName: String,
    val createTime: String,
    val expectWorkTime: Int,
    val fileList: List<FileInfoModel>,
    val id: Int,
    val intactFlag: Int,
    val locationname: String,
    val operateDesc: String,
    val operateFullName: String,
    val operateId: Int,
    val operateName: String,
    val overTime: String,
    val overTimeFlag: Int,
    val productionProductIds: String,
    val productionProductNames: String,
    val productionTimes: Int,
    val productionTotalTimes: Int,
    val relatedEquipId: Int,
    val relatedEquipName: String,
    val relatedSupplierId: Int,
    val relatedSupplierName: String,
    val remark: String,
    val sampleProvided: Int,
    val taskStatus: Int,
    val updateId: Int,
    val updateName: String,
    val updateTime: String,
    val workOrderCode: String,
    val workOrderId: Int,
    val workOrderStatus: Int,
    val workTime: Int,
    //类型
    val functionType: String="",
    //设备类型
    val facilityType: String="",

    )