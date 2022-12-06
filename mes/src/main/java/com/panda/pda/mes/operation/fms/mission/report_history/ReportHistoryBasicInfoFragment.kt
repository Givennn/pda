package com.panda.pda.mes.operation.fms.mission.report_history

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.common.ModelPropertyCreator
import com.panda.pda.mes.databinding.FragmentReportHistoryBasicInfoBinding
import com.panda.pda.mes.operation.bps.data.model.MainPlanReportHistoryModel
import com.panda.pda.mes.operation.fms.data.model.DispatchOrderReportHistoryModel
import com.panda.pda.mes.operation.fms.data.model.TaskDetailModel

/**
 * created by AnJiwei 2022/11/30
 */
class ReportHistoryBasicInfoFragment(
    private val detail: TaskDetailModel?,
    private val reportHistory: DispatchOrderReportHistoryModel?
) : BaseFragment(R.layout.fragment_report_history_basic_info) {

    private val viewBinding by viewBinding<FragmentReportHistoryBasicInfoBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (reportHistory != null) {
            ModelPropertyCreator(
                DispatchOrderReportHistoryModel::class.java,
                viewBinding.llPropertyInfo
            ).apply { setData(reportHistory) }
            viewBinding.tvRemark.text =reportHistory.remark
        }

        if (detail != null) {
            ModelPropertyCreator(
                TaskDetailModel::class.java,
                viewBinding.llPropertyInfo,
            ).apply {
                setData(detail)
            }
        }
    }
}