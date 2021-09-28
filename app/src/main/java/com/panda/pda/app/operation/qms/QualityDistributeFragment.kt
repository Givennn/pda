package com.panda.pda.app.operation.qms

import android.view.LayoutInflater
import android.view.ViewGroup
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.app.R
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.common.adapter.ViewBindingAdapter
import com.panda.pda.app.common.data.model.IdRequest
import com.panda.pda.app.databinding.ItemQualityDistributeBinding
import com.panda.pda.app.operation.qms.data.QualityApi
import com.panda.pda.app.operation.qms.data.model.QualityTaskModel
import com.panda.pda.app.operation.qms.data.model.QualityTaskModelType
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import java.util.concurrent.TimeUnit

class QualityDistributeFragment : BaseQualitySearchListFragment<ItemQualityDistributeBinding>() {
    override val qualityTaskModelType: QualityTaskModelType
        get() = QualityTaskModelType.Distribute

    override fun createViewBinding(parent: ViewGroup): ItemQualityDistributeBinding {
        return ItemQualityDistributeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    }

    override fun onBindViewHolder(
        holder: ViewBindingAdapter<ItemQualityDistributeBinding, QualityTaskModel>.ViewBindingHolder,
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
            btnActionDistribute.clicks()
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .bindToLifecycle(holder.itemView)
                .subscribe { distribute(data) }

            btnActionTransfer.clicks()
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .bindToLifecycle(holder.itemView)
                .subscribe { transfer(data) }

            btnActionBackOut.clicks()
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe {
                    showActionRequestDialog(
                        WebClient.request(QualityApi::class.java).qualityDistributeCancel(
                            IdRequest(data.id)
                        ),
                        getString(
                            R.string.quality_task_back_out_confirm,
                            getString(
                                R.string.desc_and_code_formatter,
                                data.qualityDesc,
                                data.qualityCode
                            )
                        ),
                        getString(R.string.quality_task_back_out_success)
                    )
                }

        }
    }

    //转办
    private fun transfer(data: QualityTaskModel) {
        TODO("Not yet implemented")
    }

    private fun distribute(data: QualityTaskModel) {
        TODO("Not yet implemented")
    }
}