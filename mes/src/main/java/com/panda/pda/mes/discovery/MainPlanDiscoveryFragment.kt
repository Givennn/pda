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
import com.panda.pda.mes.databinding.ItemMainPlanDiscoveryBinding
import com.panda.pda.mes.databinding.ItemMainPlanFinishBinding
import com.panda.pda.mes.discovery.data.DiscoveryApi
import com.panda.pda.mes.operation.bps.data.MainPlanApi
import com.panda.pda.mes.operation.bps.data.model.MainPlanModel
import com.squareup.moshi.Types
import io.reactivex.rxjava3.core.Single

/**
 * created by AnJiwei 2022/8/30
 */
class MainPlanDiscoveryFragment: CommonSearchListFragment<MainPlanModel>()  {

    override fun createAdapter(): CommonViewBindingAdapter<*, MainPlanModel> {
        return object : CommonViewBindingAdapter<ItemMainPlanDiscoveryBinding, MainPlanModel>() {
            override fun createBinding(parent: ViewGroup): ItemMainPlanDiscoveryBinding {
                return ItemMainPlanDiscoveryBinding.inflate(
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
                data: MainPlanModel,
                position: Int,
            ) {
                holder.itemViewBinding.apply {
                    tvMainPlanInfo.text = data.planNo
                    tvProductInfo.text = listOf(data.productName,
                        data.productCode, data.productModel).joinToString(" ")
                    tvDeliverDate.text =
                        getString(R.string.plan_deliver_time_formatter, data.deliveryTime)
                    tvMainPlanNum.text = data.planNumber.toString()
                    clInfo.setOnClickListener {
                        onItemInfoClicked(data)
                    }
                }
            }

        }
    }

    private fun onItemInfoClicked(data: MainPlanModel) {
        showDetail(data)
    }

    private fun showDetail(data: MainPlanModel) {
        Single.zip(
            WebClient.request(MainPlanApi::class.java).mainPlanDetailGet(data.id),
            WebClient.request(MainPlanApi::class.java).mainPlanOperationRecordGet(data.id)
        ) { info, record -> Pair(info, record) }
            .bindToFragment()
            .subscribe({
                navController.navigate(R.id.action_mainPlanDiscoveryFragment_to_mainPlanDetailFragment3,
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

    override fun api(key: String?): Single<DataListNode<MainPlanModel>> =
        WebClient.request(DiscoveryApi::class.java)
            .mainPlanDiscoveryListGet(key)

    override val titleResId: Int
        get() = R.string.main_plan

    override val searchBarHintResId: Int
        get() = R.string.main_plan_discovery_search_bar_hint
}