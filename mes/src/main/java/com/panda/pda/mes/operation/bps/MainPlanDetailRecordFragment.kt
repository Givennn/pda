package com.panda.pda.mes.operation.bps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.retrofit.DataListNode
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.common.data.model.CommonOperationRecordModel
import com.panda.pda.mes.databinding.FragmentQualityDetailRecordBinding
import com.panda.pda.mes.databinding.ItemTaskDetailOperateRecordBinding
import com.panda.pda.mes.operation.qms.data.model.QualityTaskRecordModel

/**
 * created by AnJiwei 2022/8/12
 */
class MainPlanDetailRecordFragment(private val recordList: DataListNode<CommonOperationRecordModel>?) :
    BaseFragment(R.layout.fragment_quality_detail_record) {

    private val bindingAdapter by lazy { createRecordAdapter() }
    private val viewBinding by viewBinding<FragmentQualityDetailRecordBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.rvRecords.adapter = bindingAdapter
        if (recordList != null) {
            bindingAdapter.refreshData(recordList.dataList)
        }
//        viewModel.qualityDetailRecordData.observe(viewLifecycleOwner, {
//            bindingAdapter.refreshData(it.dataList)
//        })
    }

    private fun createRecordAdapter(): CommonViewBindingAdapter<ItemTaskDetailOperateRecordBinding, CommonOperationRecordModel> {
        return object :
            CommonViewBindingAdapter<ItemTaskDetailOperateRecordBinding, CommonOperationRecordModel>() {
            override fun createBinding(parent: ViewGroup): ItemTaskDetailOperateRecordBinding {
                return ItemTaskDetailOperateRecordBinding.inflate(LayoutInflater.from(parent.context))
            }

            override fun onBindViewHolderWithData(
                holder: ViewBindingHolder,
                data: CommonOperationRecordModel,
                position: Int,
            ) {
                holder.itemViewBinding.apply {
                    viewStepLineTop.isVisible = holder.layoutPosition != 0
                    viewStepLineBottom.isVisible = holder.bindingAdapterPosition != itemCount - 1
                    tvRecordType.text = data.operateType
                    tvOperator.text = data.operateName
                    tvTime.text = data.createTime
                    tvDesc.text = data.remark
                }
            }
        }
    }
}