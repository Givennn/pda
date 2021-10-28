package com.panda.pda.app.operation.qms

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.panda.pda.app.base.retrofit.DataListNode
import com.panda.pda.app.operation.qms.data.model.QualityDetailModel
import com.panda.pda.app.operation.qms.data.model.QualityProblemRecordDetailModel
import com.panda.pda.app.operation.qms.data.model.QualityTaskRecordModel

/**
 * created by AnJiwei 2021/9/28
 */
class QualityViewModel : ViewModel() {

    val qualityDetailInfoData by lazy { MutableLiveData<QualityDetailModel>() }

    val qualityDetailRecordData by lazy { MutableLiveData<DataListNode<QualityTaskRecordModel>>() }

    val problemRecordDetailData by lazy { MutableLiveData<QualityProblemRecordDetailModel>() }
}