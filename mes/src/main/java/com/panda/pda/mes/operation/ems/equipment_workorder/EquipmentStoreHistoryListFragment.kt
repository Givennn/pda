package com.panda.pda.mes.operation.ems.equipment_workorder

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.library.android.controls.EndlessRecyclerViewScrollListener
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.retrofit.DataListNode
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.common.data.MesStringUtils
import com.panda.pda.mes.databinding.*
import com.panda.pda.mes.operation.ems.data.EquipmentApi
import com.panda.pda.mes.operation.ems.data.model.EquipmentInfoMatrixStoreItemModel
import com.panda.pda.mes.operation.ems.equipment_workorder.maintenance.EquipmentWorkOrderWaitFenpeiFragment
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit

/**
 * 出入库记录
 */
class EquipmentStoreHistoryListFragment :
    BaseFragment(R.layout.fragment_equipment_workorder_store_detail_record) {

    private val bindingAdapter by lazy { createRecordAdapter() }
    private val viewBinding by viewBinding<FragmentEquipmentWorkorderStoreDetailRecordBinding>()
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    //出入库记录列表
    private var facilityId = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        facilityId = arguments?.getString(FACILITYID).toString()
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }
        val layoutManager = viewBinding.rvRecords.layoutManager as? LinearLayoutManager ?: return
        scrollListener = object :
            EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                WebClient.request(EquipmentApi::class.java)
                    .pdaEmsStoreOrderListGet(MesStringUtils.stringToInt(facilityId),
                        10,
                        page
                    )
                    .bindToFragment()
                    .subscribe({
                        bindingAdapter.addData(it.dataList)
                    }, { })
            }
        }
        viewBinding.rvRecords.addOnScrollListener(scrollListener)
        viewBinding.rvRecords.adapter = bindingAdapter
        viewBinding.swipe.setOnRefreshListener { refreshData() }
        refreshData()
    }

    fun refreshData() {
        api(MesStringUtils.stringToInt(facilityId))
            .bindToFragment()
            .doFinally { viewBinding.swipe.isRefreshing = false }
            .subscribe({ data ->
                bindingAdapter.refreshData(data.dataList)
            }, { })
    }

    fun api(orderId: Int): Single<DataListNode<EquipmentInfoMatrixStoreItemModel>> {
        return WebClient.request(EquipmentApi::class.java)
            .pdaEmsStoreOrderListGet(orderId)
            .doFinally { scrollListener.resetState() }
    }

    private fun createRecordAdapter(): CommonViewBindingAdapter<ItemEquipmentWorkorderRecordBinding, EquipmentInfoMatrixStoreItemModel> {
        return object :
            CommonViewBindingAdapter<ItemEquipmentWorkorderRecordBinding, EquipmentInfoMatrixStoreItemModel>() {
            override fun createBinding(parent: ViewGroup): ItemEquipmentWorkorderRecordBinding {
                return ItemEquipmentWorkorderRecordBinding.inflate(LayoutInflater.from(parent.context))
            }
            override fun createEmptyViewBinding(parent: ViewGroup): ViewBinding {
                return FrameEmptyViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            }
            @SuppressLint("SetTextI18n")
            override fun onBindViewHolderWithData(
                holder: ViewBindingHolder,
                data: EquipmentInfoMatrixStoreItemModel,
                position: Int,
            ) {
                holder.itemViewBinding.apply {
                    tvTaskId.text = "单号：${data.workOrderCode}"
                    var title=""
                    if (data.facilityType=="1") {
                        title="模具：${data.facilityName}"
                    }else if (data.facilityType=="2"){
                        title="设备：${data.facilityName}"
                    }
                    tvTaskInfo.text=title
                    tvTaskNumber.text=data.facilityModel
                    tvTaskMemberName.text=data.submitName
//                    if (TextUtils.isEmpty(data.updateName)) {
//                        tvUpdateLabel.visibility=View.INVISIBLE
//                        tvTaskMemberName.visibility=View.INVISIBLE
//                    }else{
//                        tvTaskMemberName.text=data.updateName
//                        tvTaskMemberName.visibility=View.VISIBLE
//                        tvUpdateLabel.visibility=View.VISIBLE
//                    }
                    if (data.workOrderStatus=="7") {
                        //待出库
                        ivTaskStatus.setImageResource(R.drawable.icon_equipment_order_status_waitchuku)
                    }else if(data.workOrderStatus=="8"){
                        //待入库
                        ivTaskStatus.setImageResource(R.drawable.icon_equipment_order_status_ruku)
                    }else if(data.workOrderStatus=="9"){
                        //入库待确认
                        ivTaskStatus.setImageResource(R.drawable.icon_equipment_order_status_wait_ruku)
                    }else if(data.workOrderStatus=="11"){
                        ivTaskStatus.setImageResource(R.drawable.icon_equipment_order_status_baofei)
                    }else{
                        ivTaskStatus.setImageResource(R.drawable.icon_equipment_order_status_zaiku)
                    }
                    if (TextUtils.isEmpty(data.updateName)) {
                        tvUpdateLabel.visibility = View.INVISIBLE
                        tvTaskMemberName.visibility = View.INVISIBLE
                    } else {
                        tvTaskMemberName.text = data.updateName
                        tvUpdateLabel.text = "更新人"
                        tvTaskMemberName.visibility = View.VISIBLE
                        tvUpdateLabel.visibility = View.VISIBLE
                    }
                    tvRemark.text = data.remark
                    root.clicks()
                        .throttleFirst(500, TimeUnit.MILLISECONDS)
                        .bindToLifecycle(holder.itemView)
//                    .subscribe { showDetail(data) }
                        .subscribe {
                            navController.navigate(R.id.equipmentWorkOrderDetailFragment,
                                Bundle().apply {
                                    //带入详情页的数据
                                    putString(EquipmentWorkOrderDetailFragment.WORKORDER, data.id.toString())
                                })
                        }
                }
            }
        }
    }
    companion object {
        const val FACILITYID = "facilityId"
    }
}