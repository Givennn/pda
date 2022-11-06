package com.panda.pda.mes.operation.bps.report_history

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.databinding.FragmentReportHistoryReportInfoBinding
import com.panda.pda.mes.databinding.ItemMainPlanReportInfoBinding
import com.panda.pda.mes.operation.bps.data.model.MainPlanReportHistoryModel
import com.panda.pda.mes.operation.bps.data.model.MainPlanReportModel

/**
 * created by AnJiwei 2022/11/1
 */
class ReportInfoFragment(
    private val detail: MainPlanReportHistoryModel?,
) : BaseFragment(R.layout.fragment_report_history_report_info) {

    private val viewBinding by viewBinding<FragmentReportHistoryReportInfoBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (detail != null) {

            viewBinding.rvTaskList.adapter = object :
                CommonViewBindingAdapter<ItemMainPlanReportInfoBinding, MainPlanReportModel>(detail.reportList.toMutableList()) {
                override fun createBinding(parent: ViewGroup): ItemMainPlanReportInfoBinding {
                    return ItemMainPlanReportInfoBinding.inflate(LayoutInflater.from(parent.context),
                        parent,
                        false)
                }

                @SuppressLint("SetTextI18n")
                override fun onBindViewHolderWithData(
                    holder: ViewBindingHolder,
                    data: MainPlanReportModel,
                    position: Int,
                ) {
                    holder.itemViewBinding.apply {
                        tvReportNumber.text = "工序报工数量: ${data.reportNumber}"
                        tvOperator.text = "操作工：${data.jockeyNameStr}"
                        tvDevice.text = "设备：${data.equipmentStr}"
                        tvPersonWorkHour.text = "人员工时（小时）：${data.reportTime}"
                        tvDeviceWorkHour.text = "设备工时（小时）：${data.equRealReportTime}"
                    }
                }

            }
        }
    }

}