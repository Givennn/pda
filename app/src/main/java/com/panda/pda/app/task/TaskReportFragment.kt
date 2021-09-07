package com.panda.pda.app.task

import android.os.Bundle
import android.text.Html
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.color
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseRecycleViewAdapter
import com.panda.pda.app.base.retrofit.*
import com.panda.pda.app.common.CommonSearchListFragment
import com.panda.pda.app.databinding.FrameEmptyViewBinding
import com.panda.pda.app.databinding.ItemTaskReportBinding
import com.panda.pda.app.task.data.TaskApi
import com.panda.pda.app.task.data.model.TaskInfoModel
import com.panda.pda.app.task.data.model.TaskModel
import io.reactivex.rxjava3.core.Single

class TaskReportFragment :
    CommonSearchListFragment<TaskModel>() {
    private val taskViewModel by activityViewModels<TaskViewModel>()

    override fun api(key: String?): Single<BaseResponse<DataListNode<TaskModel>>> =
        WebClient.request(TaskApi::class.java)
            .pdaFmsTaskReportListByPageGet(key)

    override val titleResId: Int
        get() = R.string.task_report

    override val searchBarHintResId: Int
        get() = R.string.task_search_bar_hint

    override fun createAdapter(): BaseRecycleViewAdapter<*, TaskModel> {
        return object : BaseRecycleViewAdapter<ItemTaskReportBinding, TaskModel>(mutableListOf()) {
            override fun createBinding(parent: ViewGroup): ItemTaskReportBinding {
                return ItemTaskReportBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            }

            override fun createEmptyViewBinding(parent: ViewGroup): ViewBinding {
                return FrameEmptyViewBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            }

            override fun onBindViewHolderWithData(
                holder: ViewBindingHolder,
                data: TaskModel,
                position: Int,
            ) {
                holder.itemViewBinding.apply {
                    tvTaskInfo.text =
                        getString(R.string.desc_and_code_formatter, data.taskDesc, data.taskCode)
                    tvProductInfo.text = getString(R.string.desc_and_code_formatter,
                        data.productName,
                        data.productCode)
                    tvPlanFinishDate.text =
                        getString(R.string.plan_finish_time_formatter, data.planStartTime)
                    tvTaskProgress.text = getColorTaskProgress(data)
                    tvTaskSender.text = data.issueName
                    tvManHour.text = getManHour(data.totalReportTime ?: 0)
                    btnAction.visibility = if (data.taskNum > data.reportNum) View.VISIBLE else View.GONE
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

    private fun getColorTaskProgress(data: TaskModel): SpannableStringBuilder {
        return SpannableStringBuilder()
            .color(requireContext().getColor(R.color.textHighLightColor)) { append(data.reportNum.toString()) }
            .append("/${data.taskNum}")
    }

    private fun getManHour(minute: Int): String {
        var result = ""
        val hours: Int = minute / 60
        val minutes: Int = minute % 60
        if (hours != 0) {
            result += "${hours}h"
        }
        if (minutes != 0) {
            result += "${minutes}min"
        }
        return if (result.isEmpty()) "0min" else result
    }

    private fun onItemInfoClicked(data: TaskModel) {
        if (taskViewModel.taskInfoData.value?.detail?.id == data.id) {
            navToDetailFragment(data.id)
            return
        }
        WebClient.request(TaskApi::class.java)
            .taskGetByIdGet(data.id)
            .unWrapperData()
            .zipWith(WebClient.request(TaskApi::class.java).taskOperationRecordGet(data.id)
                .unWrapperData(),
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

    private fun onItemActionClicked(data: TaskModel) {
        WebClient.request(TaskApi::class.java).taskGetByIdGet(data.id)
            .bindToFragment()
            .subscribe({
                taskViewModel.taskInfoData.postValue(TaskInfoModel(it, null))
                navController.navigate(R.id.taskReportInputFragment)
            }, {})
    }
}