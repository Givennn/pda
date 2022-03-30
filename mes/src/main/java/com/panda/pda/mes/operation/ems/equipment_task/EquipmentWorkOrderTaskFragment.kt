package com.panda.pda.mes.operation.ems.equipment_task

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.panda.pda.mes.R
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.databinding.ItemEquipmentTaskBinding
import com.panda.pda.mes.operation.ems.EquipmentWorkOrderSearchListFragment
import com.panda.pda.mes.operation.ems.data.model.EmsModelType
import com.panda.pda.mes.operation.ems.data.model.EquipmentWorkOrderModel
import com.panda.pda.mes.operation.ems.equipment_workorder.maintenance.EquipmentInfoWorkOrderMaintenanceCompleteFragment
import com.panda.pda.mes.operation.ems.equipment_workorder.maintenance.EquipmentWorkOrderWaitCompleteFragment
import com.panda.pda.mes.operation.ems.equipment_workorder.maintenance.EquipmentWorkOrderWaitFenpeiFragment
import com.panda.pda.mes.operation.ems.equipment_workorder.store.EquipmentInfoWorkOrderConfirmOutStoreFragment
import com.panda.pda.mes.operation.ems.equipment_workorder.store.EquipmentInfoWorkOrderInStoreCompleteFragment
import com.panda.pda.mes.operation.ems.equipment_workorder.store.EquipmentInfoWorkOrderWaitInStoreFragment

class EquipmentWorkOrderTaskFragment :
    EquipmentWorkOrderSearchListFragment<ItemEquipmentTaskBinding>() {
    override val qualityTaskModelType: EmsModelType
        get() = EmsModelType.Task

    override fun createViewBinding(parent: ViewGroup): ItemEquipmentTaskBinding {
        return ItemEquipmentTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: CommonViewBindingAdapter<ItemEquipmentTaskBinding, EquipmentWorkOrderModel>.ViewBindingHolder,
        data: EquipmentWorkOrderModel,
        position: Int,
    ) {
        holder.itemViewBinding.apply {
            //设置标题（设备类型-设备名称-设备型号）
            tvTaskInfo.text = "${
                if (data.facilityType == "1") {
                    "设备"
                } else {
                    "模具"
                }
            }-${data.facilityName}-${data.facilityModel}"
            tvTaskNumber.text = data.facilityModel
            //单号展示
            tvTaskId.text = "单号：${
                data.workOrderCode
            }"
            //更新人---如果没有更新人，则不展示
            if (TextUtils.isEmpty(data.updateName)) {
                tvTaskMemberLabel.visibility = View.INVISIBLE
                tvTaskMemberName.visibility = View.INVISIBLE
            } else {
                tvTaskMemberName.text = data.updateName
                tvTaskMemberLabel.text = "更新人"
                tvTaskMemberName.visibility = View.VISIBLE
                tvTaskMemberLabel.visibility = View.VISIBLE
            }
            //描述
            tvRemark.text = data.remark
            //位置信息
            tvLocation.text="${data.orgName}-${data.locationName}"
            if (data.facilityType=="2"&&!TextUtils.isEmpty(data.coordinate)) {
                tvLocation.text= "${data.orgName}-${data.locationName}-${data.coordinate}"
            }
            //状态设置，根据workorderstatus字段来判断
            var statusRes: Int
            ivTaskStatus.visibility = View.VISIBLE
            //默认不展示可操作按钮
            btnActionGoTask.visibility=View.GONE
            when (data.workOrderStatus) {
                "1" -> {
                    //待分配
                    statusRes = R.drawable.icon_equipment_order_status_fenpei
                    btnActionGoTask.setIconResource(R.drawable.ic_action_equipment_fenpei)
                    btnActionGoTask.text = getString(R.string.equipment_workorder_status_daifenpei)
                    btnActionGoTask.visibility=View.VISIBLE
                    btnActionGoTask.setOnClickListener {
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
                "2" -> {
                    //待完工
                    statusRes = R.drawable.icon_equipment_order_status_daiwangong
                    btnActionGoTask.setIconResource(R.drawable.ic_action_equipment_wangong)
                    btnActionGoTask.text = getString(R.string.equipment_workorder_status_daiwangong)
                    btnActionGoTask.visibility=View.VISIBLE
                    btnActionGoTask.setOnClickListener {
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
                "3" -> {
                    //待确认
                    statusRes = R.drawable.icon_equipment_order_status_daiqueren
                    btnActionGoTask.setIconResource(R.drawable.ic_action_finish)
                    btnActionGoTask.text = getString(R.string.equipment_workorder_status_daiqueren)
                    btnActionGoTask.visibility=View.VISIBLE
                    btnActionGoTask.setOnClickListener {
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
                "4" -> {
                    //合格
                    statusRes = R.drawable.icon_equipment_order_status_ok
                }
                "5" -> {
                    //限度生产
                    statusRes = R.drawable.icon_equipment_order_status_ok
                }
//                "6" -> {
//                    //延期
//                    //不会走入此状态，因为在选择延期提交时，回立马转入待分配状态
//                }
                "7" -> {
                    //出库待确认
                    statusRes = R.drawable.icon_equipment_order_status_waitchuku
                    btnActionGoTask.setIconResource(R.drawable.ic_action_finish)
                    btnActionGoTask.text =
                        getString(R.string.equipment_workorder_status_chukuqueren)
                    btnActionGoTask.visibility=View.VISIBLE
                    btnActionGoTask.setOnClickListener {
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
                "8" -> {
                    //待入库
                    statusRes = R.drawable.icon_equipment_order_status_ruku
                    btnActionGoTask.setIconResource(R.drawable.ic_action_equipment_ruku)
                    btnActionGoTask.text = getString(R.string.equipment_workorder_status_dairuku)
                    btnActionGoTask.visibility=View.VISIBLE
                    btnActionGoTask.setOnClickListener {
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
                "9" -> {
                    //入库待确认
                    statusRes = R.drawable.icon_equipment_order_status_wait_ruku
                    btnActionGoTask.setIconResource(R.drawable.ic_action_finish)
                    btnActionGoTask.text = getString(R.string.equipment_workorder_status_rukuqueren)
                    btnActionGoTask.visibility=View.VISIBLE
                    btnActionGoTask.setOnClickListener {
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
                                putString(EquipmentInfoWorkOrderInStoreCompleteFragment.LOCATIONNAME,
                                    data.locationName)
                            })
                    }
                }
                "10" -> {
                    //在库
                    statusRes = R.drawable.icon_equipment_order_status_zaiku
                }
                "11" -> {
                    //作废
                    statusRes = R.drawable.icon_equipment_order_status_ok
                }
                else -> {
                    statusRes = R.drawable.icon_equipment_order_status_ok
                    //异常保护，不在现有状态下，不展示角标
                    ivTaskStatus.visibility = View.GONE
                }

            }
            //设置状态角标
            ivTaskStatus.setImageResource(statusRes)
        }
    }
}