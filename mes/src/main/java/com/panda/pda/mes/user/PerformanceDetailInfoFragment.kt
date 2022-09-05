package com.panda.pda.mes.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.common.ModelPropertyCreator
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.databinding.FragmentPerformanceDetailInfoBinding
import com.panda.pda.mes.databinding.ItemPerformanceRankBinding
import com.panda.pda.mes.databinding.ItemSkillInfoBinding
import com.panda.pda.mes.databinding.ItemWorkHourInfoBinding
import com.panda.pda.mes.discovery.data.DispatchOrderDetailDiscoveryModel
import com.panda.pda.mes.user.data.model.*

/**
 * created by AnJiwei 2022/9/2
 */
class PerformanceDetailInfoFragment : BaseFragment(R.layout.fragment_performance_detail_info) {

    private lateinit var workHourAdapter: CommonViewBindingAdapter<ItemWorkHourInfoBinding, WorkHourModel>
    private lateinit var skillInfoAdapter: CommonViewBindingAdapter<ItemSkillInfoBinding, SkillItemModel>
    private lateinit var detailInfo: PerformanceDetailInfoModel
    private lateinit var modelProperty: ModelPropertyCreator<DetailInfo>
    private val viewBinding by viewBinding<FragmentPerformanceDetailInfoBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        modelProperty = ModelPropertyCreator(
            DetailInfo::class.java,
            viewBinding.llPropertyInfo,
        )
        modelProperty.setData(detailInfo.detailInfo)

        viewBinding.rvPerformanceRank.adapter = object :
            CommonViewBindingAdapter<ItemPerformanceRankBinding, Item>(detailInfo.itemList.toMutableList()) {
            override fun createBinding(parent: ViewGroup): ItemPerformanceRankBinding {
                return ItemPerformanceRankBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            }

            override fun onBindViewHolderWithData(
                holder: ViewBindingHolder,
                data: Item,
                position: Int,
            ) {
                holder.itemViewBinding.apply {
                    tvPerformanceType.text = "绩效指标：${data.performanceItemName}"
                    tvPerformanceValue.text = "绩效指标值: ${data.performanceValue}"
                    tvPerformanceWeight.text = "权重：${data.performanceWeight}"
                    tvPerformanceAmount.text = "绩效（元）：${data.performanceAmount}"
                }
            }
        }

        viewBinding.tvPerformanceRankTotal.text = "总计（元）${detailInfo.itemList.sumOf { it.performanceAmount.toDouble() }.toBigDecimal().toPlainString()}"

        viewBinding.tvHourlyWage.text = "时薪（元）：${detailInfo.detailInfo.hourlyWage.toBigDecimal().toPlainString()}"

        skillInfoAdapter =
            object : CommonViewBindingAdapter<ItemSkillInfoBinding, SkillItemModel>() {
                override fun createBinding(parent: ViewGroup): ItemSkillInfoBinding {
                    return ItemSkillInfoBinding.inflate(LayoutInflater.from(parent.context),
                        parent,
                        false)
                }

                override fun onBindViewHolderWithData(
                    holder: ViewBindingHolder,
                    data: SkillItemModel,
                    position: Int,
                ) {

                    holder.itemViewBinding.apply {
                        tvSkillName.text = "技能：${data.skillName}"
                        tvLevel.text = "等级：${data.skillLevel}"
                        tvRank.text = "绩效分：${data.performancePoints}"
                    }
                }

            }

        workHourAdapter =
            object : CommonViewBindingAdapter<ItemWorkHourInfoBinding, WorkHourModel>() {
                override fun createBinding(parent: ViewGroup): ItemWorkHourInfoBinding {
                    return ItemWorkHourInfoBinding.inflate(LayoutInflater.from(parent.context),
                        parent,
                        false)
                }

                override fun onBindViewHolderWithData(
                    holder: ViewBindingHolder,
                    data: WorkHourModel,
                    position: Int,
                ) {
                    holder.itemViewBinding.apply {
                        tvMainPlanCode.text = "主计划编号：${data.planNo}"
                        tvWorkOrderCode.text = "工单编号：${data.workOrderCode}"
                        tvDispatchOrderCode.text = "绩效分：${data.dispatchOrderCode}"
                        tvDispatchOrderDesc.text = "技能：${data.dispatchOrderDesc}"
                        tvWorkHour.text = "等级：${data.workTimeHour}"
                        tvRank.text = "绩效分：${data.workTimePerformance}"
                    }
                }
            }
        viewBinding.rvSkillInfo.adapter = skillInfoAdapter
        viewBinding.rvWorkHourInfo.adapter = workHourAdapter
    }

    fun setDetailInfo(detail: PerformanceDetailInfoModel) {
        detailInfo = detail
    }

    fun setWorkHourData(workHourData: List<WorkHourModel>) {
        workHourAdapter.refreshData(workHourData)
        viewBinding.tvWorkHourTotal.text = "总计：${workHourData.sumOf { it.workTimePerformance }.toString()}"
    }

    fun setSkillInfoData(skillItemData: List<SkillItemModel>) {
        skillInfoAdapter.refreshData(skillItemData)
        viewBinding.tvSkillInfoTotal.text = "总计：${skillItemData.sumOf { it.performancePoints}}"
    }
}