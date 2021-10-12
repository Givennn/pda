package com.panda.pda.app.operation.qms

import android.view.LayoutInflater
import android.view.ViewGroup
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.app.R
import com.panda.pda.app.common.adapter.CommonViewBindingAdapter
import com.panda.pda.app.databinding.ItemQualityExecuteBinding
import com.panda.pda.app.operation.qms.data.model.QualityTaskModel
import com.panda.pda.app.operation.qms.data.model.QualityTaskModelType
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import java.util.concurrent.TimeUnit

class QualityExecuteFragment : BaseQualitySearchListFragment<ItemQualityExecuteBinding>() {
    override val qualityTaskModelType: QualityTaskModelType
        get() = QualityTaskModelType.Execute

    override fun createViewBinding(parent: ViewGroup): ItemQualityExecuteBinding {
        return ItemQualityExecuteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun onBindViewHolder(
        holder: CommonViewBindingAdapter<ItemQualityExecuteBinding, QualityTaskModel>.ViewBindingHolder,
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
            tvQualityScheme.text = data.qualitySolutionName

            btnActionExecute.clicks()
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .bindToLifecycle(holder.itemView)
                .subscribe {
                    TODO("执行api未定义")
                }
        }
    }
}

