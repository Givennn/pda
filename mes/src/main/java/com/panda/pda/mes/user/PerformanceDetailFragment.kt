package com.panda.pda.mes.user

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
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.data.model.CommonOperationRecordModel
import com.panda.pda.mes.databinding.FragmentQualityDetailBinding
import com.panda.pda.mes.discovery.DispatchOrderDetailInfoFragment
import com.panda.pda.mes.discovery.data.DispatchOrderDetailDiscoveryModel
import com.panda.pda.mes.operation.bps.MainPlanDetailRecordFragment
import com.panda.pda.mes.user.data.UserApi
import com.panda.pda.mes.user.data.model.PerformanceDetailInfoModel
import com.squareup.moshi.Types

/**
 * created by AnJiwei 2022/9/2
 */
class PerformanceDetailFragment : BaseFragment(R.layout.fragment_quality_detail) {
    private val viewBinding by viewBinding<FragmentQualityDetailBinding>()

    private val performanceDetailInfoFragment = PerformanceDetailInfoFragment()

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
        val detail = arguments?.getStringObject<PerformanceDetailInfoModel>() ?: return
        performanceDetailInfoFragment.setDetailInfo(detail)

        WebClient.request(UserApi::class.java)
            .pdaSkillItemListGet(detail.detailInfo.id)
            .bindToFragment()
            .subscribe({
                performanceDetailInfoFragment.setSkillInfoData(it.dataList)
            }, {})

        WebClient.request(UserApi::class.java)
            .pdaWorkHourListGet(detail.detailInfo.id)
            .bindToFragment()
            .subscribe({ performanceDetailInfoFragment.setWorkHourData(it.dataList) }, {})
        viewBinding.apply {
            viewPage.adapter = object : FragmentStateAdapter(parentFragmentManager, lifecycle) {
                override fun getItemCount(): Int {
                    return 2
                }

                override fun createFragment(position: Int): Fragment {
                    return when (position) {
                        0 -> performanceDetailInfoFragment
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