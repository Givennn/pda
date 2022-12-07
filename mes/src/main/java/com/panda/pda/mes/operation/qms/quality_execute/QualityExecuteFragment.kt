package com.panda.pda.mes.operation.qms.quality_execute

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.mes.R
import com.panda.pda.mes.base.extension.putObjectString
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.databinding.ItemQualityExecuteBinding
import com.panda.pda.mes.operation.qms.BaseQualitySubTaskSearchListFragment
import com.panda.pda.mes.operation.qms.data.QualityApi
import com.panda.pda.mes.operation.qms.data.model.QualityInspectItemModel
import com.panda.pda.mes.operation.qms.data.model.QualitySubTaskModel
import com.panda.pda.mes.operation.qms.data.model.QualityTaskModelType
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit

class QualityExecuteFragment : BaseQualitySubTaskSearchListFragment<ItemQualityExecuteBinding>() {
    override val qualityTaskModelType: QualityTaskModelType
        get() = QualityTaskModelType.Execute

    private val inspectItemAdapter by lazy {
        Moshi.Builder().build().adapter<List<QualityInspectItemModel>>(
            Types.newParameterizedType(
                List::class.java,
                QualityInspectItemModel::class.java
            )
        )
    }

    override fun createViewBinding(parent: ViewGroup): ItemQualityExecuteBinding {
        return ItemQualityExecuteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun onBindViewHolder(
        holder: CommonViewBindingAdapter<ItemQualityExecuteBinding, QualitySubTaskModel>.ViewBindingHolder,
        data: QualitySubTaskModel,
        position: Int
    ) {
        holder.itemViewBinding.apply {
            tvQualityInfo.text = getString(
                R.string.desc_and_code_formatter,
                data.qualityDesc,
                data.qualityTaskCode
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
            tvQualityNumber.text = "${data.inspectedNum}/${data.distributedNum}"
            tvQualityScheme.text = data.qualitySolutionName

            btnActionExecute.clicks()
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .bindToLifecycle(holder.itemView)
                .subscribe {
                    execute(data)
                }
        }
    }

    private fun execute(data: QualitySubTaskModel) {
        Single.zip(
            WebClient.request(QualityApi::class.java)
                .pdaQmsQualitySubTaskGetByIdGet(data.id),
            WebClient.request(QualityApi::class.java).pdaQmsQualityTaskGetQualityItemGet(data.id)
        ) { subTaskDetail, qualityItems ->
            Pair(subTaskDetail, qualityItems)
        }.bindToFragment()
            .subscribe({

                val args = Bundle().apply {
                    putObjectString(it.first)
                    putString(
                        ExecuteInputFragment.QUALITY_ITEM_KEY,
                        inspectItemAdapter.toJson(it.second.dataList)
                    )
                }
                navController.navigate(
                    R.id.action_qualityExecuteFragment_to_executeInputFragment,
                    args
                )
            }, {})
    }
}

