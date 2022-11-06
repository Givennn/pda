package com.panda.pda.mes.discovery

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.common.ModelPropertyCreator
import com.panda.pda.mes.databinding.FragmentQualityDetailInfoBinding
import com.panda.pda.mes.discovery.data.DispatchOrderDetailDiscoveryModel
import com.panda.pda.mes.operation.bps.data.model.MainPlanDetailModel

/**
 * created by AnJiwei 2022/8/31
 */
class DispatchOrderDetailInfoFragment(private val detail: DispatchOrderDetailDiscoveryModel?,
) : BaseFragment(R.layout.fragment_quality_detail_info) {

    private val viewBinding by viewBinding<FragmentQualityDetailInfoBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val modelProperty = ModelPropertyCreator(
            DispatchOrderDetailDiscoveryModel::class.java,
            viewBinding.llPropertyInfo,
        )
        if (detail != null) {
            modelProperty.setData(detail)
        }
    }
}