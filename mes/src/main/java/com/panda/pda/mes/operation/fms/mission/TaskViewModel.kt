package com.panda.pda.mes.operation.fms.mission

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.panda.pda.mes.operation.fms.data.model.TaskInfoModel
import com.panda.pda.mes.common.data.model.TaskMessageCountModel

/**
 * created by AnJiwei 2021/8/26
 */
class TaskViewModel: ViewModel() {

    val taskInfoData = MutableLiveData<TaskInfoModel>()
    val taskMsgData = MutableLiveData<List<TaskMessageCountModel>>()

    companion object {
        const val TASK_ID = "taskID"
    }
}