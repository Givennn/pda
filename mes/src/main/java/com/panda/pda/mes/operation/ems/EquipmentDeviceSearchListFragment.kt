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
import com.panda.pda.mes.base.extension.putObjectString
import com.panda.pda.mes.base.retrofit.DataListNode
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.WheelPickerDialogFragment
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.common.data.CommonParameters
import com.panda.pda.mes.common.data.DataParamType
import com.panda.pda.mes.databinding.FrameEmptyViewBinding
import com.panda.pda.mes.operation.ems.data.EquipmentApi
import com.panda.pda.mes.operation.ems.data.model.EquipmentInfoDeviceModel
import com.panda.pda.mes.operation.ems.data.model.EquipmentOrgModel
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import io.reactivex.rxjava3.core.Single
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * created by AnJiwei 2021/9/28
 */
abstract class EquipmentDeviceSearchListFragment<TItemViewBinding : ViewBinding> :
    EquipmentCommonSearchListFragment<EquipmentInfoDeviceModel>() {


    //类型列表
    private lateinit var headTypeData: List<String>

    //状态列表
    private var headStatusData: MutableList<String> = mutableListOf()

    //部门列表
    private var headOrgData: MutableList<EquipmentOrgModel> = mutableListOf()

    //只包含部门名称的列表，用户dialog
    private var headOrgStringData: MutableList<String> = mutableListOf()

    //设施类型
    private var facilityType: Int = 1

    //设施的功能状态
    private var functionStatus: String = "0"

    //部门选择
    private var organizationId: String = "0"

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    //类型弹窗
    private val headTypeDialog by lazy {
        WheelPickerDialogFragment().also {
            it.pickerData = headTypeData
        }
    }

    //功能状态弹窗
    private val headStatusDialog by lazy {
        WheelPickerDialogFragment().also {
            it.pickerData = headStatusData
        }
    }

    //部门弹窗
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
//            tvSelectValue.text = result.first
//            toast("result:${dialog.pickerData[result.second]}")
//            for (parameter in CommonParameters.getParameters(DataParamType.FACILITY_TYPE)) {
//                if (parameter.paramDesc==result.first) {
//                    tvSelectValue.text=CommonParameters.getValue(DataParamType.FACILITY_TYPE,result.first).toString()
//                }
//            }
            when (tabType) {
                1 -> facilityType =
                    CommonParameters.getValue(DataParamType.FACILITY_TYPE, result.first)
                2 -> {
                    functionStatus = if (result.first == "全部") {
                        0.toString()
                    } else
                        CommonParameters.getValue(DataParamType.FUNCTION_STATUS_PDA, result.first)
                            .toString()
                }
                3 -> {
                    organizationId = if (result.first == "全部") {
                        0.toString()
                    } else
                        headOrgData[result.second - 1].id
                }
            }
//            navController.navigate(R.id.equipmentInfoWorkOrderAddFragment)
            //筛选后刷新列表

            refreshByType()
        }.setCancelButton { dialogInterface, i ->
            Timber.e("dialog关闭")
        }.show(parentFragmentManager, TAG)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        headTypeData = CommonParameters.getParameters(DataParamType.FACILITY_TYPE)
            .sortedBy { it.paramValue }.map { it.paramDesc }
        headStatusData.clear()
        headOrgData.clear()
        headStatusData.clear()
        headStatusData.add("全部")
        headStatusData.addAll(CommonParameters.getParameters(DataParamType.FUNCTION_STATUS_PDA)
            .sortedBy { it.paramValue }.map { it.paramDesc }.toMutableList())
        getOrgList()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.llHeadType.setOnClickListener {
            if (headTypeData.isNotEmpty()) {
                showHeadTypeSelectDialog(1, viewBinding.tvHeadType, headTypeDialog)
            }
        }
        viewBinding.llHeadStatus.setOnClickListener {
            if (headStatusData.isNotEmpty()) {
                showHeadTypeSelectDialog(2, viewBinding.tvHeadStatus, headStatusDialog)
            }
        }
        viewBinding.llHeadTeam.setOnClickListener {
            if (headOrgStringData.isNotEmpty()) {
                showHeadTypeSelectDialog(3, viewBinding.tvHeadTeam, headTeamDialog)
            }

        }
        val layoutManager = viewBinding.rvTaskList.layoutManager as? LinearLayoutManager ?: return
        scrollListener = object :
            EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                WebClient.request(EquipmentApi::class.java)
                    .pdaEmsDeviceListByPageGet(
                        facilityType,
                        functionStatus,
                        organizationId,
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
        viewBinding.rvTaskList.addOnScrollListener(scrollListener)
    }

    fun refreshByType() {
        WebClient.request(EquipmentApi::class.java)
            .pdaEmsDeviceListByPageGet(
                facilityType,
                functionStatus,
                organizationId,
                viewBinding.etSearchBar.text?.toString()
            )
            .bindToFragment()
            .subscribe({
                itemListAdapter.refreshData(it.dataList)
            }, { })
    }

    override fun createAdapter(): CommonViewBindingAdapter<*, EquipmentInfoDeviceModel> {

        return object : CommonViewBindingAdapter<TItemViewBinding, EquipmentInfoDeviceModel>() {
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
                data: EquipmentInfoDeviceModel,
                position: Int,
            ) {
                holder.itemViewBinding.root.clicks()
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .bindToLifecycle(holder.itemView)
//                    .subscribe { showDetail(data) }
                    .subscribe {
                        navController.navigate(R.id.equipmentInfoDetailFragment, Bundle().apply {
                            //带入详情页的数据
                            putObjectString(data)
                        })
                    }
                onBindViewHolder(holder, data, position)
            }
        }
    }

    abstract fun createViewBinding(parent: ViewGroup): TItemViewBinding

    abstract fun onBindViewHolder(
        holder: CommonViewBindingAdapter<TItemViewBinding, EquipmentInfoDeviceModel>.ViewBindingHolder,
        data: EquipmentInfoDeviceModel,
        position: Int,
    )

    //点击跳转详情
    private fun showDetail(data: EquipmentInfoDeviceModel) {
        if (data.facilityType == "1") {
            //设备详情
            WebClient.request(EquipmentApi::class.java).pdaEmsDeviceDetailGet(data.facilityId)
                .bindToFragment()
                .subscribe({
                    navController.navigate(R.id.equipmentInfoDetailFragment, Bundle().apply {
                        //带入详情页的数据
                        putObjectString(it)
                    })
                }, {})
        } else if (data.facilityType == "2") {
            //模具详情
            WebClient.request(EquipmentApi::class.java).pdaEmsMouldDetailGet(data.facilityId)
                .bindToFragment()
                .subscribe({
                    navController.navigate(R.id.equipmentInfoDetailFragment, Bundle().apply {
                        //带入详情页的数据
                        putObjectString(it)
                    })
                }, {})
        }
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

    override fun api(key: String?): Single<DataListNode<EquipmentInfoDeviceModel>> {
        return WebClient.request(EquipmentApi::class.java)
            .pdaEmsDeviceListByPageGet(facilityType, functionStatus, organizationId, key)
            .doFinally { scrollListener.resetState() }
    }

    override val titleResId: Int
        get() = R.string.equipment_info


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