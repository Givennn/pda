package com.panda.pda.mes.discovery

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.extension.getGenericObjectString
import com.panda.pda.mes.base.extension.getStringObject
import com.panda.pda.mes.base.retrofit.DataListNode
import com.panda.pda.mes.common.data.model.CommonOperationRecordModel
import com.panda.pda.mes.databinding.FragmentQualityDetailBinding
import com.panda.pda.mes.discovery.data.DispatchOrderDetailDiscoveryModel
import com.panda.pda.mes.operation.bps.MainPlanDetailRecordFragment
import com.squareup.moshi.Types

/**
 * created by AnJiwei 2022/8/31
 */
class DispatchOrderDetailFragment: BaseFragment(R.layout.fragment_quality_detail) {
    private val viewBinding by viewBinding<FragmentQualityDetailBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }
        viewBinding.topAppBar.title = getString(R.string.dispatch_order_detail)
        val recordList =
            arguments?.getGenericObjectString<DataListNode<CommonOperationRecordModel>>(
                Types.newParameterizedType(
                    DataListNode::class.java,
                    CommonOperationRecordModel::class.java
                )
            )
        val detail = arguments?.getStringObject<DispatchOrderDetailDiscoveryModel>()

        viewBinding.apply {
            viewPage.adapter = object : FragmentStateAdapter(parentFragmentManager, lifecycle) {
                override fun getItemCount(): Int {
                    return 2
                }

                override fun createFragment(position: Int): Fragment {
                    return when (position) {
                        0 -> DispatchOrderDetailInfoFragment(detail)
                        1 -> MainPlanDetailRecordFragment(recordList)
                        else -> throw IndexOutOfBoundsException()
                    }
                }

            }
            TabLayoutMediator(tabLayout, viewPage) { tab, position ->
                tab.text = when (position) {
                    0 -> getString(R.string.basic_info)
                    1 -> getString(R.string.operate_record)
                    else -> ""
                }
            }.attach()
        }
    }
}