package com.panda.pda.mes.operation.ems.equipment_info

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.extension.getStringObject
import com.panda.pda.mes.base.extension.putObjectString
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.common.data.CommonParameters
import com.panda.pda.mes.common.data.DataParamType
import com.panda.pda.mes.common.data.MesStringUtils
import com.panda.pda.mes.common.data.model.FileInfoModel
import com.panda.pda.mes.databinding.FragmentEquipmentInfoDetailBinding
import com.panda.pda.mes.databinding.ItemProblemRecordDetailFileBinding
import com.panda.pda.mes.operation.ems.EquipmentGuidePdfPreviewFragment
import com.panda.pda.mes.operation.ems.data.EquipmentApi
import com.panda.pda.mes.operation.ems.data.model.EquipmentInfoDeviceModel
import com.panda.pda.mes.operation.ems.data.model.EquipmentWorkOrderModel
import com.panda.pda.mes.operation.ems.equipment_workorder.EquipmentInfoWorkOrderAddFragment
import com.panda.pda.mes.operation.ems.equipment_workorder.EquipmentProduceHistoryListFragment
import com.panda.pda.mes.operation.ems.equipment_workorder.EquipmentStoreHistoryListFragment
import com.panda.pda.mes.operation.ems.equipment_workorder.maintenance.EquipmentInfoWorkOrderMaintenanceCompleteFragment
import com.panda.pda.mes.operation.ems.equipment_workorder.maintenance.EquipmentWorkOrderWaitCompleteFragment
import com.panda.pda.mes.operation.ems.equipment_workorder.maintenance.EquipmentWorkOrderWaitFenpeiFragment
import com.panda.pda.mes.operation.ems.equipment_workorder.store.EquipmentInfoWorkOrderConfirmOutStoreFragment
import com.panda.pda.mes.operation.ems.equipment_workorder.store.EquipmentInfoWorkOrderInStoreCompleteFragment
import com.panda.pda.mes.operation.ems.equipment_workorder.store.EquipmentInfoWorkOrderWaitInStoreFragment
import com.panda.pda.mes.operation.fms.guide.GuidePdfPreviewFragment
import java.util.concurrent.TimeUnit

/**
 * 模具详情
 */
class EquipmentInfoDetailFragment : BaseFragment(R.layout.fragment_equipment_info_detail) {

    private val viewBinding by viewBinding<FragmentEquipmentInfoDetailBinding>()

    private lateinit var fileAdapter: CommonViewBindingAdapter<*, FileInfoModel>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }
        //设备或者模具信息
        val deviceModel = arguments?.getStringObject<EquipmentInfoDeviceModel>()
        if (deviceModel != null) {
            //设备详情进入
            showDetail(deviceModel.facilityType, deviceModel.facilityId)
        } else {
            //工单详情进入
            val workOrderModel = arguments?.getStringObject<EquipmentWorkOrderModel>()
            workOrderModel?.let {
                showDetail(workOrderModel.facilityType,
                    workOrderModel.facilityId)
            }
        }
    }
    //获取模次数列表
    private fun getRecord(deviceModel: EquipmentInfoDeviceModel) {
        WebClient.request(EquipmentApi::class.java)
            .pdaEmsProcedureListGet(deviceModel.facilityId.toString())
            .bindToFragment()
            .subscribe({
                navController.navigate(
                    R.id.equipmentProduceHistoryListFragment,
                    Bundle().apply {
                        putString(
                            EquipmentProduceHistoryListFragment.DETAIL_KEY,
                            EquipmentProduceHistoryListFragment.getProduceRecordJsonAdapter()
                                .toJson(it.dataList)
                        )
                        //带入模次数列表
                        putObjectString(deviceModel, "deviceModel")
                    })
            }, {})

    }

    //设置底部按钮
    private fun showBottomBtn(deviceModel: EquipmentInfoDeviceModel) {
        if (deviceModel.facilityType == "1" && deviceModel.functionStatus == 1 && (deviceModel.maintainAddPermission || deviceModel.repairAddPermission)) {
            //设备且功能状态为正常,且有权限时可以填报工单
            viewBinding.btnConfirm.isVisible = true
            //底部按钮文案展示
            viewBinding.btnConfirm.text = "工单填报"
            viewBinding.btnConfirm.setOnClickListener {
                navController.navigate(R.id.equipmentInfoWorkOrderAddFragment,
                    Bundle().apply {
                        putString(EquipmentInfoWorkOrderAddFragment.FACILITYID,
                            deviceModel.facilityId.toString())
                        putObjectString(deviceModel)
                    })
            }
        } else if (deviceModel.facilityType == "2" && deviceModel.functionStatus == 1 && deviceModel.locationStatus == 1 && (deviceModel.maintainAddPermission || deviceModel.repairAddPermission || deviceModel.stockAddPerssion)) {
            //模具且功能状态为正常且在库,且有权限时可以填报工单
            viewBinding.btnConfirm.isVisible = true
            viewBinding.btnConfirm.text = "工单填报"
            viewBinding.btnConfirm.setOnClickListener {
                navController.navigate(R.id.equipmentInfoWorkOrderAddFragment,
//                    Bundle().apply {
//                        putString(EquipmentInfoWorkOrderAddFragment.FACILITYID,
//                            deviceModel.facilityId.toString())
//                        putBoolean(EquipmentInfoWorkOrderAddFragment.MAINTAINADDPERMISSION,
//                            deviceModel.maintainAddPermission)
//                        putBoolean(EquipmentInfoWorkOrderAddFragment.REPAIRADDPERMISSION,
//                            deviceModel.repairAddPermission)
//                        putBoolean(EquipmentInfoWorkOrderAddFragment.STOCKADDPERSSION,
//                            deviceModel.stockAddPerssion)
//                    })
                    Bundle().apply {
                        putString(EquipmentInfoWorkOrderAddFragment.FACILITYID,
                            deviceModel.facilityId.toString())
                        putObjectString(deviceModel)
                    })
            }

        } else if (deviceModel.handlePermission) {
            //有操作权限
            when (deviceModel.workOrderStatus) {
                "1" -> {
                    //待分配
                    viewBinding.btnConfirm.isVisible = true
                    viewBinding.btnConfirm.text = "去分配"
                    viewBinding.btnConfirm.setOnClickListener {
                        navController.navigate(R.id.equipmentWorkOrderWaitFenpeiFragment,
                            Bundle().apply {
                                putString(EquipmentWorkOrderWaitFenpeiFragment.WORKORDERID,
                                    deviceModel.workOrderId)
                                putString(EquipmentWorkOrderWaitFenpeiFragment.TEAMID,
                                    deviceModel.teamId)
                                //设备类型
                                putString(EquipmentWorkOrderWaitFenpeiFragment.FACILITYTYPE,
                                    deviceModel.facilityType)
                                //设备名称
                                putString(EquipmentWorkOrderWaitFenpeiFragment.FACILITYDESC,
                                    deviceModel.facilityDesc)
                                //设备型号
                                putString(EquipmentWorkOrderWaitFenpeiFragment.FACILITYMODEL,
                                    deviceModel.facilityModel)
                            })
                    }
                }
                "2" -> {
                    //待完工
                    viewBinding.btnConfirm.isVisible = true
                    viewBinding.btnConfirm.text = "去完工"
                    viewBinding.btnConfirm.setOnClickListener {
                        navController.navigate(R.id.equipmentWorkOrderWaitCompleteFragment,
                            Bundle().apply {
                                putString(EquipmentWorkOrderWaitCompleteFragment.WORKORDERID,
                                    deviceModel.workOrderId)
                                //设备类型
                                putString(EquipmentWorkOrderWaitCompleteFragment.FACILITYTYPE,
                                    deviceModel.facilityType)
                                //设备名称
                                putString(EquipmentWorkOrderWaitCompleteFragment.FACILITYDESC,
                                    deviceModel.facilityDesc)
                                //设备型号
                                putString(EquipmentWorkOrderWaitCompleteFragment.FACILITYMODEL,
                                    deviceModel.facilityModel)
                            })
                    }
                }
                "3" -> {
                    //待确认
                    viewBinding.btnConfirm.isVisible = true
                    viewBinding.btnConfirm.text = "去确认"
                    viewBinding.btnConfirm.setOnClickListener {
                        navController.navigate(R.id.equipmentInfoWorkOrderMaintenanceCompleteFragment,
                            Bundle().apply {
                                putString(EquipmentInfoWorkOrderMaintenanceCompleteFragment.WORKORDERID,
                                    deviceModel.workOrderId)
                                //设备类型
                                putString(EquipmentWorkOrderWaitCompleteFragment.FACILITYTYPE,
                                    deviceModel.facilityType)
                                //设备名称
                                putString(EquipmentWorkOrderWaitCompleteFragment.FACILITYDESC,
                                    deviceModel.facilityDesc)
                                //设备型号
                                putString(EquipmentWorkOrderWaitCompleteFragment.FACILITYMODEL,
                                    deviceModel.facilityModel)
                            })
                    }
                }
                "7" -> {
                    //出库待确认
                    viewBinding.btnConfirm.isVisible = true
                    viewBinding.btnConfirm.text = "出库确认"
                    viewBinding.btnConfirm.setOnClickListener {
                        navController.navigate(R.id.equipmentInfoWorkOrderConfirmOutStoreFragment,
                            Bundle().apply {
                                putString(EquipmentInfoWorkOrderConfirmOutStoreFragment.WORKORDERID,
                                    deviceModel.workOrderId)
                                //设备类型
                                putString(EquipmentInfoWorkOrderConfirmOutStoreFragment.FACILITYTYPE,
                                    deviceModel.facilityType)
                                //设备名称
                                putString(EquipmentInfoWorkOrderConfirmOutStoreFragment.FACILITYDESC,
                                    deviceModel.facilityDesc)
                                //设备型号
                                putString(EquipmentInfoWorkOrderConfirmOutStoreFragment.FACILITYMODEL,
                                    deviceModel.facilityModel)
                            })
                    }
                }
                "8" -> {
                    //待入库
                    viewBinding.btnConfirm.isVisible = true
                    viewBinding.btnConfirm.text = "提交入库"
                    viewBinding.btnConfirm.setOnClickListener {
                        navController.navigate(R.id.equipmentInfoWorkOrderWaitInStoreFragment,
                            Bundle().apply {
                                putString(EquipmentInfoWorkOrderWaitInStoreFragment.WORKORDERID,
                                    deviceModel.workOrderId)
                                //设备类型
                                putString(EquipmentInfoWorkOrderWaitInStoreFragment.FACILITYTYPE,
                                    deviceModel.facilityType)
                                //设备名称
                                putString(EquipmentInfoWorkOrderWaitInStoreFragment.FACILITYDESC,
                                    deviceModel.facilityDesc)
                                //设备型号
                                putString(EquipmentInfoWorkOrderWaitInStoreFragment.FACILITYMODEL,
                                    deviceModel.facilityModel)
                            })
                    }
                }
                "9" -> {
                    //入库待确认
                    viewBinding.btnConfirm.isVisible = true
                    viewBinding.btnConfirm.text = "入库确认"
                    viewBinding.btnConfirm.setOnClickListener {
                        navController.navigate(R.id.equipmentInfoWorkOrderInStoreCompleteFragment,
                            Bundle().apply {
                                putString(EquipmentInfoWorkOrderInStoreCompleteFragment.WORKORDERID,
                                    deviceModel.workOrderId)
                                //设备类型
                                putString(EquipmentInfoWorkOrderInStoreCompleteFragment.FACILITYTYPE,
                                    deviceModel.facilityType)
                                //设备名称
                                putString(EquipmentInfoWorkOrderInStoreCompleteFragment.FACILITYDESC,
                                    deviceModel.facilityDesc)
                                //设备型号
                                putString(EquipmentInfoWorkOrderInStoreCompleteFragment.FACILITYMODEL,
                                    deviceModel.facilityModel)
                                //BDC位置
                                putString(EquipmentInfoWorkOrderInStoreCompleteFragment.LOCATIONNAME,
                                    deviceModel.areaDesc)
                            })
                    }
                }
                else -> {
                    viewBinding.btnConfirm.isVisible = false
                }
            }
        } else {
            viewBinding.btnConfirm.isVisible = false
        }
    }

    /*
    * 创建文件列表适配并渲染
    * */
    private fun createFileAdapter(deviceModel: EquipmentInfoDeviceModel?) {
        //初始化底部文件列表
        fileAdapter =
            object : CommonViewBindingAdapter<ItemProblemRecordDetailFileBinding, FileInfoModel>() {
                override fun createBinding(parent: ViewGroup): ItemProblemRecordDetailFileBinding {
                    return ItemProblemRecordDetailFileBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )

                }

                override fun onBindViewHolderWithData(
                    holder: ViewBindingHolder,
                    data: FileInfoModel,
                    position: Int,
                ) {
                    holder.itemViewBinding.ivDelete.isVisible = false
                    holder.itemViewBinding.btnFile.text = data.fileName
                    holder.itemViewBinding.root.setOnClickListener {
                        showFileView(data)
                    }
                }

            }
        viewBinding.rvFilesList.adapter = fileAdapter
        fileAdapter.refreshData(deviceModel?.fileList ?: listOf())
    }

    private fun showFileView(data: FileInfoModel) {
        navController.navigate(R.id.equipmentGuidePdfPreviewFragment,
            Bundle().apply { putSerializable(EquipmentGuidePdfPreviewFragment.PDF_KEY, data) })
    }

    private fun refreshData(deviceModel: EquipmentInfoDeviceModel) {
        //设置标题
        if (deviceModel != null) {
            if (deviceModel.facilityType == "2") {
                //设置模具标题
                viewBinding.topAppBar.setTitle(R.string.equipment_info_detail_title)
            } else if (deviceModel.facilityType == "1") {
                //设置设备标题
                viewBinding.topAppBar.setTitle(R.string.equipment_info_device_detail_title)
            }
            viewBinding.apply {
                //编号
                tvInfoNumber.text = deviceModel.facilityCode
                //名称
                tvInfoName.text = deviceModel.facilityDesc
                //型号
                tvInfoModel.text = deviceModel.facilityModel
                //部门
                tvInfoBm.text = deviceModel.orgName
                //区域
                tvInfoArea.text = deviceModel.areaDesc
                //功能状态
                when (deviceModel.functionStatus) {
                    //正常、归还、报废
                    1, 4, 5 -> {
                        tvInfoWorkStatus.text =
                            CommonParameters.getDesc(DataParamType.FUNCTION_STATUS,
                                deviceModel.functionStatus)
                    }
                    else -> {
                        if (!TextUtils.isEmpty(deviceModel.workOrderStatus)) {
                            tvInfoWorkStatus.text =
                                CommonParameters.getDesc(DataParamType.WORK_ORDER_STATUS,
                                    MesStringUtils.stringToInt(deviceModel.workOrderStatus))
                        }
                    }
                }

                llWorkorderStatus.setOnClickListener {
                    //跳转维保记录，维保记录页面viewpager展示维修与保养记录
                    navController.navigate(R.id.equipmentMaintenanceHistoryFragment,
                        Bundle().apply {
                            //带入详情页的数据val facilityId = arguments?.getString("facilityId") ?: ""
                            //        val facilityType = arguments?.getString("facilityType") ?: ""
                            putString("facilityId", deviceModel.facilityId.toString())
                            putString("facilityType", deviceModel.facilityType)
//                            putObjectString(deviceModel)
                        })
                }
                //位置状态
                if (deviceModel.facilityType == "2") {
                    //模具展示位置状态
                    llWorkorderLocationStatus.visibility = View.VISIBLE
                    tvInfoLocationStatus.text = if (deviceModel.locationStatus == 1) {
                        "在库"
                    } else {
                        "出库"
                    }
                    //模具展示客户
                    tvSupplierorcustomer.text = "客户"
                    tvInfoFactory.text = deviceModel.customerDesc
                    //模具展示模次数
                    llInfoModulus.visibility = View.VISIBLE
                    tvInfoMcs.text = deviceModel.totalModulus
                    //模具展示坐标
                    llInfoLocation.visibility = View.VISIBLE
                    tvInfoLocation.text = deviceModel.coordinate
                } else if (deviceModel.facilityType == "1") {
                    //设备不展示位置状态
                    llWorkorderLocationStatus.visibility = View.GONE
                    //设备展示厂家
                    tvSupplierorcustomer.text = "厂家"
                    tvInfoFactory.text = deviceModel.supplierDesc
                    //设备不展示模次数
                    llInfoModulus.visibility = View.GONE
                    //设备不展示坐标
                    llInfoLocation.visibility = View.GONE
                }
                llWorkorderLocationStatus.setOnClickListener {
                    //跳转出入库记录页面
                    navController.navigate(R.id.equipmentStoreHistoryListFragment, Bundle().apply {
                        //带入详情页的数据val facilityId = arguments?.getString("facilityId") ?: ""
                        //        val facilityType = arguments?.getString("facilityType") ?: ""
                        putString(EquipmentStoreHistoryListFragment.FACILITYID,
                            deviceModel.facilityId.toString())
//                            putObjectString(deviceModel)
                    })
                }
//                llInfoModulus.setOnClickListener {
//                    //跳转生产次数记录页面
////                    navController.navigate(R.id.equipmentProduceHistoryListFragment,
////                        Bundle().apply {
////                            //带入详情页的数据
////                            putObjectString(deviceModel)
////                        })
//                    getRecord(deviceModel)
//                }

                llInfoModulus.clicks()
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe {
                        getRecord(deviceModel)
                    }
                showBottomBtn(deviceModel)
                if (null != deviceModel.fileList && deviceModel.fileList.isNotEmpty()) {
                    //展示附件
                    llFiles.isVisible = true
                    //创建文件列表适配并渲染
                    createFileAdapter(deviceModel)
                }else{
                    llFiles.isVisible = false
                }
            }
        }

    }

    //获取设备详情
    private fun showDetail(facilityType: String, facilityId: Int) {
        if (facilityType == "1") {
            //设备详情
            WebClient.request(EquipmentApi::class.java).pdaEmsDeviceDetailGet(facilityId)
                .bindToFragment()
                .subscribe({
                    refreshData(it)
                }, {})
        } else if (facilityType == "2") {
            //模具详情
            WebClient.request(EquipmentApi::class.java).pdaEmsMouldDetailGet(facilityId)
                .bindToFragment()
                .subscribe({
                    refreshData(it)
                }, {})
        }
    }
}