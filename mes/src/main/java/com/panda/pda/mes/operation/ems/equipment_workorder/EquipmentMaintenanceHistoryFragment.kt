package com.panda.pda.mes.operation.ems.equipment_workorder

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
import com.panda.pda.mes.databinding.FragmentEquipmentWorkorderMaintenanceHistoryBinding
import com.panda.pda.mes.databinding.FragmentQualityDetailBinding
import com.panda.pda.mes.operation.qms.data.model.QualityDetailModel
import com.panda.pda.mes.operation.qms.data.model.QualitySubTaskDetailModel
import com.panda.pda.mes.operation.qms.data.model.QualityTaskRecordModel
import com.squareup.moshi.Types

/**
 * 维保记录，集成维修列表与保养列表两套fragment
 */
class EquipmentMaintenanceHistoryFragment :
    BaseFragment(R.layout.fragment_equipment_workorder_maintenance_history) {
    private val viewBinding by viewBinding<FragmentEquipmentWorkorderMaintenanceHistoryBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }
        //设备id
        val facilityId = arguments?.getString("facilityId") ?: ""
        //设备类型
        val facilityType = arguments?.getString("facilityType") ?: ""
        viewBinding.apply {
            viewPage.adapter = object : FragmentStateAdapter(childFragmentManager, lifecycle) {
                override fun getItemCount(): Int {
                    return 2
                }

                override fun createFragment(position: Int): Fragment {
                    return when (position) {
                        //维修记录列表
                        0 -> EquipmentMaintenanceHistoryListFragment(facilityId, facilityType)
                        //保养记录列表
                        1 -> EquipmentUpkeepHistoryListFragment(facilityId, facilityType)
                        else -> throw IndexOutOfBoundsException()
                    }
                }
            }
            TabLayoutMediator(tabLayout, viewPage) { tab, position ->
                tab.text = when (position) {
                    0 -> "维修记录"
                    1 -> "保养记录"
                    else -> ""
                }
            }.attach()
        }
    }
}