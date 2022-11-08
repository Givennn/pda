package com.panda.pda.mes.operation.fms.mission

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.base.extension.toast
import com.panda.pda.mes.common.data.CommonParameters
import com.panda.pda.mes.common.data.DataParamType
import com.panda.pda.mes.databinding.FragmentTaskDetailBinding
import com.panda.pda.mes.databinding.ItemTaskDetailOperateRecordBinding
import com.panda.pda.mes.operation.fms.data.model.TaskInfoModel
import com.panda.pda.mes.operation.fms.data.model.TaskRecordModel


class TaskDetailFragment : BaseFragment(R.layout.fragment_task_detail) {

    private val viewBinding by viewBinding<FragmentTaskDetailBinding>()
    private val viewModel by activityViewModels<TaskViewModel>()

    private lateinit var bindingAdapter: CommonViewBindingAdapter<ItemTaskDetailOperateRecordBinding, TaskRecordModel>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener(it) }
        createRecordAdapter()
        viewBinding.rvRecords.adapter = bindingAdapter
        val taskInfo = viewModel.taskInfoData.value
        if (taskInfo != null) {
            bindData(taskInfo)
        } else {
            toast(R.string.get_task_detail_failed)
            navBackListener(requireView())
        }
    }

    private fun createRecordAdapter() {
        bindingAdapter =
            object : CommonViewBindingAdapter<ItemTaskDetailOperateRecordBinding, TaskRecordModel>(
                mutableListOf()) {
                override fun createBinding(parent: ViewGroup): ItemTaskDetailOperateRecordBinding {
                    return ItemTaskDetailOperateRecordBinding.inflate(LayoutInflater.from(parent.context))
                }

                override fun onBindViewHolderWithData(
                    holder: ViewBindingHolder,
                    data: TaskRecordModel,
                    position: Int,
                ) {
                    holder.itemViewBinding.apply {
                        viewStepLineTop.isVisible = holder.layoutPosition != 0
                        viewStepLineBottom.isVisible =
                            holder.bindingAdapterPosition != itemCount - 1
                        tvRecordType.text = data.operateType
                        tvOperator.text = data.operateName
                        tvTime.text = data.createTime
                        tvDesc.text = data.operateDetail
                    }
                }

            }
    }

    private fun bindData(info: TaskInfoModel) {
        viewBinding.apply {
            val taskDetail = info.detail
            tvTaskCode.text = taskDetail.dispatchOrderCode
            tvTaskDesc.text = taskDetail.dispatchOrderDesc
            tvParentOrder.text = taskDetail.originalDispatchOrderCode
            tvIncludeOrders.text =
                if (taskDetail.includeDispatchOrderList == null) "" else taskDetail.includeDispatchOrderList.joinToString(
                    ",") { it.dispatchOrderCode }
            tvTaskCount.text = taskDetail.dispatchOrderNum.toString()
            tvProductCode.text = taskDetail.productCode
            tvProductDesc.text = taskDetail.productName
            tvWorkOrderCode.text = taskDetail.workOrderCode
            tvOrderCode.text = taskDetail.workNo
            tvBatchCode.text = taskDetail.batchNo
            tvProductReceiver.text = taskDetail.receiveName ?: ""
            tvProductType.text =
                CommonParameters.getDesc(DataParamType.PRODUCT_MODE, taskDetail.productMode)
            tvTaskStatus.text =
                CommonParameters.getDesc(DataParamType.TASK_STATUS, taskDetail.dispatchOrderStatus)
            tvPlanStartTime.text = taskDetail.planStartTime
            tvPlanFinishTime.text = taskDetail.planEndTime
            tvOperator.text = taskDetail.jockeyName
            if (taskDetail.productMode == CommonParameters.getValue(DataParamType.PRODUCT_MODE,
                    "人工")
            ) {
                tvPrdEqp.text = "-"
            } else {
                tvPrdEqp.text = taskDetail.equipmentDesc
            }
            tvActualStartTime.text = taskDetail.realStartTime
            tvActualFinishTime.text = taskDetail.realEndTime
            bindingAdapter.refreshData(info.recordList ?: listOf())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_detail, container, false)
    }

}