package com.panda.pda.mes.operation.qms.quality_task_detail

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
import com.panda.pda.mes.databinding.FragmentQualityDetailBinding
import com.panda.pda.mes.operation.qms.data.model.QualityDetailModel
import com.panda.pda.mes.operation.qms.data.model.QualitySubTaskDetailModel
import com.panda.pda.mes.operation.qms.data.model.QualityTaskRecordModel
import com.squareup.moshi.Types

/**
 * created by AnJiwei 2021/9/28
 */
class QualityDetailFragment : BaseFragment(R.layout.fragment_quality_detail) {
    private val viewBinding by viewBinding<FragmentQualityDetailBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }
        val recordList = arguments?.getGenericObjectString<DataListNode<QualityTaskRecordModel>>(
            Types.newParameterizedType(
                DataListNode::class.java,
                QualityTaskRecordModel::class.java
            )
        )
        val subTask = arguments?.getStringObject<QualitySubTaskDetailModel>()
        val task = arguments?.getStringObject<QualityDetailModel>()

        viewBinding.apply {
            viewPage.adapter = object : FragmentStateAdapter(parentFragmentManager, lifecycle) {
                override fun getItemCount(): Int {
                    return 2
                }

                override fun createFragment(position: Int): Fragment {
                    return when (position) {
                        0 -> QualityDetailInfoFragment(subTask, task)
                        1 -> QualityDetailRecordFragment(recordList)
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