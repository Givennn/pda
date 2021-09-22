package com.panda.pda.app.discovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import com.panda.pda.app.R
import com.panda.pda.app.common.adapter.ViewBindingAdapter
import com.panda.pda.app.base.retrofit.*
import com.panda.pda.app.common.CommonSearchListFragment
import com.panda.pda.app.databinding.FrameEmptyViewBinding
import com.panda.pda.app.databinding.ItemDiscoveryTaskBinding
import com.panda.pda.app.discovery.data.DiscoveryApi
import com.panda.pda.app.task.TaskViewModel
import com.panda.pda.app.task.data.TaskApi
import com.panda.pda.app.task.data.model.TaskInfoModel
import com.panda.pda.app.task.data.model.TaskModel
import io.reactivex.rxjava3.core.Single

/**
 * created by AnJiwei 2021/9/1
 */
class TaskDiscoveryFragment : CommonSearchListFragment<TaskModel>() {

    private val taskViewModel by activityViewModels<TaskViewModel>()

    override fun createAdapter(): ViewBindingAdapter<*, TaskModel> {

        return object :
            ViewBindingAdapter<ItemDiscoveryTaskBinding, TaskModel>(mutableListOf()) {
            override fun createBinding(parent: ViewGroup): ItemDiscoveryTaskBinding {
                return ItemDiscoveryTaskBinding.inflate(LayoutInflater.from(parent.context),
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
                    tvTaskCode.text = data.taskCode
                    tvTaskDesc.text = data.taskDesc
                    tvTaskSender.text = data.issueName
                    tvPlanIssueDate.text = data.issueTime
                }.clInfo.setOnClickListener { onItemInfoClicked(data) }
            }

        }
    }


    override val titleResId: Int
        get() = R.string.task

    override fun api(key: String?): Single<BaseResponse<DataListNode<TaskModel>>> =
        WebClient.request(DiscoveryApi::class.java).pdaFmsTaskListAllGet(key)

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
        navController.navigate(R.id.action_taskDiscoveryFragment_to_taskDiscoveryDetailFragment,
            Bundle().apply { putInt(TaskViewModel.TASK_ID, id) })
    }
}