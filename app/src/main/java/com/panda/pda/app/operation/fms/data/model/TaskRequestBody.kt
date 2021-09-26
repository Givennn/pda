package com.panda.pda.app.operation.fms.data.model

import com.panda.pda.app.common.data.model.FileInfoModel

data class TaskReportRequest(
    val id: Int,
    val reportNumber: Int,
    val remark: String,
    val fileList: List<FileInfoModel>
)
