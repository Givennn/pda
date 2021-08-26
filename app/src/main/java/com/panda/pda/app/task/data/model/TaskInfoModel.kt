package com.panda.pda.app.task.data.model

/**
 * created by AnJiwei 2021/8/26
 */
data class TaskInfoModel(
    val detail: TaskDetailModel,
    val recordList: List<TaskRecordModel>,
)