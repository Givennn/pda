package com.panda.pda.app.operation.qms.quality_sign

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.app.R
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.common.adapter.CommonViewBindingAdapter
import com.panda.pda.app.common.data.model.IdRequest
import com.panda.pda.app.databinding.ItemQualitySignBinding
import com.panda.pda.app.operation.qms.BaseQualitySearchListFragment
import com.panda.pda.app.operation.qms.QualityViewModel
import com.panda.pda.app.operation.qms.data.QualityApi
import com.panda.pda.app.operation.qms.data.model.QualityTaskModel
import com.panda.pda.app.operation.qms.data.model.QualityTaskModelType
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import java.util.concurrent.TimeUnit

class QualitySignFragment : BaseQualitySearchListFragment<ItemQualitySignBinding>() {
    override val qualityTaskModelType: QualityTaskModelType
        get() = QualityTaskModelType.Sign

    private val viewModel by activityViewModels<QualityViewModel>()

    override fun createViewBinding(parent: ViewGroup): ItemQualitySignBinding {
        return ItemQualitySignBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    }

    override fun onBindViewHolder(
        holder: CommonViewBindingAdapter<ItemQualitySignBinding, QualityTaskModel>.ViewBindingHolder,
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
            btnActionSign.clicks()
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .bindToLifecycle(holder.itemView)
                .subscribe {
                    showActionRequestDialog(
                        WebClient.request(QualityApi::class.java)
                            .pdaQmsQualitySubTaskSignPost(IdRequest(data.id)),
                        getString(
                            R.string.quality_task_finish_confirm, getString(
                                R.string.desc_and_code_formatter,
                                data.qualityDesc,
                                data.qualityCode
                            )
                        ),
                        getString(R.string.quality_task_finish_success)
                    )
                }
            btnActionBackOut.clicks()
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe { backOut(data) }
        }
    }

    private fun backOut(data: QualityTaskModel) {
        WebClient.request(QualityApi::class.java).pdaQmsCommonDetailGet(data.id)
            .bindToFragment()
            .subscribe(
                {
                    viewModel.qualityDetailInfoData.postValue(it)
                    navController.navigate(R.id.action_qualitySignFragment_to_qualityDistributeBackOutFragment)
                },
                {})
    }
}