package com.panda.pda.app.operation.qms.quality_distribute

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.app.R
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.common.adapter.CommonViewBindingAdapter
import com.panda.pda.app.common.data.model.IdRequest
import com.panda.pda.app.databinding.ItemQualityDistributeBinding
import com.panda.pda.app.operation.qms.BaseQualitySearchListFragment
import com.panda.pda.app.operation.qms.QualityViewModel
import com.panda.pda.app.operation.qms.data.QualityApi
import com.panda.pda.app.operation.qms.data.model.QualityTaskModel
import com.panda.pda.app.operation.qms.data.model.QualityTaskModelType
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import java.util.concurrent.TimeUnit

class QualityDistributeFragment : BaseQualitySearchListFragment<ItemQualityDistributeBinding>() {

    private val viewModel by activityViewModels<QualityViewModel>()

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
        holder: CommonViewBindingAdapter<ItemQualityDistributeBinding, QualityTaskModel>.ViewBindingHolder,
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
            tvQualityNumber.text = "${data.distributedNum}/${data.qualityNum}"

            btnActionDistribute.clicks()
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .bindToLifecycle(holder.itemView)
                .subscribe { distribute(data) }

            btnActionTransfer.clicks()
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .bindToLifecycle(holder.itemView)
                .subscribe { transfer(data) }
        }
    }

    //转办
    private fun transfer(data: QualityTaskModel) {
        WebClient.request(QualityApi::class.java).pdaQmsCommonDetailGet(data.id)
            .bindToFragment()
            .subscribe(
                {
                    viewModel.qualityDetailInfoData.postValue(it)
                    navController.navigate(R.id.action_qualityDistributeFragment_to_qualityDistributeTransferFragment)
                },
                {})
    }

    private fun distribute(data: QualityTaskModel) {
        WebClient.request(QualityApi::class.java).pdaQmsCommonDetailGet(data.id)
            .bindToFragment()
            .subscribe(
                {
                    viewModel.qualityDetailInfoData.postValue(it)
                    navController.navigate(R.id.action_qualityDistributeFragment_to_qualityDistributeDistributeFragment)
                },
                {})
    }


}