package com.panda.pda.app.discovery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.panda.pda.app.discovery.data.TaskReportDetailModel

/**
 * created by AnJiwei 2021/9/1
 */
class TaskReportViewModel: ViewModel() {

    val taskReportDetailData = MutableLiveData<TaskReportDetailModel>()
}