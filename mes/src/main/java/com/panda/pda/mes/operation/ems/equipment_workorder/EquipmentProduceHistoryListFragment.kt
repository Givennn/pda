package com.panda.pda.mes.operation.ems.equipment_workorder

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.extension.getStringObject
import com.panda.pda.mes.base.retrofit.DataListNode
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.databinding.*
import com.panda.pda.mes.operation.ems.data.model.EquipmentInfoDeviceModel
import com.panda.pda.mes.operation.ems.data.model.EquipmentProcedureModel
import com.panda.pda.mes.operation.qms.data.model.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

/**
 * 生产数记录
 */
class EquipmentProduceHistoryListFragment :
    BaseFragment(R.layout.fragment_equipment_workorder_produce_record) {

    private val bindingAdapter by lazy { createRecordAdapter() }
    private val viewBinding by viewBinding<FragmentEquipmentWorkorderProduceRecordBinding>()
    private var recordList: List<EquipmentProcedureModel>? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }
        viewBinding.rvRecords.adapter = bindingAdapter
        val detailStr = arguments?.getString(DETAIL_KEY)
        recordList = getProduceRecordJsonAdapter().fromJson(detailStr)
            ?: listOf()
        bindingAdapter.refreshData(recordList!!)
        var deviceModel = arguments?.getStringObject<EquipmentInfoDeviceModel>("deviceModel")
            ?: return
        //设备模具的名称型号
        var titleName = if (deviceModel.facilityType == "1") {
            "设备-${deviceModel.facilityDesc}-${deviceModel.facilityModel}"
        } else {
            "模具-${deviceModel.facilityDesc}-${deviceModel.facilityModel}"
        }
        viewBinding.titleName.text = titleName
    }

    private fun createRecordAdapter(): CommonViewBindingAdapter<ItemEquipmentProducecountBinding, EquipmentProcedureModel> {
        return object :
            CommonViewBindingAdapter<ItemEquipmentProducecountBinding, EquipmentProcedureModel>() {
            override fun createBinding(parent: ViewGroup): ItemEquipmentProducecountBinding {
                return ItemEquipmentProducecountBinding.inflate(LayoutInflater.from(parent.context))
            }

            @SuppressLint("SetTextI18n")
            override fun onBindViewHolderWithData(
                holder: ViewBindingHolder,
                data: EquipmentProcedureModel,
                position: Int,
            ) {
                holder.itemViewBinding.apply {
                    //此次次数
                    tvCount.text = "${data.procedureModulus}次"
                    //创建人
                    tvName.text = data.createName
                    //总次数
                    tvTotalcount.text = "总计${data.totalModulus}次"
                    //时间
                    tvTime.text = data.createTime
                }
            }
        }
    }

    companion object {
        const val DETAIL_KEY = "produce_key"

        fun getProduceRecordJsonAdapter(): JsonAdapter<List<EquipmentProcedureModel>> {
            return Moshi.Builder().build().adapter(Types.newParameterizedType(
                List::class.java,
                EquipmentProcedureModel::class.java
            ))
        }
    }
}