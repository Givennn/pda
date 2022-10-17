package com.panda.pda.mes.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.panda.pda.library.android.controls.EndlessRecyclerViewScrollListener
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.extension.putGenericObjectString
import com.panda.pda.mes.base.extension.putObjectString
import com.panda.pda.mes.base.retrofit.DataListNode
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.common.data.model.CommonOperationRecordModel
import com.panda.pda.mes.databinding.FragmentSystemLogBinding
import com.panda.pda.mes.databinding.ItemPersonalPerformanceBinding
import com.panda.pda.mes.databinding.ItemSystemLogBinding
import com.panda.pda.mes.discovery.data.DiscoveryApi
import com.panda.pda.mes.operation.fms.data.GuideApi
import com.panda.pda.mes.user.data.UserApi
import com.panda.pda.mes.user.data.model.PersonalPerformanceModel
import com.panda.pda.mes.user.data.model.SystemLogModel
import com.squareup.moshi.Types
import io.reactivex.rxjava3.core.Single
import java.text.SimpleDateFormat
import java.util.*

/**
 * created by AnJiwei 2022/9/2
 */
class PersonalPerformanceFragment : BaseFragment(R.layout.fragment_personal_performance) {

    private var startTime: String? = null
    private val bindingAdapter by lazy { createRecordAdapter() }
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    private val viewBinding by viewBinding<FragmentSystemLogBinding>()

    private val datePicker by lazy {
        MaterialDatePicker.Builder.datePicker().setTitleText(R.string.time_pick).build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }
        viewBinding.rvLogList.adapter = bindingAdapter
        viewBinding.ibCleanText.setOnClickListener {
            viewBinding.etSearchBar.text = ""
            startTime = null
            refreshData()
        }
        viewBinding.etSearchBar.setOnClickListener {
            showDateRangePicker()
        }
        datePicker.addOnPositiveButtonClickListener { onTimePicked(it) }

        val layoutManager = viewBinding.rvLogList.layoutManager as? LinearLayoutManager ?: return
        scrollListener = object :
            EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                WebClient.request(UserApi::class.java)
                    .pdaPersonalPerformanceGet(viewBinding.etSearchBar.text?.toString(), page)
                    .bindToFragment()
                    .subscribe({
                        bindingAdapter.addData(it.dataList)
                    }, { })
            }
        }
        viewBinding.rvLogList.addOnScrollListener(scrollListener)
    }

    private fun onTimePicked(time: Long) {
        startTime = convertLongToTime(time)
        viewBinding.etSearchBar.text = startTime
        refreshData()
    }

    private fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        return format.format(date)
    }

    private fun showDateRangePicker() {
        datePicker.show(parentFragmentManager, TAG)
    }

    private fun createRecordAdapter(): CommonViewBindingAdapter<ItemPersonalPerformanceBinding, PersonalPerformanceModel> {
        return object :
            CommonViewBindingAdapter<ItemPersonalPerformanceBinding, PersonalPerformanceModel>() {
            override fun createBinding(parent: ViewGroup): ItemPersonalPerformanceBinding {
                return ItemPersonalPerformanceBinding.inflate(LayoutInflater.from(parent.context))
            }

            override fun onBindViewHolderWithData(
                holder: ViewBindingHolder,
                data: PersonalPerformanceModel,
                position: Int,
            ) {
                holder.itemViewBinding.apply {
                    tvRealMoney.text =
                        getString(R.string.real_performance_formatter, data.realAmount)
                    tvLogTime.text =
                        getString(R.string.statistic_time_formatter, data.statisticsTime)
                    tvPerformance.text =
                        getString(R.string.performance_rank_formatter, data.performance)
                    root.setOnClickListener {
                        showPerformanceDetail(data)
                    }
//                    tvLogType.text = data.operationFunction
//                    tvLogInfo.text = data.operateDetail
//                    tvLogTime.text = data.createTime
//                    viewStepLineTop.isVisible = holder.layoutPosition != 0
//                    viewStepLineBottom.isVisible = holder.bindingAdapterPosition != itemCount - 1
//                    tvRecordType.text = data.operateType
//                    tvOperator.text = data.operateName
//                    tvTime.text = data.createTime
//                    tvDesc.text = data.remark
                }
            }
        }
    }

    private fun showPerformanceDetail(data: PersonalPerformanceModel) {

        Single.zip(
            WebClient.request(UserApi::class.java).pdaPerformanceDetailGet(data.id),
            WebClient.request(UserApi::class.java).pdaPerformanceOperateRecord(data.id)
        ) { info, record -> kotlin.Pair(info, record) }
            .bindToFragment()
            .subscribe({
                navController.navigate(R.id.action_personalPerformanceFragment_to_performanceDetailFragment, Bundle().apply {
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

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    private fun refreshData() {
        var startDateTime = if (startTime != null) "$startTime-01 00:00:00" else null
        if (startDateTime == null) {
            val date = Date()
            date.month = date.month - 1
            val format = SimpleDateFormat("yyyy-MM-dd 00:00:00", Locale.getDefault())

            startDateTime = format.format(date)
        }
        WebClient.request(UserApi::class.java)
            .pdaPersonalPerformanceGet(startDateTime)
            .bindToFragment()
            .subscribe({
                bindingAdapter.refreshData(it.dataList)
            }, {})
    }
}