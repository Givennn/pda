package com.panda.pda.app.operation.fms.mission

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.common.adapter.CommonViewBindingAdapter
import com.panda.pda.app.base.extension.toast
import com.panda.pda.app.common.data.CommonParameters
import com.panda.pda.app.common.data.DataParamType
import com.panda.pda.app.databinding.FragmentTaskDetailBinding
import com.panda.pda.app.databinding.ItemTaskDetailOperateRecordBinding
import com.panda.pda.app.operation.fms.data.model.TaskInfoModel
import com.panda.pda.app.operation.fms.data.model.TaskRecordModel


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

    private fun bindData(info: TaskInfoModel) {
        viewBinding.apply {
            val taskDetail = info.detail
            tvTaskCode.text = taskDetail.taskCode
            tvTaskDesc.text = taskDetail.taskDesc
            tvTaskCount.text = taskDetail.taskNum.toString()
            tvProductCode.text = taskDetail.productCode
            tvProductDesc.text = taskDetail.productName
            tvPlanCode.text = taskDetail.planCode
            tvOrderCode.text = taskDetail.workNo
            tvBatchCode.text = taskDetail.batchNo
            tvTaskStatus.text =
                CommonParameters.getDesc(DataParamType.TASK_STATUS, taskDetail.taskStatus)
            tvPlanStartTime.text = taskDetail.planStartTime
            tvPlanFinishTime.text = taskDetail.planEndTime
            tvOperator.text = taskDetail.jockeyName
            tvPrdEqp.text = taskDetail.equipmentDesc
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