package com.panda.pda.app.operation.qms.quality_task_detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.databinding.FragmentQualityDetailBinding

/**
 * created by AnJiwei 2021/9/28
 */
class QualityDetailFragment : BaseFragment(R.layout.fragment_quality_detail) {
    private val viewBinding by viewBinding<FragmentQualityDetailBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }
        viewBinding.apply {
            viewPage.adapter = object : FragmentStateAdapter(parentFragmentManager, lifecycle) {
                override fun getItemCount(): Int {
                    return 2
                }

                override fun createFragment(position: Int): Fragment {
                    return when (position) {
                        0 -> QualityDetailInfoFragment()
                        1 -> QualityDetailRecordFragment()
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