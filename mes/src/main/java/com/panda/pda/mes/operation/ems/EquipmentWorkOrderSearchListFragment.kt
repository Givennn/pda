package com.panda.pda.mes.operation.ems

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.library.android.controls.EndlessRecyclerViewScrollListener
import com.panda.pda.mes.R
import com.panda.pda.mes.base.ConfirmDialogFragment
import com.panda.pda.mes.base.extension.toast
import com.panda.pda.mes.base.retrofit.DataListNode
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.WheelPickerDialogFragment
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.common.data.CommonParameters
import com.panda.pda.mes.common.data.DataParamType
import com.panda.pda.mes.databinding.FrameEmptyViewBinding
import com.panda.pda.mes.operation.ems.data.EquipmentApi
import com.panda.pda.mes.operation.ems.data.model.EmsModelType
import com.panda.pda.mes.operation.ems.data.model.EquipmentOrgModel
import com.panda.pda.mes.operation.ems.data.model.EquipmentWorkOrderModel
import com.panda.pda.mes.operation.ems.equipment_workorder.EquipmentWorkOrderDetailFragment
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import io.reactivex.rxjava3.core.Single
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * created by AnJiwei 2021/9/28
 */
abstract class EquipmentWorkOrderSearchListFragment<TItemViewBinding : ViewBinding> :
    EquipmentCommonSearchListFragment<EquipmentWorkOrderModel>() {

    abstract val qualityTaskModelType: EmsModelType

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    //设施类型
    private var facilityType: Int = 1

    //设施的功能状态
    private var workorderStatus: String = ""

    //部门选择
    private var organizationId: String = ""
    private var headTypeData: MutableList<String> = mutableListOf()
    private var headStatusData: MutableList<String> = mutableListOf()

    //部门列表
    private var headOrgData: MutableList<EquipmentOrgModel> = mutableListOf()

    //只包含部门名称的列表，用户dialog
    private var headOrgStringData: MutableList<String> = mutableListOf()
    override val searchBarHintResId: Int?
        get() = R.string.quality_task_search_hint

    private val headTypeDialog by lazy {
        WheelPickerDialogFragment().also {
            it.pickerData = headTypeData
        }
    }
    private val headStatusDialog by lazy {
        WheelPickerDialogFragment().also {
            it.pickerData = headStatusData
        }
    }
    private val headTeamDialog: WheelPickerDialogFragment by lazy {
        WheelPickerDialogFragment().also {
            it.pickerData = headOrgStringData
        }
    }

    private fun showHeadTypeSelectDialog(
        tabType: Int,
        tvSelectValue: TextView,
        dialog: WheelPickerDialogFragment,
    ) {
        dialog.setConfirmButton { result ->
            result!!.first
            tvSelectValue.text = result.first
            when (tabType) {
                1 -> facilityType =
                    CommonParameters.getValue(DataParamType.FACILITY_TYPE, result.first)
                2 -> {
                    workorderStatus = if (result.first == "全部") {
                        ""
                    } else
                        CommonParameters.getValue(DataParamType.WORK_ORDER_STATUS, result.first)
                            .toString()
                }
                3 -> {
                    organizationId = if (result.first == "全部") {
                        ""
                    } else
                        headOrgData[result.second - 1].id
                }
            }
            refreshByType()
//            navController.navigate(R.id.equipmentMaintenanceHistoryFragment)
        }.setCancelButton { dialogInterface, i ->
            Timber.e("dialog关闭")
        }.show(parentFragmentManager, TAG)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        headOrgData.clear()
        headTypeData.clear()
        headStatusData.clear()
        headTypeData.addAll(CommonParameters.getParameters(DataParamType.FACILITY_TYPE)
            .sortedBy { it.paramValue }.map { it.paramDesc }.toMutableList())
        headStatusData.add("全部")
        headStatusData.addAll(CommonParameters.getParameters(DataParamType.WORK_ORDER_STATUS)
            .sortedBy { it.paramValue }.map { it.paramDesc }.toMutableList())
        getOrgList()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.e("navController.graph${navController.graph}")

        viewBinding.llHeadType.setOnClickListener {
            showHeadTypeSelectDialog(1, viewBinding.tvHeadType, headTypeDialog)
        }
        viewBinding.llHeadStatus.setOnClickListener {
            showHeadTypeSelectDialog(2, viewBinding.tvHeadStatus, headStatusDialog)
        }
        viewBinding.llHeadTeam.setOnClickListener {
            showHeadTypeSelectDialog(3, viewBinding.tvHeadTeam, headTeamDialog)
        }
        if (qualityTaskModelType == EmsModelType.Task) {
            //任务列表不展示筛选与搜索
            viewBinding.llSelect.visibility = View.GONE
            viewBinding.lineSelect.visibility = View.GONE
            viewBinding.lineSearch.visibility = View.GONE
            viewBinding.rlTopSearch.visibility = View.GONE

        } else if (qualityTaskModelType == EmsModelType.WORKORDER) {
            //工单列表不展示搜索
            viewBinding.lineSearch.visibility = View.GONE
            viewBinding.rlTopSearch.visibility = View.GONE
        }
        val layoutManager = viewBinding.rvTaskList.layoutManager as? LinearLayoutManager ?: return
        scrollListener = object :
            EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                if (qualityTaskModelType == EmsModelType.Task) {
                    WebClient.request(EquipmentApi::class.java)
                        .pdaEmsWorkOrderTaskByPageGet(
                            10,
                            page
                        )
                        .bindToFragment()
                        .subscribe({
                            itemListAdapter.addData(it.dataList)
                        }, { })
                } else {
                    WebClient.request(EquipmentApi::class.java)
                        .pdaEmsWorkOrderListByPageGet(
                            facilityType, workorderStatus, organizationId,
                            viewBinding.etSearchBar.text?.toString(),
                            10,
                            page
                        )
                        .bindToFragment()
                        .subscribe({
                            itemListAdapter.addData(it.dataList)
                        }, { })
                }
            }
        }
        viewBinding.rvTaskList.addOnScrollListener(scrollListener)
    }


    override fun createAdapter(): CommonViewBindingAdapter<*, EquipmentWorkOrderModel> {

        return object : CommonViewBindingAdapter<TItemViewBinding, EquipmentWorkOrderModel>() {
            override fun createBinding(parent: ViewGroup): TItemViewBinding {
                return createViewBinding(parent)
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
                data: EquipmentWorkOrderModel,
                position: Int,
            ) {
                holder.itemViewBinding.root.clicks()
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .bindToLifecycle(holder.itemView)
//                    .subscribe { showDetail(data) }
                    .subscribe {
                        navController.navigate(R.id.equipmentWorkOrderDetailFragment,
                            Bundle().apply {
                                //带入详情页的数据
                                putString(EquipmentWorkOrderDetailFragment.WORKORDER, data.id)
                            })
                    }
                onBindViewHolder(holder, data, position)
            }
        }
    }

    abstract fun createViewBinding(parent: ViewGroup): TItemViewBinding

    abstract fun onBindViewHolder(
        holder: CommonViewBindingAdapter<TItemViewBinding, EquipmentWorkOrderModel>.ViewBindingHolder,
        data: EquipmentWorkOrderModel,
        position: Int,
    )

    private fun showDetail(data: EquipmentWorkOrderModel) {
//        Single.zip(
//            WebClient.request(QualityApi::class.java).pdaQmsCommonDetailGet(data.id),
//            WebClient.request(QualityApi::class.java).pdaQmsCommonOperatorListGet(data.id),
//            { info, record -> Pair(info, record) })
//            .bindToFragment()
//            .subscribe({
////                qualityViewModel.qualityDetailInfoData.postValue(it.first)
////                qualityViewModel.qualityDetailRecordData.postValue(it.second)
//                navController.navigate(R.id.qualityDetailFragment, Bundle().apply {
//                    putObjectString(it.first)
//                    putGenericObjectString(
//                        it.second,
//                        Types.newParameterizedType(
//                            DataListNode::class.java,
//                            QualityTaskRecordModel::class.java
//                        )
//                    )
//                })
//            }, {})

    }


    fun refreshByType() {
        if (qualityTaskModelType == EmsModelType.Task) {
            WebClient.request(EquipmentApi::class.java)
                .pdaEmsWorkOrderTaskByPageGet()
                .bindToFragment()
                .subscribe({
                    itemListAdapter.refreshData(it.dataList)
                }, { })
        } else {
            WebClient.request(EquipmentApi::class.java)
                .pdaEmsWorkOrderListByPageGet(
                    facilityType,
                    workorderStatus,
                    organizationId,
                    viewBinding.etSearchBar.text?.toString()
                )
                .bindToFragment()
                .subscribe({
                    itemListAdapter.refreshData(it.dataList)
                }, { })
        }

    }

    override fun api(key: String?): Single<DataListNode<EquipmentWorkOrderModel>> {
        return if (qualityTaskModelType == EmsModelType.Task) {
            //待处理任务列表接口请求
            WebClient.request(EquipmentApi::class.java)
                .pdaEmsWorkOrderTaskByPageGet()
                .doFinally { scrollListener.resetState() }
        } else {
            //工单列表接口请求
            WebClient.request(EquipmentApi::class.java)
                .pdaEmsWorkOrderListByPageGet(facilityType, workorderStatus, organizationId, key)
                .doFinally { scrollListener.resetState() }
        }

    }

    //页面标题
    override val titleResId: Int
        get() = when (qualityTaskModelType) {
            EmsModelType.Task -> R.string.equipment_task
            EmsModelType.WORKORDER -> R.string.equipment_workorder
        }

    //获取部门列表
    private fun getOrgList() {
        WebClient.request(EquipmentApi::class.java)
            .pdaEmsOrgListGet()
            .subscribe({
                //顶部列表
                headOrgData = it.dataList.toMutableList()
                headOrgStringData.clear()
                if (headOrgData.isNotEmpty()) {
                    headOrgStringData.add("全部")
                }
                headOrgData.forEach { item ->
                    headOrgStringData.add(item.orgName)
                }
            }, {
            })

    }
}