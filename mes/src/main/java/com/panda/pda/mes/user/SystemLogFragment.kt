package com.panda.pda.mes.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.common.data.model.CommonOperationRecordModel
import com.panda.pda.mes.databinding.FragmentSystemLogBinding
import com.panda.pda.mes.databinding.ItemSystemLogBinding
import com.panda.pda.mes.databinding.ItemTaskDetailOperateRecordBinding
import com.panda.pda.mes.user.data.UserApi
import com.panda.pda.mes.user.data.model.SystemLogModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * created by AnJiwei 2022/8/29
 */
class SystemLogFragment : BaseFragment(R.layout.fragment_system_log) {

    private var endTime: String? = null
    private var startTime: String? = null
    private val bindingAdapter by lazy { createRecordAdapter() }
    private val userViewModel by activityViewModels<UserViewModel>()

    private val viewBinding by viewBinding<FragmentSystemLogBinding>()

    private val datePicker by lazy {
        MaterialDatePicker.Builder.dateRangePicker().setTitleText(R.string.time_pick).build()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }
        viewBinding.rvLogList.adapter = bindingAdapter
        viewBinding.ibCleanText.setOnClickListener {
            viewBinding.etSearchBar.text = ""
            startTime = null
            endTime = null
            refreshData()
        }
        viewBinding.etSearchBar.setOnClickListener {
            showDateRangePicker()
        }
        datePicker.addOnPositiveButtonClickListener { onTimePicked(it) }
    }

    private fun onTimePicked(pair: Pair<Long, Long>) {
        startTime = convertLongToTime(pair.first)
        endTime = convertLongToTime(pair.second)
        viewBinding.etSearchBar.text = listOf(startTime, endTime).joinToString("-")
        refreshData()
    }

    private fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(date)
    }

    private fun showDateRangePicker() {
        datePicker.show(parentFragmentManager, TAG)
    }

    private fun createRecordAdapter(): CommonViewBindingAdapter<ItemSystemLogBinding, SystemLogModel> {
        return object :
            CommonViewBindingAdapter<ItemSystemLogBinding, SystemLogModel>() {
            override fun createBinding(parent: ViewGroup): ItemSystemLogBinding {
                return ItemSystemLogBinding.inflate(LayoutInflater.from(parent.context))
            }

            override fun onBindViewHolderWithData(
                holder: ViewBindingHolder,
                data: SystemLogModel,
                position: Int,
            ) {
                holder.itemViewBinding.apply {
                    tvLogType.text = data.operationFunction
                    tvLogInfo.text = data.operateDetail
                    tvLogTime.text = data.createTime
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

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    private fun refreshData() {
        val startDateTime = if (startTime != null) "$startTime 00:00:00" else null
        val endDataTime = if (endTime != null) "$endTime 00:00:00" else null
        WebClient.request(UserApi::class.java)
            .pdaSystemLogGet(startDateTime, endDataTime)
            .bindToFragment()
            .subscribe({
                bindingAdapter.refreshData(it.dataList)
            }, {})
    }
}