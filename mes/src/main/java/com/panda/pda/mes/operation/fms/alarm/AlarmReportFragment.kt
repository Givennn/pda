package com.panda.pda.mes.operation.fms.alarm

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.extension.toast
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.databinding.FragmentAlarmReportBinding
import com.panda.pda.mes.operation.fms.data.AlarmApi
import com.panda.pda.mes.operation.fms.data.model.AlarmDetailRequest
import java.util.concurrent.TimeUnit

/**
 * created by AnJiwei 2021/8/30
 */
class AlarmReportFragment : BaseFragment(R.layout.fragment_alarm_report) {
    private val viewBinding by viewBinding<FragmentAlarmReportBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.apply {
            topAppBar.setNavigationOnClickListener {
                navBackListener.invoke(it)
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
        WebClient.request(AlarmApi::class.java)
            .pdaFmsAlarmAddPost(AlarmDetailRequest(alarmDetail))
            .bindToFragment()
            .subscribe({
                toast(getString(R.string.alarm_report_success_message))
                navBackListener.invoke(requireView())
            }, {})
    }
}