package com.panda.pda.app.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseRecycleViewAdapter
import com.panda.pda.app.base.ConfirmDialogFragment
import com.panda.pda.app.base.extension.toast
import com.panda.pda.app.base.retrofit.*
import com.panda.pda.app.common.CommonSearchListFragment
import com.panda.pda.app.databinding.FrameEmptyViewBinding
import com.panda.pda.app.databinding.ItemTaskFinishBinding
import com.panda.pda.app.task.data.TaskApi
import com.panda.pda.app.task.data.model.TaskIdRequest
import com.panda.pda.app.task.data.model.TaskInfoModel
import com.panda.pda.app.task.data.model.TaskModel
import io.reactivex.rxjava3.core.Single

class TaskFinishFragment :
    CommonSearchListFragment<TaskModel>() {
    private val taskViewModel by activityViewModels<TaskViewModel>()

    override fun api(key: String?): Single<BaseResponse<DataListNode<TaskModel>>> =
        WebClient.request(TaskApi::class.java)
            .taskCompleteListGet(key)

    override val titleResId: Int
        get() = R.string.task_finish

    override fun createAdapter(): BaseRecycleViewAdapter<*, TaskModel> {
        return object : BaseRecycleViewAdapter<ItemTaskFinishBinding, TaskModel>(mutableListOf()) {
            override fun createBinding(parent: ViewGroup): ItemTaskFinishBinding {
                return ItemTaskFinishBinding.inflate(LayoutInflater.from(parent.context),
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
                    tvPlanFinishDate.text = data.planEndTime
                    tvTaskDesc.text = data.taskDesc
                    tvTaskSender.text = data.issueName
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
            },
                { toast(it.message ?: getString(R.string.net_work_error)) })
    }

    private fun navToDetailFragment(id: Int) {
        navController.navigate(R.id.taskDetailFragment,
            Bundle().apply { putInt(TaskViewModel.TASK_ID, id) })
    }

    private fun onItemActionClicked(data: TaskModel) {

        val dialog = ConfirmDialogFragment().setTitle(getString(R.string.task_finish_confirm))
            .setConfirmButton({ _, _ ->
                WebClient.request(TaskApi::class.java)
                    .taskCompleteConfirmPost(TaskIdRequest(data.id))
                    .bindToFragment()
                    .subscribe({ toast(R.string.task_finish_success_toast) },
                        { })
            })
        dialog.show(parentFragmentManager, TAG)
    }
}