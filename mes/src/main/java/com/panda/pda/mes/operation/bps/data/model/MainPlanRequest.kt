package com.panda.pda.mes.operation.bps.data.model

import com.panda.pda.mes.common.data.model.FileInfoModel
import com.panda.pda.mes.common.data.model.IdRequest
import com.panda.pda.mes.common.data.model.PersonModel

/**
 * created by AnJiwei 2022/8/8
 */
data class MainPlanReportRequest(
    val id: Int,
    val remark: String,
    val fileList: List<FileInfoModel>,
    val reportList: List<MainPlanReportItem>
)

data class MainPlanReportItem(
    var reportNumber: Int?,
    var reportTime: Int?, // 工时
    var equipmentTime: Int?,
    var jockeyList: List<Int>,
    var equipmentList: List<Int>

) {
    @Transient
    var selectedPerson = listOf<PersonModel>()
    @Transient
    var selectedEquipment = listOf<EquipmentModel>()
}

data class MainPlanFinishRequest(
    val id: Int,
    val inspectNum: Int? = null
)