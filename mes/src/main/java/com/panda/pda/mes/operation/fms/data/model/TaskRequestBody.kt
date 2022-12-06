package com.panda.pda.mes.operation.fms.data.model

import com.panda.pda.mes.common.data.model.FileInfoModel

data class TaskReportRequest(
    val id: Int,
    val reportNumber: Int,
    val remark: String,
    val fileList: List<FileInfoModel>,
    val resourceList: List<ResourceModel>,
    val deliverNumber: Int?,
    )
