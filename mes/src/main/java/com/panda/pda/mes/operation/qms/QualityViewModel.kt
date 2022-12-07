package com.panda.pda.mes.operation.qms

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panda.pda.mes.base.retrofit.DataListNode
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.data.CommonApi
import com.panda.pda.mes.common.data.model.QMSModuleProperty
import com.panda.pda.mes.common.data.model.SysModule
import com.panda.pda.mes.common.data.model.SysParameter
import com.panda.pda.mes.operation.qms.data.model.QualityDetailModel
import com.panda.pda.mes.operation.qms.data.model.QualityProblemRecordDetailModel
import com.panda.pda.mes.operation.qms.data.model.QualitySubTaskDetailModel
import com.panda.pda.mes.operation.qms.data.model.QualityTaskRecordModel
import kotlinx.coroutines.launch

/**
 * created by AnJiwei 2021/9/28
 */
class QualityViewModel : ViewModel() {

    val qualityDetailInfoData by lazy { MutableLiveData<QualityDetailModel>() }

    val qualityDetailRecordData by lazy { MutableLiveData<DataListNode<QualityTaskRecordModel>>() }

    val qualityDetailSubTaskData by lazy { MutableLiveData<QualitySubTaskDetailModel>() }

    val problemRecordDetailData by lazy { MutableLiveData<QualityProblemRecordDetailModel>() }

    val qmsSysParametersData by lazy { MutableLiveData<List<SysParameter>>() }

    suspend fun getQmsSysParameters(): Result<List<SysParameter>> {

        return if (qmsSysParametersData.value == null) {

            return viewModelScope.runCatching {
                WebClient.request(CommonApi::class.java)
                    .pdaConfigSysQueryGet(SysModule.QMS.name).dataList
            }.onSuccess {
                qmsSysParametersData.postValue(it)
            }
        } else {
            Result.success(qmsSysParametersData.value!!)
        }
    }


    suspend fun getQmsSysProperty(property: QMSModuleProperty): SysParameter? {

        val parameters = getQmsSysParameters().getOrNull()
        return parameters?.firstOrNull { it.sysParam == property.name }
    }

}