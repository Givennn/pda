package com.panda.pda.mes.operation.ems.equipment_workorder

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.library.android.controls.EndlessRecyclerViewScrollListener
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.extension.putObjectString
import com.panda.pda.mes.base.retrofit.DataListNode
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.ModelPropertyCreator
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.common.data.MesStringUtils
import com.panda.pda.mes.databinding.*
import com.panda.pda.mes.operation.ems.data.EquipmentApi
import com.panda.pda.mes.operation.ems.data.model.EquipmentInfoMatrixMaintainRepairItemModel
import com.panda.pda.mes.operation.ems.data.model.EquipmentInfoMatrixStoreItemModel
import com.panda.pda.mes.operation.ems.equipment_workorder.EquipmentWorkOrderDetailFragment.Companion.WORKORDER
import com.panda.pda.mes.operation.qms.data.model.QualityDetailModel
import com.panda.pda.mes.operation.qms.data.model.QualitySubTaskDetailModel
import com.panda.pda.mes.operation.qms.data.model.QualityTaskRecordModel
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit

/**
 * 维修记录,传入设备或者模具id，传入设备或者模具类型，1：设备，2：模具
 */
class EquipmentMaintenanceHistoryListFragment(
    private var facilityId: String,
    private var facilityType: String,
) :
    BaseFragment(R.layout.fragment_equipment_workorder_maintenance_detail_record) {

    private val bindingAdapter by lazy { createRecordAdapter() }
    private val viewBinding by viewBinding<FragmentEquipmentWorkorderMaintenanceDetailRecordBinding>()
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    //出入库记录列表
    private var dataList = mutableListOf<EquipmentInfoMatrixMaintainRepairItemModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (facilityId.isEmpty()) {
            facilityId = "1"
        }
        if (facilityType.isEmpty()) {
            facilityType = "1"
        }
        val layoutManager = viewBinding.rvRecords.layoutManager as? LinearLayoutManager ?: return
        scrollListener = object :
            EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                if (facilityType == "1") {
                    //设备的维修列表
                    WebClient.request(EquipmentApi::class.java)
                        .pdaEmsEquipmentRepairOrderListGet(MesStringUtils.stringToInt(facilityId),
                            10,
                            page
                        )
                        .bindToFragment()
                        .subscribe({
                            bindingAdapter.addData(it.dataList)
                        }, { })
                } else {
                    //模具的维修列表
                    WebClient.request(EquipmentApi::class.java)
                        .pdaEmsModuleRepairOrderListGet(MesStringUtils.stringToInt(facilityId),
                            10,
                            page
                        )
                        .bindToFragment()
                        .subscribe({
                            bindingAdapter.addData(it.dataList)
                        }, { })
                }
            }
        }
        viewBinding.rvRecords.addOnScrollListener(scrollListener)
        viewBinding.rvRecords.adapter = bindingAdapter
        viewBinding.swipe.setOnRefreshListener { refreshData() }
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    fun refreshData() {
        api(MesStringUtils.stringToInt(facilityId), facilityType)
            .bindToFragment()
            .doFinally { viewBinding.swipe.isRefreshing = false }
            .subscribe({ data ->
                bindingAdapter.refreshData(data.dataList)
                dataList = data.dataList as MutableList<EquipmentInfoMatrixMaintainRepairItemModel>
            }, { })
    }

    fun api(
        facilityId: Int,
        facilityType: String,
    ): Single<DataListNode<EquipmentInfoMatrixMaintainRepairItemModel>> {
        if (facilityType == "1") {
            //设备查询维修记录
            return WebClient.request(EquipmentApi::class.java)
                .pdaEmsEquipmentRepairOrderListGet(facilityId)
                .doFinally { scrollListener.resetState() }
        } else {
            //模具查询维修记录
            return WebClient.request(EquipmentApi::class.java)
                .pdaEmsModuleRepairOrderListGet(facilityId)
                .doFinally { scrollListener.resetState() }
        }
    }

    private fun createRecordAdapter(): CommonViewBindingAdapter<ItemEquipmentWorkorderRecordBinding, EquipmentInfoMatrixMaintainRepairItemModel> {
        return object :
            CommonViewBindingAdapter<ItemEquipmentWorkorderRecordBinding, EquipmentInfoMatrixMaintainRepairItemModel>() {
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
                data: EquipmentInfoMatrixMaintainRepairItemModel,
                position: Int,
            ) {
                holder.itemViewBinding.apply {
                    tvTaskId.text = "单号：${data.workOrderCode}"
                    var title = ""
                    if (data.facilityType == "1") {
                        title = "设备：${data.facilityName}"
                    } else if (data.facilityType == "2") {
                        title = "模具：${data.facilityName}"
                    }
                    tvTaskInfo.text = title
                    tvTaskNumber.text = data.facilityModel
                    tvTaskMemberName.text = data.assignName
                    if (data.workOrderStatus == "1") {
                        //待分配
                        ivTaskStatus.setImageResource(R.drawable.icon_equipment_order_status_fenpei)
                    } else if (data.workOrderStatus == "2") {
                        //待完工
                        ivTaskStatus.setImageResource(R.drawable.icon_equipment_order_status_daiwangong)
                    } else if (data.workOrderStatus == "3") {
                        //待确认
                        ivTaskStatus.setImageResource(R.drawable.icon_equipment_order_status_daiqueren)
                    } else {
                        if (data.conclusion == "2") {
                            //不合格
                            ivTaskStatus.setImageResource(R.drawable.icon_equipment_order_status_fail)
                        } else {
                            ivTaskStatus.setImageResource(R.drawable.icon_equipment_order_status_ok)
                        }
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
                                    putString(WORKORDER, data.id.toString())
                                })
                        }
                }
            }
        }
    }
}