package com.panda.pda.mes.discovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.panda.pda.mes.R
import com.panda.pda.mes.base.extension.putGenericObjectString
import com.panda.pda.mes.base.extension.putObjectString
import com.panda.pda.mes.base.retrofit.DataListNode
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.CommonSearchListFragment
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.common.data.model.CommonOperationRecordModel
import com.panda.pda.mes.databinding.FrameEmptyViewBinding
import com.panda.pda.mes.databinding.ItemDiscoveryWorkOrderBinding
import com.panda.pda.mes.databinding.ItemMainPlanDiscoveryBinding
import com.panda.pda.mes.discovery.data.DiscoveryApi
import com.panda.pda.mes.discovery.data.WorkOrderDiscoveryModel
import com.panda.pda.mes.operation.bps.data.MainPlanApi
import com.panda.pda.mes.operation.bps.data.model.MainPlanModel
import com.squareup.moshi.Types
import io.reactivex.rxjava3.core.Single

/**
 * created by AnJiwei 2022/8/31
 */
class WorkOrderDiscoveryFragment: CommonSearchListFragment<WorkOrderDiscoveryModel>()  {

    override fun createAdapter(): CommonViewBindingAdapter<*, WorkOrderDiscoveryModel> {
        return object : CommonViewBindingAdapter<ItemDiscoveryWorkOrderBinding, WorkOrderDiscoveryModel>() {
            override fun createBinding(parent: ViewGroup): ItemDiscoveryWorkOrderBinding {
                return ItemDiscoveryWorkOrderBinding.inflate(
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
                data: WorkOrderDiscoveryModel,
                position: Int,
            ) {
                holder.itemViewBinding.apply {
//                    tvMainPlanInfo.text = data.planNo
//                    tvProductInfo.text = listOf(data.productName,
//                        data.productCode, data.productModel).joinToString(" ")
//                    tvDeliverDate.text =
//                        getString(R.string.plan_deliver_time_formatter, data.deliveryTime)
//                    tvMainPlanNum.text = data.planNumber.toString()
                    tvProductInfo.text = listOf(data.productCode, data.productName, data.productModel).joinToString(" ")
                    tvWorkOrderInfo.text = data.workOrderCode
                    tvWorkOrderNum.text = data.workOrderNum.toString()
                    clInfo.setOnClickListener {
                        onItemInfoClicked(data)
                    }
                }
            }

        }
    }

    private fun onItemInfoClicked(data: WorkOrderDiscoveryModel) {
        showDetail(data)
    }

    private fun showDetail(data: WorkOrderDiscoveryModel) {
        Single.zip(
            WebClient.request(DiscoveryApi::class.java).workOrderGetByIdGet(data.id),
            WebClient.request(DiscoveryApi::class.java).workOrderOperationRecordGet(data.id)
        ) { info, record -> Pair(info, record) }
            .bindToFragment()
            .subscribe({
                navController.navigate(R.id.action_workOrderDiscoveryFragment_to_workOrderDetailFragment,
                    Bundle().apply {
                        putObjectString(it.first)
                        putGenericObjectString(
                            it.second,
                            Types.newParameterizedType(
                                DataListNode::class.java,
                                CommonOperationRecordModel::class.java
                            )
                        )
                    })
            }, {})
    }

    override fun api(key: String?): Single<DataListNode<WorkOrderDiscoveryModel>> =
        WebClient.request(DiscoveryApi::class.java)
            .workOrderDiscoveryListGet(key)

    override val titleResId: Int
    get() = R.string.work_order

    override val searchBarHintResId: Int
    get() = R.string.work_order_discovery_search_bar_hint
}