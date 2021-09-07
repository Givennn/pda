package com.panda.pda.app.operation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseRecycleViewAdapter
import com.panda.pda.app.base.retrofit.BaseResponse
import com.panda.pda.app.base.retrofit.DataListNode
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.common.CommonSearchListFragment
import com.panda.pda.app.databinding.ItemAlarmHistoryBinding
import com.panda.pda.app.operation.data.AlarmApi
import com.panda.pda.app.operation.data.model.AlarmHistoryModel
import io.reactivex.rxjava3.core.Single

/**
 * created by AnJiwei 2021/8/31
 */
class AlarmHistoryFragment : CommonSearchListFragment<AlarmHistoryModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.etSearchBar.visibility = View.GONE
    }
    override fun createAdapter(): BaseRecycleViewAdapter<*, AlarmHistoryModel> {
        return object : BaseRecycleViewAdapter<ItemAlarmHistoryBinding, AlarmHistoryModel>(
            mutableListOf()) {
            override fun createBinding(parent: ViewGroup): ItemAlarmHistoryBinding {
                return ItemAlarmHistoryBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            }

            override fun onBindViewHolderWithData(
                holder: ViewBindingHolder,
                data: AlarmHistoryModel,
                position: Int,
            ) {
                holder.itemViewBinding.apply {
                    tvAlarmCode.text = data.alarmCode
                    tvAlarmStatus.text = data.status.toString()
                    tvReporter.text = data.createName
                    tvReportTime.text = data.createTime
                    tvAlarmDetail.text = data.alarmDetail
                    tvCloser.text = data.closeName
                    tvCloseTime.text = data.closeTime
                    tvCloseDetail.text = data.closeRemark
                    if (data.status == 0) { //TODO status charge
                        clClose.visibility = View.GONE
                    } else {
                        clClose.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun api(key: String?): Single<BaseResponse<DataListNode<AlarmHistoryModel>>> =
        WebClient.request(AlarmApi::class.java)
            .pdaFmsAlarmMyHistoryGet()
            .map {
                BaseResponse(it.message,
                    DataListNode(it.data?.dataList ?: listOf()),
                    it.code)
            }


    override val titleResId: Int
        get() = R.string.history_record

}