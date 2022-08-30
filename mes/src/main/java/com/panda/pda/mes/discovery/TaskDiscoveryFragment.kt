package com.panda.pda.mes.discovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import com.panda.pda.mes.R
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.base.retrofit.*
import com.panda.pda.mes.common.CommonSearchListFragment
import com.panda.pda.mes.databinding.FrameEmptyViewBinding
import com.panda.pda.mes.databinding.ItemDiscoveryTaskBinding
import com.panda.pda.mes.discovery.data.DiscoveryApi
import com.panda.pda.mes.operation.fms.mission.TaskViewModel
import com.panda.pda.mes.operation.fms.data.TaskApi
import com.panda.pda.mes.operation.fms.data.model.TaskInfoModel
import com.panda.pda.mes.operation.fms.data.model.DispatchOrderModel
import io.reactivex.rxjava3.core.Single

/**
 * created by AnJiwei 2021/9/1
 */
class TaskDiscoveryFragment : CommonSearchListFragment<DispatchOrderModel>() {

    private val taskViewModel by activityViewModels<TaskViewModel>()

    override val searchBarHintResId: Int?
        get() = R.string.discovery_search_hint
    override fun createAdapter(): CommonViewBindingAdapter<*, DispatchOrderModel> {

        return object :
            CommonViewBindingAdapter<ItemDiscoveryTaskBinding, DispatchOrderModel>(mutableListOf()) {
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
                data: DispatchOrderModel,
                position: Int,
            ) {
                holder.itemViewBinding.apply {
                    tvTaskInfo.text =
                        getString(R.string.desc_and_code_formatter, data.dispatchOrderDesc, data.dispatchOrderCode)
                    tvProductInfo.text = getString(
                        R.string.desc_and_code_formatter,
                        data.productName,
                        data.productCode
                    )
                    tvTaskSender.text = data.issueName
                    tvPlanIssueDate.text = "派工时间: ${data.issueTime}"
                }.clInfo.setOnClickListener { onItemInfoClicked(data) }
            }

        }
    }


    override val titleResId: Int
        get() = R.string.dispatch_order

    override fun api(key: String?): Single<DataListNode<DispatchOrderModel>> =
        WebClient.request(DiscoveryApi::class.java).pdaFmsTaskListAllGet(key)

    private fun onItemInfoClicked(data: DispatchOrderModel) {
        if (taskViewModel.taskInfoData.value?.detail?.id == data.id) {
            navToDetailFragment(data.id)
            return
        }
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
        navController.navigate(R.id.action_taskDiscoveryFragment_to_taskDiscoveryDetailFragment,
            Bundle().apply { putInt(TaskViewModel.TASK_ID, id) })
    }
}