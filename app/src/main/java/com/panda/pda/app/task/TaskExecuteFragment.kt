package com.panda.pda.app.task

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.base.BaseRecycleViewAdapter
import com.panda.pda.app.base.ConfirmDialogFragment
import com.panda.pda.app.base.extension.toast
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.base.retrofit.onMainThread
import com.panda.pda.app.base.unWrapperData
import com.panda.pda.app.databinding.*
import com.panda.pda.app.task.data.TaskApi
import com.panda.pda.app.task.data.model.TaskIdRequest
import com.panda.pda.app.task.data.model.TaskInfoModel
import com.panda.pda.app.task.data.model.TaskModel

class TaskExecuteFragment : BaseFragment(R.layout.fragment_task_execute) {

    private val viewBinding by viewBinding<FragmentTaskExecuteBinding>()
    private val taskViewModel by activityViewModels<TaskViewModel>()

    private lateinit var adapter: BaseRecycleViewAdapter<ItemTaskExecuteBinding, TaskModel>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createAdapter()
        viewBinding.root.setOnRefreshListener { refreshData() }
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener(it) }
        viewBinding.etSearchBar.setOnEditorActionListener { editText, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                refreshData()
            } else if (event.keyCode == KeyEvent.KEYCODE_ENTER) {
                refreshData()
                (editText as EditText).selectAll()
                return@setOnEditorActionListener true
            }
            false
        }
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    private fun createAdapter() {
        adapter =
            object : BaseRecycleViewAdapter<ItemTaskExecuteBinding, TaskModel>(mutableListOf()) {
                override fun createBinding(parent: ViewGroup): ItemTaskExecuteBinding {
                    return ItemTaskExecuteBinding.inflate(LayoutInflater.from(parent.context))
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
        viewBinding.rvTaskList.adapter = adapter
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
            .bindToFragment()
            .subscribe({ info ->
                taskViewModel.taskInfoData.postValue(info)
                navToDetailFragment(data.id)
            }, { })
    }

    private fun navToDetailFragment(id: Int) {
        navController.navigate(R.id.taskDetailFragment,
            Bundle().apply { putInt(TaskViewModel.TASK_ID, id) })
    }


    private fun refreshData() {
        WebClient.request(TaskApi::class.java)
            .taskExecutionListByPageGet(viewBinding.etSearchBar.text?.toString())
            .onMainThread()
            .unWrapperData()
            .bindToFragment()
            .doFinally { viewBinding.root.isRefreshing = false }
            .subscribe({ data -> adapter.refreshData(data.dataList) }, { })
    }

    private fun onItemActionClicked(data: TaskModel) {
        val dialog = ConfirmDialogFragment().setTitle(getString(R.string.task_finish_confirm))
            .setConfirmButton({ _, _ ->
                WebClient.request(TaskApi::class.java)
                    .taskExecutionConfirmPost(TaskIdRequest(data.id))
                    .onMainThread()
                    .unWrapperData()
                    .bindToFragment()
                    .subscribe({ toast(R.string.task_finish_toast) },
                        { })
            })
        dialog.show(parentFragmentManager, TAG)
    }
}