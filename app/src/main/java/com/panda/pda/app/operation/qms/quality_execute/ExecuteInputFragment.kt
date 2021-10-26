package com.panda.pda.app.operation.qms.quality_execute

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import by.kirich1409.viewbindingdelegate.viewBinding
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.common.ModelPropertyCreator
import com.panda.pda.app.common.adapter.CommonViewBindingAdapter
import com.panda.pda.app.databinding.FragmentExecuteInputBinding
import com.panda.pda.app.databinding.ItemQualityCheckItemBinding
import com.panda.pda.app.operation.qms.data.QualityApi
import com.panda.pda.app.operation.qms.data.model.QualityInspectItemModel
import com.panda.pda.app.operation.qms.data.model.QualitySubTaskDetailModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import java.util.concurrent.TimeUnit

/**
 * created by AnJiwei 2021/10/13
 */
class ExecuteInputFragment : BaseFragment(R.layout.fragment_execute_input) {

    private val viewBinding by viewBinding<FragmentExecuteInputBinding>()

    private lateinit var subTaskDetailModel: QualitySubTaskDetailModel

    private val inspectItemAdapter by lazy {
        Moshi.Builder().build().adapter<List<QualityInspectItemModel>>(
            Types.newParameterizedType(
                List::class.java,
                QualityInspectItemModel::class.java
            )
        )
    }

    private lateinit var inspectItems: List<QualityInspectItemModel>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }

        subTaskDetailModel =
            requireArguments().getSerializable(DETAIL_KEY) as QualitySubTaskDetailModel
        inspectItems =
            inspectItemAdapter.fromJson(requireArguments().getString(QUALITY_ITEM_KEY, ""))
                ?: listOf()
        val modelProperty =
            ModelPropertyCreator(QualitySubTaskDetailModel::class.java, viewBinding.llPropertyInfo)
        modelProperty.setData(subTaskDetailModel)
        viewBinding.rvQualityItem.adapter = object :
            CommonViewBindingAdapter<ItemQualityCheckItemBinding, QualityInspectItemModel>(
                inspectItems.toMutableList()
            ) {
            override fun createBinding(parent: ViewGroup): ItemQualityCheckItemBinding {
                return ItemQualityCheckItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            }

            override fun onBindViewHolderWithData(
                holder: ViewBindingHolder,
                data: QualityInspectItemModel,
                position: Int
            ) {
                val isValueType = data.valueType == 1
                holder.itemViewBinding.ivArrow.isVisible = !isValueType
                holder.itemViewBinding.tvSelectValue.isVisible = !isValueType
                holder.itemViewBinding.tvInputValue.isVisible = isValueType

                holder.itemViewBinding.tvInputValue.doAfterTextChanged {
                    data.conclusion = it.toString()
                }

                if (!isValueType) {
                    holder.itemViewBinding.root.setOnClickListener {
                        showConclusionSelectDialog(data, holder.bindingAdapterPosition)
                    }
                }
            }
        }

        viewBinding.llNgReason.clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .bindToLifecycle(requireView())
            .subscribe { navToNgReasonSelect() }
    }

    private fun showConclusionSelectDialog(
        data: QualityInspectItemModel,
        bindingAdapterPosition: Int
    ) {

    }

    private fun navToNgReasonSelect() {
        WebClient.request(QualityApi::class.java)
            .pdaQmsQualitySubTaskGetBadnessListGet(subTaskDetailModel.id)
            .bindToFragment()
            .subscribe({

            }, {})
    }

    companion object {
        const val DETAIL_KEY = "detail_key"
        const val QUALITY_ITEM_KEY = "quality_item_key"
    }
}