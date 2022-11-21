package com.panda.pda.mes.operation.qms.ng_product_deall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.panda.pda.mes.R
import com.panda.pda.mes.base.extension.putObjectString
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.databinding.ItemNgProductDealTaskBinding
import com.panda.pda.mes.operation.qms.BaseQualitySearchListFragment
import com.panda.pda.mes.operation.qms.data.model.QualityTaskModel
import com.panda.pda.mes.operation.qms.data.model.QualityTaskModelType

/**
 * created by AnJiwei 2022/10/10
 */
class NGProductDealFragment : BaseQualitySearchListFragment<ItemNgProductDealTaskBinding>() {

    override val qualityTaskModelType: QualityTaskModelType
        get() = QualityTaskModelType.Task

    override fun createViewBinding(parent: ViewGroup): ItemNgProductDealTaskBinding {
        return ItemNgProductDealTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    }

    override fun onBindViewHolder(
        holder: CommonViewBindingAdapter<ItemNgProductDealTaskBinding, QualityTaskModel>.ViewBindingHolder,
        data: QualityTaskModel,
        position: Int
    ) {
        holder.itemViewBinding.apply {
            tvQualityInfo.text = getString(
                R.string.desc_and_code_formatter,
                data.qualityDesc,
                data.qualityCode
            )
            tvTaskInfo.text =
                getString(R.string.desc_and_code_formatter, data.productName, data.productCode)
            if (!data.planStartTime.isNullOrEmpty() && !data.planEndTime.isNullOrEmpty()) {
                tvPlanDateSection.text = getString(
                    R.string.time_section_formatter,
                    data.planStartTime,
                    data.planEndTime
                )
            }
            tvNgNumber.text = data.unqualifiedNum.toString()
            tvDealNumber.text = data.temporaryControlNum.toString()
            tvQualityScheme.text = data.qualitySolutionName

            btnActionDeal.setOnClickListener {
                navToNgListFragment(data)
            }
        }
    }

    private fun navToNgListFragment(data: QualityTaskModel) {

        navController.navigate(R.id.action_NGProductDealFragment_to_NGProductListFragment, Bundle().apply {
            putObjectString(data)
        })
    }

    override val titleResId: Int
        get() = R.string.ng_product_deal
}