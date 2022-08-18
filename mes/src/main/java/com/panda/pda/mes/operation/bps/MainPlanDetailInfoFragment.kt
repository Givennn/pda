package com.panda.pda.mes.operation.bps

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.common.ModelPropertyCreator
import com.panda.pda.mes.databinding.FragmentQualityDetailInfoBinding
import com.panda.pda.mes.operation.bps.data.model.MainPlanDetailModel
import com.panda.pda.mes.operation.qms.data.model.QualityDetailModel
import com.panda.pda.mes.operation.qms.data.model.QualitySubTaskDetailModel

/**
 * created by AnJiwei 2022/8/8
 */
class MainPlanDetailInfoFragment(
    private val detail: MainPlanDetailModel?,
) : BaseFragment(R.layout.fragment_quality_detail_info) {

    private val viewBinding by viewBinding<FragmentQualityDetailInfoBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val modelProperty = ModelPropertyCreator(
            MainPlanDetailModel::class.java,
            viewBinding.llPropertyInfo,
        )
        if (detail != null) {
            modelProperty.setData(detail)
        }
    }
}