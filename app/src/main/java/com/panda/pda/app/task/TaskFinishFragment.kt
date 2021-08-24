package com.panda.pda.app.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.base.BaseRecycleViewAdapter
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.base.retrofit.onMainThread
import com.panda.pda.app.databinding.FragmentTaskFinishBinding
import com.panda.pda.app.databinding.ItemTaskFinishBinding
import com.panda.pda.app.task.model.TaskApi
import com.panda.pda.app.task.model.TaskModel

class TaskFinishFragment : BaseFragment(R.layout.fragment_task_finish) {

    private val viewBinding by viewBinding<FragmentTaskFinishBinding>()

    private lateinit var adapter: BaseRecycleViewAdapter<ItemTaskFinishBinding, TaskModel>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = object : BaseRecycleViewAdapter<ItemTaskFinishBinding, TaskModel>(mutableListOf()) {
            override fun createBinding(parent: ViewGroup): ItemTaskFinishBinding {
                return ItemTaskFinishBinding.inflate(LayoutInflater.from(parent.context))
            }

            override fun onBindViewHolderWithData(
                holder: ViewHolder,
                data: TaskModel,
                position: Int,
            ) {
                holder.binding.apply {
                    tvTaskCode.text = data.taskCode
                    tvPlanFinishDate.text = data.planEndTime
                    tvTaskDesc.text = data.taskDesc
                    tvTaskSender.text = data.issueName
                }.btnAction.setOnClickListener {
                    report(data)
                }
            }

        }
        viewBinding.rvTaskList.adapter = adapter
    }


    override fun onResume() {
        super.onResume()
        refreshData()
    }

    private fun refreshData() {
        WebClient.request(TaskApi::class.java)
            .taskCompleteListGet()
            .onMainThread()
            .subscribe { data -> adapter.refreshData(data.data?.dataList ?: listOf()) }
    }

    private fun report(data: TaskModel) {

    }
}