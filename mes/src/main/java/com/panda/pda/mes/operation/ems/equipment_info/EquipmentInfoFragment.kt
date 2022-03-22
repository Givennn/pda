package com.panda.pda.mes.operation.ems.equipment_info

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.panda.pda.mes.R
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.databinding.ItemEquipmentInfoBinding
import com.panda.pda.mes.operation.ems.EquipmentDeviceSearchListFragment
import com.panda.pda.mes.operation.ems.data.model.EmsModelType
import com.panda.pda.mes.operation.ems.data.model.EquipmentInfoDeviceModel

class EquipmentInfoFragment : EquipmentDeviceSearchListFragment<ItemEquipmentInfoBinding>() {
    override val qualityTaskModelType: EmsModelType
        get() = EmsModelType.INFO

    override fun createViewBinding(parent: ViewGroup): ItemEquipmentInfoBinding {
        return ItemEquipmentInfoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: CommonViewBindingAdapter<ItemEquipmentInfoBinding, EquipmentInfoDeviceModel>.ViewBindingHolder,
        data: EquipmentInfoDeviceModel,
        position: Int,
    ) {
        holder.itemViewBinding.apply {
            //设备编号
            tvInfoId.text = "编号：${data.facilityCode}"
            tvInfoName.text = "${
                if (data.facilityType == "1") {
                    "设备"
                } else {
                    "模具"
                }
            }-${data.facilityDesc}"
            //设备型号
            tvInfoNumber.text = data.facilityModel
            //位置信息
            tvInfoLocation.text = data.areaDesc
            //功能状态
            val functionStatus = data.functionStatus
            //位置状态
            val locationStatus = data.locationStatus
            val statusRes: Int
            when (functionStatus) {
                1 -> {
                    //正常
                    //功能状态为1，展示位置状态
                    if (locationStatus == 1) {
                        //在库
                        statusRes = R.drawable.icon_equipment_info_status_zaiku
                    } else {
                        statusRes = R.drawable.icon_equipment_info_status_chuku
                    }
                }
                2 -> {
                    //维修
                    statusRes = R.drawable.icon_equipment_info_status_weixiu
                }
                3 -> {
                    //保养
                    statusRes = R.drawable.icon_equipment_info_status_baoyang
                }
                4 -> {
                    //报废
                    statusRes = R.drawable.icon_equipment_info_status_baofei
                }
                5 -> {
                    //归还
                    statusRes = R.drawable.icon_equipment_info_status_guihuan
                }
                else -> {
                    //异常保护，不会进入当前状态
                    statusRes = R.drawable.icon_equipment_order_status_ok
                }
            }
            //设置状态角标
            ivInfoStatus.setImageResource(statusRes)
        }
    }
}