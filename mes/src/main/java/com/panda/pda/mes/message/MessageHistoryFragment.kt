package com.panda.pda.mes.message

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.viewpager2.adapter.FragmentStateAdapter
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.extension.getGenericObjectString
import com.panda.pda.mes.base.extension.getStringObject
import com.panda.pda.mes.base.retrofit.DataListNode
import com.panda.pda.mes.databinding.FragmentEquipmentWorkorderMaintenanceHistoryBinding
import com.panda.pda.mes.databinding.FragmentMessageHistoryBinding
import com.panda.pda.mes.databinding.FragmentQualityDetailBinding
import com.panda.pda.mes.operation.ems.equipment_workorder.EquipmentMaintenanceHistoryListFragment
import com.panda.pda.mes.operation.ems.equipment_workorder.EquipmentUpkeepHistoryListFragment
import com.panda.pda.mes.operation.qms.data.model.QualityDetailModel
import com.panda.pda.mes.operation.qms.data.model.QualitySubTaskDetailModel
import com.panda.pda.mes.operation.qms.data.model.QualityTaskRecordModel
import com.squareup.moshi.Types
import timber.log.Timber

/**
 * Author:yinzhilin
 * Date: 2023/1/10
 * Desc:消息列表，包括已读与未读两个子fragment
 */
class MessageHistoryFragment :
    BaseFragment(R.layout.fragment_message_history) {
    private val viewBinding by viewBinding<FragmentMessageHistoryBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.e("history onViewCreated")
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }
        //设备id
        val facilityId = arguments?.getString("facilityId") ?: ""
        //设备类型
        val facilityType = arguments?.getString("facilityType") ?: ""
        val messageUnreadListFragment = MessageUnreadListFragment(0)
        val messageReadListFragment = MessageUnreadListFragment(1)
        viewBinding.apply {
            viewPage.adapter = object : FragmentStateAdapter(childFragmentManager, lifecycle) {
                override fun getItemCount(): Int {
                    return 2
                }

                override fun createFragment(position: Int): Fragment {
                    return when (position) {
                        //维修记录列表
                        0 -> messageUnreadListFragment
                        //保养记录列表
                        1 -> messageReadListFragment
                        else -> throw IndexOutOfBoundsException()
                    }
                }
            }
            TabLayoutMediator(tabLayout, viewPage) { tab, position ->
                tab.text = when (position) {
                    0 -> "未读"
                    1 -> "已读"
                    else -> ""
                }
            }.attach()
        }
    }

}