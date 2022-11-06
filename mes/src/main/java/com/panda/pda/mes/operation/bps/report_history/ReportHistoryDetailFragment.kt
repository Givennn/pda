package com.panda.pda.mes.operation.bps.report_history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.extension.getStringObject
import com.panda.pda.mes.databinding.FragmentQualityDetailBinding
import com.panda.pda.mes.operation.bps.data.model.MainPlanReportHistoryModel

/**
 * created by AnJiwei 2022/11/1
 */
class ReportHistoryDetailFragment : BaseFragment(R.layout.fragment_quality_detail) {
    private val viewBinding by viewBinding<FragmentQualityDetailBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }
        viewBinding.topAppBar.title = getString(R.string.main_plan_report_detail)

        val detail = arguments?.getStringObject<MainPlanReportHistoryModel>()

        viewBinding.apply {
            viewPage.adapter = object : FragmentStateAdapter(parentFragmentManager, lifecycle) {
                override fun getItemCount(): Int {
                    return 3
                }

                override fun createFragment(position: Int): Fragment {
                    return when (position) {
                        0 -> BasicInfoFragment(detail)
                        1 -> ReportInfoFragment(detail)
                        2 -> PhotosFragment(detail)
                        else -> throw IndexOutOfBoundsException()
                    }
                }

            }
            TabLayoutMediator(tabLayout, viewPage) { tab, position ->
                tab.text = when (position) {
                    0 -> getString(R.string.basic_info)
                    1 -> getString(R.string.report_info)
                    2 -> getString(R.string.photo)
                    else -> ""
                }
            }.attach()
        }
    }
}