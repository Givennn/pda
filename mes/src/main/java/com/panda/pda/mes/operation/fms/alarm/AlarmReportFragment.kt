package com.panda.pda.mes.operation.fms.alarm

import android.os.Bundle
import android.view.View
import android.widget.TextView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.extension.toast
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.WheelPickerDialogFragment
import com.panda.pda.mes.common.data.CommonParameters
import com.panda.pda.mes.common.data.DataParamType
import com.panda.pda.mes.databinding.FragmentAlarmReportBinding
import com.panda.pda.mes.operation.fms.data.AlarmApi
import com.panda.pda.mes.operation.fms.data.model.AlarmDetailRequest
import com.panda.pda.mes.operation.qms.data.model.QualityInspectItemModel
import java.util.concurrent.TimeUnit

/**
 * created by AnJiwei 2021/8/30
 */
class AlarmReportFragment : BaseFragment(R.layout.fragment_alarm_report) {
    private val viewBinding by viewBinding<FragmentAlarmReportBinding>()

    private val requestAlarm = AlarmDetailRequest()

    private val exceptionTypeMap by lazy {
        CommonParameters.getParameters(DataParamType.EXCEPTION_TYPE)
    }
    private val exceptionTypeDialog by lazy {
        WheelPickerDialogFragment().also {
            it.pickerData = exceptionTypeMap.map { parameter -> parameter.paramDesc }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.apply {
            if (requestAlarm.exceptionTypes != null) {
                tvExceptionType.text = CommonParameters.getDesc(DataParamType.EXCEPTION_TYPE,
                    requestAlarm.exceptionTypes!!)

            }
            swIsTrouble.isChecked = requestAlarm.breakdownFlag == 1
            swIsEmergency.isChecked = requestAlarm.emergencyFlag == 1
            if (requestAlarm.alarmDetail != null) {
                etRemark.setText(requestAlarm.alarmDetail)
            }
            topAppBar.setNavigationOnClickListener {
                navBackListener.invoke(it)
            }
            swIsTrouble.setOnCheckedChangeListener { _, isChecked ->
                requestAlarm.breakdownFlag = if (isChecked) 1 else 2
            }

            swIsEmergency.setOnCheckedChangeListener { _, isChecked ->
                requestAlarm.emergencyFlag = if (isChecked) 1 else 2
            }

            llExceptionType.setOnClickListener {
                if (exceptionTypeDialog.pickerData.isEmpty()) {
                    toast(R.string.net_work_error)
                } else {
                    exceptionTypeDialog.show(parentFragmentManager, TAG)
                }
            }

            llEquipment.setOnClickListener {
                navToEquipmentSelect()
            }
            btnConfirm
                .clicks()
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe {
                    alarmReport()
                }
            btnShowAlarmHistory.clicks()
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe {
                    navToAlarmHistory()
                }
        }

        exceptionTypeDialog.setConfirmButton { result ->
            if (result != null) {
                val selectedException = exceptionTypeMap.getOrNull(result.second)
                if (selectedException != null) {
                    requestAlarm.exceptionTypes = selectedException.paramValue
                    viewBinding.tvExceptionType.text = selectedException.paramDesc
                }
            }
        }
    }

    private fun navToEquipmentSelect() {


    }

    private fun navToAlarmHistory() {
        navController.navigate(R.id.action_alarmReportFragment_to_alarmHistoryFragment)
    }

    private fun alarmReport() {
        val alarmDetail = viewBinding.etRemark.text.toString()
        if (alarmDetail.isEmpty()) {
            toast(getString(R.string.alarm_detail_empty_message))
            return
        }
        requestAlarm.alarmDetail = alarmDetail
        WebClient.request(AlarmApi::class.java)
            .pdaFmsAlarmAddPost(requestAlarm)
            .bindToFragment()
            .subscribe({
                toast(getString(R.string.alarm_report_success_message))
                navBackListener.invoke(requireView())
            }, {})
    }
}