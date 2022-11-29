package com.panda.pda.mes.operation.fms.alarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.panda.pda.mes.R
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.base.retrofit.DataListNode
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.CommonSearchListFragment
import com.panda.pda.mes.common.data.CommonParameters
import com.panda.pda.mes.common.data.DataParamType
import com.panda.pda.mes.databinding.FrameEmptyViewBinding
import com.panda.pda.mes.databinding.ItemAlarmHistoryBinding
import com.panda.pda.mes.operation.fms.data.AlarmApi
import com.panda.pda.mes.operation.fms.data.model.AlarmHistoryModel
import com.panda.pda.mes.operation.fms.data.model.AlarmStatus
import io.reactivex.rxjava3.core.Single

/**
 * created by AnJiwei 2021/8/31
 */
class AlarmHistoryFragment : CommonSearchListFragment<AlarmHistoryModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.tilSearchBar.visibility = View.GONE
    }

    override fun createAdapter(): CommonViewBindingAdapter<*, AlarmHistoryModel> {
        return object : CommonViewBindingAdapter<ItemAlarmHistoryBinding, AlarmHistoryModel>(
            mutableListOf()) {
            override fun createBinding(parent: ViewGroup): ItemAlarmHistoryBinding {
                return ItemAlarmHistoryBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            }

            override fun createEmptyViewBinding(parent: ViewGroup): ViewBinding {
                return FrameEmptyViewBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            }

            override fun onBindViewHolderWithData(
                holder: ViewBindingHolder,
                data: AlarmHistoryModel,
                position: Int,
            ) {
                holder.itemViewBinding.apply {
                    tvAlarmCode.text = data.alarmCode
                    tvAlarmStatus.text = data.alarmStatus.type
                    tvReporter.text = data.createName
                    tvReportTime.text = data.createTime
                    tvAlarmDetail.text = data.alarmDetail

                    tvExceptionType.text = CommonParameters.getDesc(DataParamType.ALARM_EXCEPTION_TYPE, data.exceptionTypes)
                    tvTagBreakdown.isVisible = data.breakdownFlag == 1
                    tvTagEmergency.isVisible = data.emergencyFlag == 1
                    tvSelectedEquipment.text = data.equipmentDesc

                    if (data.alarmStatus == AlarmStatus.Open) {
                        clClose.visibility = View.GONE
                    } else {
                        clClose.visibility = View.VISIBLE
                        tvCloser.text = data.closeName
                        tvCloseTime.text = data.closeTime
                        tvCloseDetail.text = data.closeRemark
                    }
                }
            }
        }
    }

    override fun api(key: String?): Single<DataListNode<AlarmHistoryModel>> =
        WebClient.request(AlarmApi::class.java)
            .pdaFmsAlarmMyHistoryGet()
            .map {
                DataListNode(it.dataList)
            }


    override val titleResId: Int
        get() = R.string.history_record

}