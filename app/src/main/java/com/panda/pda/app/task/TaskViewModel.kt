package com.panda.pda.app.task

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.panda.pda.app.task.data.model.TaskInfoModel

/**
 * created by AnJiwei 2021/8/26
 */
class TaskViewModel: ViewModel() {

    val taskInfoData = MutableLiveData<TaskInfoModel>()

    companion object {
        const val TASK_ID = "taskID"
    }
}