package com.panda.pda.app.operation.qms

import android.view.LayoutInflater
import android.view.ViewGroup
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.app.R
import com.panda.pda.app.common.adapter.ViewBindingAdapter
import com.panda.pda.app.databinding.ItemQualityReviewBinding
import com.panda.pda.app.operation.qms.data.model.QualityTaskModel
import com.panda.pda.app.operation.qms.data.model.QualityTaskModelType
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import java.util.concurrent.TimeUnit

class QualityReviewFragment : BaseQualitySearchListFragment<ItemQualityReviewBinding>() {
    override val qualityTaskModelType: QualityTaskModelType
        get() = QualityTaskModelType.Review

    override fun createViewBinding(parent: ViewGroup): ItemQualityReviewBinding {
        return ItemQualityReviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    }

    override fun onBindViewHolder(
        holder: ViewBindingAdapter<ItemQualityReviewBinding, QualityTaskModel>.ViewBindingHolder,
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
                        getString(R.string.desc_and_code_formatter, data.taskDesc, data.taskCode)
                    tvPlanDateSection.text = getString(
                        R.string.time_section_formatter,
                        data.planStartTime,
                        data.planEndTime
                    )
                    tvQualityNumber.text = data.qualityNum.toString()
                    tvInspectNumber.text = data.deliverNum.toString()
                    tvQualityScheme.text = data.qualitySolutionName

                    btnActionTransfer.clicks()
                        .throttleFirst(500, TimeUnit.MILLISECONDS)
                        .bindToLifecycle(holder.itemView)
                        .subscribe { transfer(data) }

                    btnActionReview.clicks()
                        .throttleFirst(500, TimeUnit.MILLISECONDS)
                        .bindToLifecycle(holder.itemView)
                        .subscribe { review(data) }
                }
    }

    private fun review(data: QualityTaskModel) {
        TODO("Not yet implemented")
    }

    private fun transfer(data: QualityTaskModel) {
        TODO("Not yet implemented")
    }

}