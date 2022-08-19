package com.panda.pda.mes.operation.bps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.panda.pda.mes.R
import com.panda.pda.mes.base.extension.putGenericObjectString
import com.panda.pda.mes.base.extension.putObjectString
import com.panda.pda.mes.base.retrofit.DataListNode
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.CommonSearchListFragment
import com.panda.pda.mes.common.DateUtils
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.databinding.FrameEmptyViewBinding
import com.panda.pda.mes.databinding.ItemMainPlanReportBinding
import com.panda.pda.mes.databinding.ItemTaskReportBinding
import com.panda.pda.mes.operation.bps.data.MainPlanApi
import com.panda.pda.mes.operation.bps.data.model.MainPlanModel
import com.panda.pda.mes.operation.qms.data.model.QualityTaskRecordModel
import com.squareup.moshi.Types
import io.reactivex.rxjava3.core.Single

/**
 * created by AnJiwei 2022/8/8
 */
class MainPlanReportFragment : CommonSearchListFragment<MainPlanModel>() {

    override fun createAdapter(): CommonViewBindingAdapter<*, MainPlanModel> {
        return object : CommonViewBindingAdapter<ItemMainPlanReportBinding, MainPlanModel>() {
            override fun createBinding(parent: ViewGroup): ItemMainPlanReportBinding {
                return ItemMainPlanReportBinding.inflate(
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
                navController.navigate(R.id.action_mainPlanReportFragment_to_mainPlanDetailFragment,
                    Bundle().apply {
                        putObjectString(it.first)
                        putGenericObjectString(
                            it.second,
                            Types.newParameterizedType(
                                DataListNode::class.java,
                                QualityTaskRecordModel::class.java
                            )
                        )
                    })
            }, {})
    }

    private fun onItemActionClicked(data: MainPlanModel) {
        WebClient.request(MainPlanApi::class.java).mainPlanDetailGet(data.id)
            .bindToFragment()
            .subscribe({
                navController.navigate(R.id.action_mainPlanReportFragment_to_mainPlanReportInputFragment,
                    Bundle().apply {
                        putObjectString(it)
                    })
            }, {})
    }

    override fun api(key: String?): Single<DataListNode<MainPlanModel>> =
        WebClient.request(MainPlanApi::class.java)
            .mainPlanReportListGet(key)

    override val titleResId: Int
        get() = R.string.main_plan_report
}