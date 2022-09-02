package com.panda.pda.mes.operation.fms.mission

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import com.panda.pda.mes.R
import com.panda.pda.mes.base.extension.putObjectString
import com.panda.pda.mes.base.retrofit.DataListNode
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.base.retrofit.onMainThread
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.databinding.FrameEmptyViewBinding
import com.panda.pda.mes.databinding.ItemTaskPrepareBinding
import com.panda.pda.mes.operation.fms.data.TaskApi
import com.panda.pda.mes.operation.fms.data.model.TaskInfoModel
import com.panda.pda.mes.operation.fms.data.model.DispatchOrderModel
import io.reactivex.rxjava3.core.Single

/**
 * created by AnJiwei 2022/6/21
 */
public class TaskPrepareFragment :
    BaseExchangeOperateActionFragment<DispatchOrderModel>() {
    private val taskViewModel by activityViewModels<TaskViewModel>()

    override fun api(key: String?): Single<DataListNode<DispatchOrderModel>> =
        WebClient.request(TaskApi::class.java)
            .prepareDispatchOrderListGet(key)

    override val titleResId: Int
        get() = R.string.task_prepare

    override val searchBarHintResId: Int
        get() = R.string.task_search_bar_hint

    override fun createAdapter(): CommonViewBindingAdapter<*, DispatchOrderModel> {
        return object :
            CommonViewBindingAdapter<ItemTaskPrepareBinding, DispatchOrderModel>(
                mutableListOf()) {
            override fun createBinding(parent: ViewGroup): ItemTaskPrepareBinding {
                return ItemTaskPrepareBinding.inflate(
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
                    tvProductInfo.text = listOf(data.productName, data.productModel).joinToString(" ")
                    tvPlanFinishDate.text =
                        getString(R.string.receive_time_formatter, data.receiveTime ?: "-")
                    tvTaskResponsePerson.text = data.issueName
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

    private fun onItemInfoClicked(data: DispatchOrderModel) {
        if (taskViewModel.taskInfoData.value?.detail?.id == data.id) {
            navToDetailFragment(data.id)
            return
        }
        WebClient.request(TaskApi::class.java)
            .taskGetByIdGet(data.id)
            .zipWith(WebClient.request(TaskApi::class.java).taskOperationRecordGet(data.id)
            ) { detail, records -> TaskInfoModel(detail, records.dataList) }
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

        navController.navigate(R.id.producePrepareFragment,
            Bundle().apply { putObjectString(data) })
    }
}