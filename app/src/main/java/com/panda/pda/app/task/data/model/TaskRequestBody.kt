package com.panda.pda.app.task.data.model

import com.panda.pda.app.common.data.model.FileInfoModel

/**
 * created by AnJiwei 2021/8/30
 */
data class TaskIdRequest(val id: Int)

data class TaskReportRequest(
    val id: Int,
    val reportNumber: Int,
    val remark: String,
    val fileList: List<FileInfoModel>
)
