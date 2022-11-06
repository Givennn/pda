package com.panda.pda.mes.operation.bps.report_history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.panda.pda.mes.R
import com.panda.pda.mes.base.extension.getGenericObjectString
import com.panda.pda.mes.base.extension.putObjectString
import com.panda.pda.mes.base.extension.toast
import com.panda.pda.mes.base.retrofit.DataListNode
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.CommonSearchListFragment
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.common.data.model.CommonOperationRecordModel
import com.panda.pda.mes.databinding.ItemMainPlanReportBinding
import com.panda.pda.mes.databinding.ItemReportHistoryBinding
import com.panda.pda.mes.operation.bps.data.MainPlanApi
import com.panda.pda.mes.operation.bps.data.model.ReportModel
import com.squareup.moshi.Types
import io.reactivex.rxjava3.core.Single

/**
 * created by AnJiwei 2022/11/4
 */
class ReportHistoryFragment : CommonSearchListFragment<ReportModel>() {

    private lateinit var reportList: List<ReportModel>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewBinding.etSearchBar.isVisible = false
        val reports = arguments?.getGenericObjectString<List<ReportModel>>(
            Types.newParameterizedType(
                List::class.java,
                ReportModel::class.java
            ))
        if (reports == null) {
            toast(getString(R.string.net_work_error))
        } else {
            reportList = reports
        }
        super.onViewCreated(view, savedInstanceState)

    }

    override fun createAdapter(): CommonViewBindingAdapter<*, ReportModel> {
        return object :
            CommonViewBindingAdapter<ItemReportHistoryBinding, ReportModel>(reportList.toMutableList()) {
            override fun createBinding(parent: ViewGroup): ItemReportHistoryBinding {
                return ItemReportHistoryBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            }

            override fun onBindViewHolderWithData(
                holder: ViewBindingHolder,
                data: ReportModel,
                position: Int,
            ) {

                holder.itemViewBinding.apply {
                    tvReporter.text = data.createName
                    tvReportTime.text = data.createTime
                    tvRemark.text = data.remark
                    root.setOnClickListener {
                        navToDetail(data)
                    }
                }
            }

        }
    }

    private fun navToDetail(data: ReportModel) {
        WebClient.request(MainPlanApi::class.java).mainPlanReportHistoryGet(data.id)
            .bindToFragment()
            .subscribe({
                navController.navigate(R.id.action_mainPlanReportFragment_to_mainPlanReportInputFragment,
                    Bundle().apply {
                        putObjectString(it)
                    })
            }, {})
    }

    override fun api(key: String?): Single<DataListNode<ReportModel>> {
        return Single.just(DataListNode(reportList))
    }

    override val titleResId: Int
        get() = R.string.main_plan_report_history
}