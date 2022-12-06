package com.panda.pda.mes.operation.fms.mission.report_history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.panda.pda.mes.R
import com.panda.pda.mes.base.extension.getGenericObjectString
import com.panda.pda.mes.base.extension.getStringObject
import com.panda.pda.mes.base.extension.putObjectString
import com.panda.pda.mes.base.extension.toast
import com.panda.pda.mes.base.retrofit.DataListNode
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.CommonSearchListFragment
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.databinding.ItemDispatchOrderReportHistoryBinding
import com.panda.pda.mes.databinding.ItemReportHistoryBinding
import com.panda.pda.mes.operation.bps.data.MainPlanApi
import com.panda.pda.mes.operation.bps.data.model.ReportModel
import com.panda.pda.mes.operation.fms.data.TaskApi
import com.panda.pda.mes.operation.fms.data.model.DispatchOrderModel
import com.panda.pda.mes.operation.fms.data.model.DispatchOrderReportHistoryModel
import com.squareup.moshi.Types
import io.reactivex.rxjava3.core.Single

/**
 * created by AnJiwei 2022/11/30
 */
class DispatchOrderReportHistoryFragment: CommonSearchListFragment<DispatchOrderReportHistoryModel>() {

    private var dispatchOrder: DispatchOrderModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewBinding.tilSearchBar.isVisible = false
        dispatchOrder = arguments?.getStringObject<DispatchOrderModel>()
        if (dispatchOrder == null) {
            toast(getString(R.string.net_work_error))
            navController.popBackStack()
            return
        }
        itemListAdapter.refreshData(dispatchOrder?.reportRecordList ?: listOf() )
        super.onViewCreated(view, savedInstanceState)

    }

    override fun createAdapter(): CommonViewBindingAdapter<*, DispatchOrderReportHistoryModel> {
        return object :
            CommonViewBindingAdapter<ItemDispatchOrderReportHistoryBinding, DispatchOrderReportHistoryModel>() {
            override fun createBinding(parent: ViewGroup): ItemDispatchOrderReportHistoryBinding {
                return ItemDispatchOrderReportHistoryBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            }

            override fun onBindViewHolderWithData(
                holder: ViewBindingHolder,
                data: DispatchOrderReportHistoryModel,
                position: Int,
            ) {

                holder.itemViewBinding.apply {
                    tvReporter.text = data.createName
                    tvReportTime.text = data.createTime
                    tvRemark.text = data.remark
                    tvReportNumber.text = data.reportNumber.toString()
                    root.setOnClickListener {
                        navToDetail(data)
                    }
                }
            }

        }
    }

    private fun navToDetail(data: DispatchOrderReportHistoryModel) {
        WebClient.request(TaskApi::class.java).taskGetByIdGet(dispatchOrder!!.id)
            .bindToFragment()
            .subscribe({
                navController.navigate(R.id.action_dispatchOrderReportHistoryFragment_to_dispatchOrderReportHistoryDetailFragment,
                    Bundle().apply {
                        putObjectString(it)
                        putObjectString(data)
                    })
            }, {})
    }

    override fun api(key: String?): Single<DataListNode<DispatchOrderReportHistoryModel>> {
        return Single.never()
    }

    override fun refreshData() {
        return
    }

    override val titleResId: Int
        get() = R.string.main_plan_report_history
}