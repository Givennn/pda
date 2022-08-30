package com.panda.pda.mes.operation.fms.mission

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.color
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import com.panda.pda.mes.R
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.base.retrofit.*
import com.panda.pda.mes.common.DateUtils
import com.panda.pda.mes.databinding.FrameEmptyViewBinding
import com.panda.pda.mes.databinding.ItemTaskReportBinding
import com.panda.pda.mes.operation.fms.data.TaskApi
import com.panda.pda.mes.operation.fms.data.model.TaskInfoModel
import com.panda.pda.mes.operation.fms.data.model.DispatchOrderModel
import io.reactivex.rxjava3.core.Single

class TaskReportFragment :
    BaseExchangeOperateActionFragment<DispatchOrderModel>() {
    private val taskViewModel by activityViewModels<TaskViewModel>()

    override fun api(key: String?): Single<DataListNode<DispatchOrderModel>> =
        WebClient.request(TaskApi::class.java)
            .pdaFmsTaskReportListByPageGet(key)

    override val titleResId: Int
        get() = R.string.task_report

    override val searchBarHintResId: Int
        get() = R.string.task_search_bar_hint

    override fun createAdapter(): CommonViewBindingAdapter<*, DispatchOrderModel> {
        return object : CommonViewBindingAdapter<ItemTaskReportBinding, DispatchOrderModel>(mutableListOf()) {
            override fun createBinding(parent: ViewGroup): ItemTaskReportBinding {
                return ItemTaskReportBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            }

            override fun createEmptyViewBinding(parent: ViewGroup): ViewBinding {
                return FrameEmptyViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            }

            override fun onBindViewHolderWithData(
                holder: ViewBindingHolder,
                data: DispatchOrderModel,
                position: Int,
            ) {
                holder.itemViewBinding.apply {
                    tvTaskInfo.text = listOf(data.dispatchOrderCode, data.dispatchOrderDesc).joinToString(" ")
                    tvProductInfo.text = listOf(data.productName, data.productCode, data.productModel).joinToString(" ")
                    tvPlanFinishDate.text =
                        getString(R.string.plan_finish_time_formatter, data.planStartTime)
                    tvTaskProgress.text = getColorTaskProgress(data)
                    tvTaskSender.text = data.receiveName
                    tvManHour.text = DateUtils.getManHour(data.totalReportTime ?: 0)
                    btnAction.visibility =
                        if (data.dispatchOrderNum > data.reportNum) View.VISIBLE else View.GONE
                    btnAction.setOnClickListener {
                        onItemActionClicked(data)
                    }
                    clInfo.setOnClickListener {
                        onItemInfoClicked(data)
                    }
                }
            }
        }
    }

    private fun getColorTaskProgress(data: DispatchOrderModel): SpannableStringBuilder {
        return SpannableStringBuilder()
            .color(requireContext().getColor(R.color.textHighLightColor)) { append(data.reportNum.toString()) }
            .append("/${data.dispatchOrderNum}")
    }

    private fun onItemInfoClicked(data: DispatchOrderModel) {

        WebClient.request(TaskApi::class.java)
            .taskGetByIdGet(data.id)
            .zipWith(WebClient.request(TaskApi::class.java).taskOperationRecordGet(data.id),
                { detail, records -> TaskInfoModel(detail, records.dataList) })
            .onMainThread()
            .bindLoadingStatus()
            .subscribe({ info ->
                taskViewModel.taskInfoData.postValue(info)
                navToDetailFragment(data.id)
            }, { })
    }

    private fun navToDetailFragment(id: Int) {
        navController.navigate(R.id.taskDetailFragment,
            Bundle().apply { putInt(TaskViewModel.TASK_ID, id) })
    }

    private fun onItemActionClicked(data: DispatchOrderModel) {
        WebClient.request(TaskApi::class.java).taskGetByIdGet(data.id)
            .bindToFragment()
            .subscribe({
                taskViewModel.taskInfoData.postValue(TaskInfoModel(it, null))
                navController.navigate(R.id.taskReportInputFragment)
            }, {})
    }
}