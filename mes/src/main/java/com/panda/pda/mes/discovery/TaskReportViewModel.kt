package com.panda.pda.mes.discovery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.panda.pda.mes.discovery.data.TaskReportDetailModel

/**
 * created by AnJiwei 2021/9/1
 */
class TaskReportViewModel: ViewModel() {

    val taskReportDetailData = MutableLiveData<TaskReportDetailModel>()
}