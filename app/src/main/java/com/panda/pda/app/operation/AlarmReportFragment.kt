package com.panda.pda.app.operation

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.base.extension.toast
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.databinding.FragmentAlarmReportBinding
import com.panda.pda.app.operation.data.AlarmApi
import com.panda.pda.app.operation.data.model.AlarmDetailRequest

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
            btnConfirm.setOnClickListener {
                alarmReport()
            }

            btnShowAlarmHistory.setOnClickListener {
                navToAlarmHistory()
            }
        }

    }

    private fun navToAlarmHistory() {
        navController.navigate(R.id.action_alarmReportFragment_to_alarmHistoryFragment)
    }

    private fun alarmReport() {
        val alarmDetail = viewBinding.tvAlarmDetail.text.toString()
        if (alarmDetail.isEmpty()) {
            toast(getString(R.string.alarm_detail_empty_message))
            return
        }
        WebClient.request(AlarmApi::class.java)
            .pdaFmsAlarmAddPost(AlarmDetailRequest(alarmDetail))
            .bindToFragment()
            .subscribe ({
                toast(getString(R.string.alarm_report_success_message))
                navBackListener.invoke(requireView())
            }, {})
    }
}