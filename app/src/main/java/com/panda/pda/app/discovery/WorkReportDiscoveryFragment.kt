package com.panda.pda.app.discovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import com.panda.pda.app.R
import com.panda.pda.app.common.adapter.CommonViewBindingAdapter
import com.panda.pda.app.base.retrofit.DataListNode
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.common.CommonSearchListFragment
import com.panda.pda.app.databinding.FrameEmptyViewBinding
import com.panda.pda.app.databinding.ItemDiscoveryWorkReportBinding
import com.panda.pda.app.discovery.data.DiscoveryApi
import com.panda.pda.app.discovery.data.TaskReportModel
import com.panda.pda.app.operation.fms.mission.TaskViewModel
import io.reactivex.rxjava3.core.Single

/**
 * created by AnJiwei 2021/9/1
 */
class WorkReportDiscoveryFragment : CommonSearchListFragment<TaskReportModel>() {

    private val taskViewModel by activityViewModels<TaskReportViewModel>()

    override val searchBarHintResId: Int?
        get() = R.string.discovery_search_hint

    override fun createAdapter(): CommonViewBindingAdapter<*, TaskReportModel> {
        return object : CommonViewBindingAdapter<ItemDiscoveryWorkReportBinding, TaskReportModel>(
            mutableListOf()) {
            override fun createBinding(parent: ViewGroup): ItemDiscoveryWorkReportBinding {
                return ItemDiscoveryWorkReportBinding.inflate(LayoutInflater.from(parent.context),
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
                data: TaskReportModel,
                position: Int,
            ) {
                holder.itemViewBinding.apply {
                    tvTaskInfo.text =
                        getString(R.string.desc_and_code_formatter, data.taskDesc ?: "", data.taskCode ?: "")
                    tvProductInfo.text = getString(
                        R.string.desc_and_code_formatter,
                        data.productName ?: "",
                        data.productCode ?: ""
                    )
                    tvReportTime.text = "报工时间: ${data.createTime}"
                    tvTaskReporter.text = data.createName
                    tvReportNumber.text = data.reportNumber.toString()
                }.clInfo.setOnClickListener { onItemInfoClicked(data) }
            }

        }
    }

    override fun api(key: String?): Single<DataListNode<TaskReportModel>> =
        WebClient.request(DiscoveryApi::class.java).pdaFmsReportListAllGet(key)

    override val titleResId: Int
        get() = R.string.report

    private fun onItemInfoClicked(data: TaskReportModel) {

        WebClient.request(DiscoveryApi::class.java)
            .pdaFmsReportGetByIdGet(data.id)
            .bindToFragment()
            .subscribe({ info ->
                taskViewModel.taskReportDetailData.postValue(info)
                navToDetailFragment(data.id)
            }, { })
    }

    private fun navToDetailFragment(id: Int) {
        navController.navigate(R.id.action_workReportDiscoveryFragment_to_taskReportDetailFragment,
            Bundle().apply { putInt(TaskViewModel.TASK_ID, id) })
    }
}