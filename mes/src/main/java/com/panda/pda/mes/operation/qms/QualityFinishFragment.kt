package com.panda.pda.mes.operation.qms

import android.view.LayoutInflater
import android.view.ViewGroup
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.mes.R
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.common.data.model.IdRequest
import com.panda.pda.mes.databinding.ItemQualityFinishBinding
import com.panda.pda.mes.operation.qms.data.QualityApi
import com.panda.pda.mes.operation.qms.data.model.QualityTaskModel
import com.panda.pda.mes.operation.qms.data.model.QualityTaskModelType
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import java.util.concurrent.TimeUnit

class QualityFinishFragment : BaseQualitySearchListFragment<ItemQualityFinishBinding>() {
    override val qualityTaskModelType: QualityTaskModelType
        get() = QualityTaskModelType.Finish

    override fun createViewBinding(parent: ViewGroup): ItemQualityFinishBinding {
        return ItemQualityFinishBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun onBindViewHolder(
        holder: CommonViewBindingAdapter<ItemQualityFinishBinding, QualityTaskModel>.ViewBindingHolder,
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
                getString(R.string.desc_and_code_formatter, data.dispatchOrderDesc, data.dispatchOrderCode)
            tvPlanDateSection.text = getString(
                R.string.time_section_formatter,
                data.planStartTime,
                data.planEndTime
            )
            tvQualityNumber.text = data.qualityNum.toString()
            tvQualityScheme.text = data.qualitySolutionName

            btnActionFinish.clicks()
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .bindToLifecycle(holder.itemView)
                .subscribe {
                    showActionRequestDialog(
                        WebClient.request(QualityApi::class.java).pdaQmsQualityTaskFinishPost(
                            IdRequest(data.id)
                        ),
                        getString(
                            R.string.quality_task_finish_confirm,
                            getString(
                                R.string.desc_and_code_formatter,
                                data.qualityDesc,
                                data.qualityCode
                            )
                        ),
                        getString(R.string.quality_task_finish_success)
                    )
                }

        }
    }
}