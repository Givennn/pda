package com.panda.pda.app.operation.qms.quality_task_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.common.adapter.CommonViewBindingAdapter
import com.panda.pda.app.databinding.FragmentQualityDetailRecordBinding
import com.panda.pda.app.databinding.ItemTaskDetailOperateRecordBinding
import com.panda.pda.app.operation.qms.QualityViewModel
import com.panda.pda.app.operation.qms.data.model.QualityTaskRecordModel

/**
 * created by AnJiwei 2021/9/28
 */
class QualityDetailRecordFragment : BaseFragment(R.layout.fragment_quality_detail_record) {

    private val bindingAdapter by lazy { createRecordAdapter() }
    private val viewBinding by viewBinding<FragmentQualityDetailRecordBinding>()
    private val viewModel by activityViewModels<QualityViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.rvRecords.adapter = bindingAdapter
        viewModel.qualityDetailRecordData.observe(viewLifecycleOwner, {
            bindingAdapter.refreshData(it.dataList)
        })
    }

    private fun createRecordAdapter(): CommonViewBindingAdapter<ItemTaskDetailOperateRecordBinding, QualityTaskRecordModel> {
        return object :
            CommonViewBindingAdapter<ItemTaskDetailOperateRecordBinding, QualityTaskRecordModel>() {
            override fun createBinding(parent: ViewGroup): ItemTaskDetailOperateRecordBinding {
                return ItemTaskDetailOperateRecordBinding.inflate(LayoutInflater.from(parent.context))
            }

            override fun onBindViewHolderWithData(
                holder: ViewBindingHolder,
                data: QualityTaskRecordModel,
                position: Int,
            ) {
                holder.itemViewBinding.apply {
                    when (holder.layoutPosition) {
                        0 -> {
                            viewStepLineTop.visibility = View.INVISIBLE
                        }
                        itemCount - 1 -> {
                            viewStepLineBottom.visibility = View.INVISIBLE
                        }
                    }
                    tvRecordType.text = data.operateType
                    tvOperator.text = data.operateName
                    tvTime.text = data.createTime
                    tvDesc.text = data.operateDetail
                }
            }
        }
    }
}