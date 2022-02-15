package com.panda.pda.mes.operation.qms

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.panda.pda.mes.base.retrofit.DataListNode
import com.panda.pda.mes.operation.qms.data.model.QualityDetailModel
import com.panda.pda.mes.operation.qms.data.model.QualityProblemRecordDetailModel
import com.panda.pda.mes.operation.qms.data.model.QualitySubTaskDetailModel
import com.panda.pda.mes.operation.qms.data.model.QualityTaskRecordModel

/**
 * created by AnJiwei 2021/9/28
 */
class QualityViewModel : ViewModel() {

    val qualityDetailInfoData by lazy { MutableLiveData<QualityDetailModel>() }

    val qualityDetailRecordData by lazy { MutableLiveData<DataListNode<QualityTaskRecordModel>>() }

    val qualityDetailSubTaskData by lazy { MutableLiveData<QualitySubTaskDetailModel>() }

    val problemRecordDetailData by lazy { MutableLiveData<QualityProblemRecordDetailModel>() }
}