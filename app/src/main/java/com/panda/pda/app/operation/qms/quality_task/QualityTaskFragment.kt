package com.panda.pda.app.operation.qms.quality_task

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.app.R
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.common.adapter.CommonViewBindingAdapter
import com.panda.pda.app.common.data.model.IdRequest
import com.panda.pda.app.databinding.ItemQualityTaskBinding
import com.panda.pda.app.operation.qms.BaseQualitySearchListFragment
import com.panda.pda.app.operation.qms.QualityViewModel
import com.panda.pda.app.operation.qms.data.QualityApi
import com.panda.pda.app.operation.qms.data.model.QualityTaskModel
import com.panda.pda.app.operation.qms.data.model.QualityTaskModelType
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import java.util.concurrent.TimeUnit

class QualityTaskFragment : BaseQualitySearchListFragment<ItemQualityTaskBinding>() {
    override val qualityTaskModelType: QualityTaskModelType
        get() = QualityTaskModelType.Task

    private val viewModel by activityViewModels<QualityViewModel>()

    override fun createViewBinding(parent: ViewGroup): ItemQualityTaskBinding {
        return ItemQualityTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    }

    override fun onBindViewHolder(
        holder: CommonViewBindingAdapter<ItemQualityTaskBinding, QualityTaskModel>.ViewBindingHolder,
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
            tvQualityNumber.text = data.qualityNum.toString()
            tvInspectNumber.text = data.deliverNum.toString()
            tvQualityScheme.text = data.qualitySolutionName

            val hasReceive = data.status != QualityTaskModelType.Task.code
            btnActionReceive.isVisible = !hasReceive
            btnActionCommit.isVisible = hasReceive
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
                            .pdaQmsTaskReceivePost(IdRequest(data.id)),
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
        WebClient.request(QualityApi::class.java).pdaQmsCommonDetailGet(data.id)
            .bindToFragment()
            .subscribe(
                {
                    viewModel.qualityDetailInfoData.postValue(it)
                    navController.navigate(R.id.action_qualityTaskFragment_to_qualityTaskCommitFragment)
                },
                {})
    }
}