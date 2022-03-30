package com.panda.pda.mes.operation.ems.equipment_workorder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.api.load
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.extension.getStringObject
import com.panda.pda.mes.base.extension.putObjectString
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.ModelPropertyCreator
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.common.data.CommonApi
import com.panda.pda.mes.common.data.CommonParameters
import com.panda.pda.mes.common.data.DataParamType
import com.panda.pda.mes.common.data.MesStringUtils
import com.panda.pda.mes.common.data.model.FileInfoModel
import com.panda.pda.mes.databinding.*
import com.panda.pda.mes.operation.ems.data.EquipmentApi
import com.panda.pda.mes.operation.ems.data.model.EquipmentInfoDeviceModel
import com.panda.pda.mes.operation.ems.data.model.EquipmentWorkOrderModel
import com.panda.pda.mes.operation.ems.equipment_workorder.maintenance.EquipmentInfoWorkOrderMaintenanceCompleteFragment
import com.panda.pda.mes.operation.ems.equipment_workorder.maintenance.EquipmentWorkOrderWaitCompleteFragment
import com.panda.pda.mes.operation.ems.equipment_workorder.maintenance.EquipmentWorkOrderWaitFenpeiFragment
import com.panda.pda.mes.operation.ems.equipment_workorder.store.EquipmentInfoWorkOrderConfirmOutStoreFragment
import com.panda.pda.mes.operation.ems.equipment_workorder.store.EquipmentInfoWorkOrderInStoreCompleteFragment
import com.panda.pda.mes.operation.ems.equipment_workorder.store.EquipmentInfoWorkOrderWaitInStoreFragment
import com.panda.pda.mes.operation.qms.QualityViewModel
import com.panda.pda.mes.operation.qms.data.model.QualityProblemRecordDetailModel
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import java.util.concurrent.TimeUnit

/**
 * 模具详情
 */
class EquipmentWorkOrderDetailFragment :
    BaseFragment(R.layout.fragment_equipment_workorder_detail) {

    private val viewBinding by viewBinding<FragmentEquipmentWorkorderDetailBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }
        val workOrderId = arguments?.getString(WORKORDER)
        workOrderId?.let {
            showDetail(it)
        }

    }

    private fun refreshData(workOrderDetail: EquipmentWorkOrderModel) {
        workOrderDetail?.let { data ->
            viewBinding.apply {
                btnConfirm.setOnClickListener {
                }
                tvWorkorderCode.text = data.workOrderCode
                //设备编号
                tvWorkorderNumber.text = data.facilityCode
                tvWorkorderTbr.text = data.createName
                tvWorkorderTbtime.text = data.createTime
                //设备名称
                tvWorkorderName.text = data.facilityName
                llDeviceName.setOnClickListener{
                    gotoDeviceDetail(data)
                }
                //设备型号
                tvWorkorderMarking.text = data.facilityModel
                //部门
                tvWorkorderGroup.text = data.orgName
                //位置
                tvWorkorderLocation.text = data.locationName
                //类型
                tvWorkorderType.text = CommonParameters.getDesc(DataParamType.FUNCTION_TYPE,
                   MesStringUtils.stringToInt(data.functionType))
                //处理状态
                tvWorkorderStatus.text =
                    CommonParameters.getDesc(DataParamType.WORK_ORDER_STATUS,
                        MesStringUtils.stringToInt(data.workOrderStatus))
                llWorkorderStatus.setOnClickListener{
                    //跳转操作记录
                    getOperate(data)
                }
                //坐标
                if (data.facilityType=="1") {
                    llWorkorderCoordinate.isVisible=false
                }else{
                    llWorkorderCoordinate.isVisible=true
                    tvWorkorderCoordinate.text = data.coordinate
                }
                if (data.operable == 1) {
                    //有操作权限
                    when (MesStringUtils.stringToInt(data.workOrderStatus)) {
                        1 -> {
                            //待分配
                            viewBinding.btnConfirm.isVisible = true
                            viewBinding.btnConfirm.text = "去分配"
                            viewBinding.btnConfirm.setOnClickListener {
                                navController.navigate(R.id.equipmentWorkOrderWaitFenpeiFragment,
                                    Bundle().apply {
                                        //工单id
                                        putString(EquipmentWorkOrderWaitFenpeiFragment.WORKORDERID,
                                            data.id)
                                        //小组id，选择人员使用
                                        putString(EquipmentWorkOrderWaitFenpeiFragment.TEAMID,
                                            data.teamId)
                                        //设备类型
                                        putString(EquipmentWorkOrderWaitFenpeiFragment.FACILITYTYPE,
                                            data.facilityType)
                                        //设备名称
                                        putString(EquipmentWorkOrderWaitFenpeiFragment.FACILITYDESC,
                                            data.facilityName)
                                        //设备型号
                                        putString(EquipmentWorkOrderWaitFenpeiFragment.FACILITYMODEL,
                                            data.facilityModel)
                                    })
                            }
                        }
                        2 -> {
                            //待完工
                            viewBinding.btnConfirm.isVisible = true
                            viewBinding.btnConfirm.text = "去完工"
                            viewBinding.btnConfirm.setOnClickListener {
                                navController.navigate(R.id.equipmentWorkOrderWaitCompleteFragment,
                                    Bundle().apply {
                                        putString(EquipmentWorkOrderWaitCompleteFragment.WORKORDERID,
                                            data.id)
                                        //设备类型
                                        putString(EquipmentWorkOrderWaitCompleteFragment.FACILITYTYPE,
                                            data.facilityType)
                                        //设备名称
                                        putString(EquipmentWorkOrderWaitCompleteFragment.FACILITYDESC,
                                            data.facilityName)
                                        //设备型号
                                        putString(EquipmentWorkOrderWaitCompleteFragment.FACILITYMODEL,
                                            data.facilityModel)
                                    })
                            }
                        }
                        3 -> {
                            //待确认
                            viewBinding.btnConfirm.isVisible = true
                            viewBinding.btnConfirm.text = "去确认"
                            viewBinding.btnConfirm.setOnClickListener {
                                navController.navigate(R.id.equipmentInfoWorkOrderMaintenanceCompleteFragment,
                                    Bundle().apply {
                                        putString(EquipmentInfoWorkOrderMaintenanceCompleteFragment.WORKORDERID,
                                            data.id)
                                        //设备类型
                                        putString(EquipmentInfoWorkOrderMaintenanceCompleteFragment.FACILITYTYPE,
                                            data.facilityType)
                                        //设备名称
                                        putString(EquipmentInfoWorkOrderMaintenanceCompleteFragment.FACILITYDESC,
                                            data.facilityName)
                                        //设备型号
                                        putString(EquipmentInfoWorkOrderMaintenanceCompleteFragment.FACILITYMODEL,
                                            data.facilityModel)
                                    })
                            }
                        }
                        7 -> {
                            //出库待确认
                            viewBinding.btnConfirm.isVisible = true
                            viewBinding.btnConfirm.text = "出库确认"
                            viewBinding.btnConfirm.setOnClickListener {
                                navController.navigate(R.id.equipmentInfoWorkOrderConfirmOutStoreFragment,
                                    Bundle().apply {
                                        putString(EquipmentInfoWorkOrderConfirmOutStoreFragment.WORKORDERID,
                                            data.id)
                                        //设备类型
                                        putString(EquipmentInfoWorkOrderConfirmOutStoreFragment.FACILITYTYPE,
                                            data.facilityType)
                                        //设备名称
                                        putString(EquipmentInfoWorkOrderConfirmOutStoreFragment.FACILITYDESC,
                                            data.facilityName)
                                        //设备型号
                                        putString(EquipmentInfoWorkOrderConfirmOutStoreFragment.FACILITYMODEL,
                                            data.facilityModel)
                                    })
                            }
                        }
                        8 -> {
                            //待入库
                            viewBinding.btnConfirm.isVisible = true
                            viewBinding.btnConfirm.text = "提交入库"
                            viewBinding.btnConfirm.setOnClickListener {
                                navController.navigate(R.id.equipmentInfoWorkOrderWaitInStoreFragment,
                                    Bundle().apply {
                                        putString(EquipmentInfoWorkOrderWaitInStoreFragment.WORKORDERID,
                                            data.id)
                                        //设备id
                                        putString(EquipmentInfoWorkOrderWaitInStoreFragment.FACILITYID,
                                            data.facilityId.toString())
                                        //设备类型
                                        putString(EquipmentInfoWorkOrderWaitInStoreFragment.FACILITYTYPE,
                                            data.facilityType)
                                        //设备名称
                                        putString(EquipmentInfoWorkOrderWaitInStoreFragment.FACILITYDESC,
                                            data.facilityName)
                                        //设备型号
                                        putString(EquipmentInfoWorkOrderWaitInStoreFragment.FACILITYMODEL,
                                            data.facilityModel)
                                    })
                            }
                        }
                        9 -> {
                            //入库待确认
                            viewBinding.btnConfirm.isVisible = true
                            viewBinding.btnConfirm.text = "入库确认"
                            viewBinding.btnConfirm.setOnClickListener {
                                navController.navigate(R.id.equipmentInfoWorkOrderInStoreCompleteFragment,
                                    Bundle().apply {
                                        putString(EquipmentInfoWorkOrderInStoreCompleteFragment.WORKORDERID,
                                            data.id)
                                        //设备类型
                                        putString(EquipmentInfoWorkOrderInStoreCompleteFragment.FACILITYTYPE,
                                            data.facilityType)
                                        //设备名称
                                        putString(EquipmentInfoWorkOrderInStoreCompleteFragment.FACILITYDESC,
                                            data.facilityName)
                                        //设备型号
                                        putString(EquipmentInfoWorkOrderInStoreCompleteFragment.FACILITYMODEL,
                                            data.facilityModel)
                                        //bdc位置
                                        putString(EquipmentInfoWorkOrderInStoreCompleteFragment.LOCATIONNAME,
                                            data.locationName)
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
        }
    }

    //详情
    private fun showDetail(workOrderId: String) {
        //设备详情
        WebClient.request(EquipmentApi::class.java).pdaEmsWorkOrderDetailGet(workOrderId)
            .bindToFragment()
            .subscribe({
                refreshData(it)
            }, {})
    }

    private fun gotoDeviceDetail(workOrderDetail: EquipmentWorkOrderModel){
        navController.navigate(R.id.equipmentInfoDetailFragment, Bundle().apply {
            //带入详情页的数据
            putObjectString(workOrderDetail)
        })
    }
    //操作记录
    private fun getOperate(workOrderDetail: EquipmentWorkOrderModel) {
        WebClient.request(EquipmentApi::class.java)
            .pdaEmsWorkOrderOperateListGet(workOrderDetail.id)
            .bindToFragment()
            .subscribe({
                navController.navigate(
                    R.id.equipmentOperationHistoryListFragment,
                    Bundle().apply {
                        putString(
                            EquipmentOperationHistoryListFragment.DETAIL_KEY,
                            EquipmentOperationHistoryListFragment.getProduceRecordJsonAdapter()
                                .toJson(it.dataList)
                        )
                    })
            }, {})

    }
    companion object {
        const val WORKORDER = "workOrder"
    }
}