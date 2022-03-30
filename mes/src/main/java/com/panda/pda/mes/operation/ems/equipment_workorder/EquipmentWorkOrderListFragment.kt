package com.panda.pda.mes.operation.ems.equipment_workorder

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.panda.pda.mes.R
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.databinding.ItemEquipmentTaskBinding
import com.panda.pda.mes.operation.ems.EquipmentWorkOrderSearchListFragment
import com.panda.pda.mes.operation.ems.data.model.EmsModelType
import com.panda.pda.mes.operation.ems.data.model.EquipmentWorkOrderModel
import com.panda.pda.mes.operation.qms.QualityViewModel
import com.panda.pda.mes.operation.qms.data.QualityApi
import com.panda.pda.mes.operation.qms.data.model.QualityTaskModel

class EquipmentWorkOrderListFragment :
    EquipmentWorkOrderSearchListFragment<ItemEquipmentTaskBinding>() {
    override val qualityTaskModelType: EmsModelType
        get() = EmsModelType.WORKORDER
    override val searchBarHintResId: Int?
        get() = R.string.equipment_search_hint_workorderlist

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
            var statusRes: Int
            tvTaskInfo.text = "${
                if (data.facilityType == "1") {
                    "设备"
                } else {
                    "模具"
                }
            }-${data.facilityName}-${data.facilityModel}"
            tvTaskNumber.text = data.facilityModel
            tvTaskId.text = "单号：${
                data.workOrderCode
            }"
            if (TextUtils.isEmpty(data.updateName)) {
                tvTaskMemberLabel.visibility = View.INVISIBLE
                tvTaskMemberName.visibility = View.INVISIBLE
            } else {
                tvTaskMemberName.text = data.updateName
                tvTaskMemberLabel.text = "更新人"
                tvTaskMemberName.visibility = View.VISIBLE
                tvTaskMemberLabel.visibility = View.VISIBLE
            }
            tvRemark.text = data.remark
            //位置信息
            tvLocation.text="${data.orgName}-${data.locationName}"
            if (data.facilityType=="2"&&!TextUtils.isEmpty(data.coordinate)) {
                tvLocation.text= "${data.orgName}-${data.locationName}-${data.coordinate}"
            }
            ivTaskStatus.visibility = View.VISIBLE
            when (data.workOrderStatus) {
                "1" -> {
                    //待分配
                    statusRes = R.drawable.icon_equipment_order_status_fenpei
                }
                "2" -> {
                    //待完工
                    statusRes = R.drawable.icon_equipment_order_status_daiwangong
                }
                "3" -> {
                    //待确认
                    statusRes = R.drawable.icon_equipment_order_status_daiqueren
                }
                "4" -> {
                    //合格
                    statusRes = R.drawable.icon_equipment_order_status_ok
                }
                "5" -> {
                    //限度生产
                    statusRes = R.drawable.icon_equipment_order_status_xdsc
                }
//                "6" -> {
//                    //延期
//                    //不会走入此状态，因为在选择延期提交时，回立马转入待分配状态
//                }
                "7" -> {
                    //出库待确认
                    statusRes = R.drawable.icon_equipment_order_status_waitchuku
                }
                "8" -> {
                    //待入库
                    statusRes = R.drawable.icon_equipment_order_status_ruku
                }
                "9" -> {
                    //入库待确认
                    statusRes = R.drawable.icon_equipment_order_status_wait_ruku

                }
                "10" -> {
                    //在库
                    statusRes = R.drawable.icon_equipment_order_status_zaiku
                }
                "11" -> {
                    //作废
                    statusRes = R.drawable.icon_equipment_order_status_baofei
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