package com.panda.pda.mes.operation.bps.report_history

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.common.ModelPropertyCreator
import com.panda.pda.mes.databinding.FragmentReportHistoryBasicInfoBinding
import com.panda.pda.mes.operation.bps.data.model.MainPlanReportDetailModel

/**
 * created by AnJiwei 2022/11/1
 */
class BasicInfoFragment(
    private val detail: MainPlanReportDetailModel?,
) : BaseFragment(R.layout.fragment_report_history_basic_info) {

    private val viewBinding by viewBinding<FragmentReportHistoryBasicInfoBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val modelProperty = ModelPropertyCreator(
            MainPlanReportDetailModel::class.java,
            viewBinding.llPropertyInfo,
        )
        if (detail != null) {
            modelProperty.setData(detail)
            viewBinding.tvRemark.text = detail.remark
        }
    }
}