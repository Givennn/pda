package com.panda.pda.app.operation.qms

import android.view.LayoutInflater
import android.view.ViewGroup
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.app.R
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.common.adapter.ViewBindingAdapter
import com.panda.pda.app.common.data.model.IdRequest
import com.panda.pda.app.databinding.ItemQualityTaskBinding
import com.panda.pda.app.operation.qms.data.QualityApi
import com.panda.pda.app.operation.qms.data.model.QualityTaskModel
import com.panda.pda.app.operation.qms.data.model.QualityTaskModelType
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import java.util.concurrent.TimeUnit

class QualityTaskFragment : BaseQualitySearchListFragment<ItemQualityTaskBinding>() {
    override val qualityTaskModelType: QualityTaskModelType
        get() = QualityTaskModelType.Task

    override fun createViewBinding(parent: ViewGroup): ItemQualityTaskBinding {
        return ItemQualityTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    }

    override fun onBindViewHolder(
        holder: ViewBindingAdapter<ItemQualityTaskBinding, QualityTaskModel>.ViewBindingHolder,
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

            btnActionCommit.clicks()
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .bindToLifecycle(holder.itemView)
                .subscribe { commit(data) }

            btnActionReceive.clicks()
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .bindToLifecycle(holder.itemView)
                .subscribe {
                    showActionRequestDialog(
                        WebClient.request(QualityApi::class.java)
                            .pdaQmsTaskRevicePost(IdRequest(data.id)),
                        getString(
                            R.string.quality_task_receive_confirm, getString(
                                R.string.desc_and_code_formatter,
                                data.qualityDesc,
                                data.qualityCode
                            )
                        ),
                        getString(R.string.quality_task_receive_success)
                    )
                }
        }
    }

    private fun commit(data: QualityTaskModel) {
        TODO("Not yet implemented")
    }
}