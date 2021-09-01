package com.panda.pda.app.task.data.model

/**
 * created by AnJiwei 2021/9/1
 */
data class TaskMessageCountModel(
    val taskToFinish: Int,
    val taskToReceive: Int,
    val taskToReport: Int,
    val taskToRun: Int
) {
    fun hasNewMessage(): Boolean {
        return taskToFinish + taskToReceive + taskToReport + taskToRun > 0
    }
}