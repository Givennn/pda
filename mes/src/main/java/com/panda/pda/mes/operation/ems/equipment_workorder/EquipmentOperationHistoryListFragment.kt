package com.panda.pda.mes.operation.ems.equipment_workorder

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.extension.putObjectString
import com.panda.pda.mes.base.retrofit.DataListNode
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.common.data.CommonParameters
import com.panda.pda.mes.common.data.DataParamType
import com.panda.pda.mes.common.data.MesStringUtils
import com.panda.pda.mes.databinding.FragmentEquipmentWorkorderOperationDetailRecordBinding
import com.panda.pda.mes.databinding.FrameEmptyViewBinding
import com.panda.pda.mes.databinding.ItemEquipmentDetailOperateRecordBinding
import com.panda.pda.mes.operation.ems.data.model.EquipmentOperateModel
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

/**
 * 操作记录
 */
class EquipmentOperationHistoryListFragment :
    BaseFragment(R.layout.fragment_equipment_workorder_operation_detail_record) {

    private val bindingAdapter by lazy { createRecordAdapter() }
    private val viewBinding by viewBinding<FragmentEquipmentWorkorderOperationDetailRecordBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener.invoke(requireView()) }
        val detailStr = arguments?.getString(DETAIL_KEY)
        val recordList = detailStr?.let { getProduceRecordJsonAdapter().fromJson(it) }
            ?: listOf()
        bindingAdapter.refreshData(recordList)
        viewBinding.rvRecords.adapter = bindingAdapter
//        viewModel.qualityDetailRecordData.observe(viewLifecycleOwner, {
//            bindingAdapter.refreshData(it.dataList)
//        })
    }

    private fun createRecordAdapter(): CommonViewBindingAdapter<ItemEquipmentDetailOperateRecordBinding, EquipmentOperateModel> {
        return object :
            CommonViewBindingAdapter<ItemEquipmentDetailOperateRecordBinding, EquipmentOperateModel>() {
            override fun createBinding(parent: ViewGroup): ItemEquipmentDetailOperateRecordBinding {
                return ItemEquipmentDetailOperateRecordBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
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
                data: EquipmentOperateModel,
                position: Int,
            ) {
                holder.itemViewBinding.apply {
                    viewStepLineTop.isVisible = holder.layoutPosition != 0
                    viewStepLineBottom.isVisible = holder.bindingAdapterPosition != itemCount - 1
                    //将不是必须展示的信息先隐藏
                    tvSimple.isVisible = false
                    tvTimeout.isVisible = false
                    tvComplete.isVisible = false
                    tvWorktime.isVisible = false
                    tvHege.isVisible = false
                    tvDevice.isVisible = false
                    tvSupplier.isVisible = false
                    tvProduct.isVisible = false
                    tvMember.isVisible = false
                    tvPlantime.isVisible = false
                    tvSimple.isVisible = false
                    //操作标题
                    tvRecordType.text = data.operateFullName
                    //操作人
                    tvOperator.text = data.createName
                    //操作时间
                    tvTime.text = data.createTime
                    //操作描述
                    tvDesc.text = "描述：${data.remark}"
                    var facilityTypeName = CommonParameters.getDesc(DataParamType.FACILITY_TYPE,
                        MesStringUtils.stringToInt(data.facilityType))
                    var functionTypeName = CommonParameters.getDesc(DataParamType.FUNCTION_TYPE,
                        MesStringUtils.stringToInt(data.functionType))
                    var operateTypeName = CommonParameters.getDesc(DataParamType.WORK_ORDER_OPERATE,
                        data.workOrderStatus)
                    tvRecordType.text = "${facilityTypeName}${functionTypeName}${operateTypeName}"
                    //处理右上角图文图标，描述与图片都为空时，不展示操作内容详情入口
                    if (TextUtils.isEmpty(data.remark) && data.fileList.isEmpty()) {
                        ivPic.visibility = View.INVISIBLE
                    } else {
                        ivPic.visibility = View.VISIBLE
                        if (data.fileList.isNotEmpty()) {
                            ivPic.setImageResource(R.drawable.icon_equipment_operate_pic)
                        } else {
                            ivPic.setImageResource(R.drawable.icon_equipment_operate_msg)
                        }
                        ivPic.setOnClickListener {
                            navController.navigate(R.id.equipmentOperationDetailFragment,
                                Bundle().apply {
                                    //带入详情页的数据
                                    putObjectString(data)
                                })
                        }
                    }
                    //根据不同状态展示不同元素
                    //维保
//                    if (data.workOrderType!="3") {
                    when (data.workOrderStatus) {
                        //已分配也就是待完工状态
                        2 -> {
                            //已分配需要展示是否提供样品，要给出是否提供样品标识、被分配人、预计完工时间
                            tvSimple.visibility = View.VISIBLE
                            tvMember.visibility = View.VISIBLE
                            tvPlantime.visibility = View.VISIBLE
                            var sampleText = if (data.sampleProvided == 1) {
                                "是"
                            } else {
                                "否"
                            }
                            tvSimple.text = "是否提供样品：${sampleText}"
                            tvPlantime.text = "预计完工时间：${data.expectWorkTime}"
                            if (data.userNames != null&&data.userNames.isNotEmpty()) {
                                var textMember="被分配人员："
                                tvMember.text = textMember+data.userNames.joinToString(";") {
                                    it
                                }
                            }
                        }
                        3 -> {
                            //已完工也就是待确认状态，需要展示是否超期、是否完工、工时
                            tvTimeout.visibility = View.VISIBLE
                            tvComplete.visibility = View.VISIBLE
                            tvWorktime.visibility = View.VISIBLE
                            var timeOutText = if (data.overTimeFlag == 1) {
                                "是"
                            } else {
                                "否"
                            }
                            tvTimeout.text = "是否超期：${timeOutText}"
                            var completeText = if (data.completeFlag == 1) {
                                "是"
                            } else {
                                "否"
                            }
                            tvComplete.text = "是否完工：${completeText}"
                            tvWorktime.text = "工时：${data.workTime}"
                        }
                        4 -> {
                            //合格
                            //展示是否合格：是
                            tvHege.visibility = View.VISIBLE
                            var intactText = if (data.conclusion == 1) {
                                "是"
                            } else {
                                "否"
                            }
                            tvHege.text = "是否合格：${intactText}"
                        }
                        5 -> {
                            //限度生产
                            //展示是否合格：限度生产
                            var intactText = if (data.conclusion == 2) {
                                "是"
                            } else {
                                "否"
                            }
                            tvHege.visibility = View.VISIBLE
                            tvHege.text = "是否限度生产：${intactText}"
                        }
                        7 -> {
                            //出库待确认
                            //展示有设备展示设备，有厂商展示厂商，都没有则都不展示
                            tvDevice.visibility = View.VISIBLE
                            tvSupplier.visibility = View.VISIBLE
                            if (!TextUtils.isEmpty(data.relatedEquipName)) {
                                tvDevice.visibility = View.VISIBLE
                                tvDevice.text = "设备：${data.relatedEquipName}"
                            }else{
                                tvDevice.visibility = View.GONE
                            }
                            if (!TextUtils.isEmpty(data.relatedSupplierName)) {
                                tvSupplier.visibility = View.VISIBLE
                                tvSupplier.text = "厂商：${data.relatedSupplierName}"
                            }else{
                                tvSupplier.visibility = View.GONE
                            }
                        }
                        9 -> {
                            //入库待确认
                            //展示生产产品
                            tvProduct.visibility = View.VISIBLE
                            tvProduct.text="生产产品：${data.productionProductNames.replace("[","").replace("]","")}"
                        }
                        else -> {
                        }
                    }

//                    }else{
//
//                    }
                    //


                }
            }
        }
    }

    companion object {
        const val DETAIL_KEY = "produce_key"

        fun getProduceRecordJsonAdapter(): JsonAdapter<List<EquipmentOperateModel>> {
            return Moshi.Builder().build().adapter(Types.newParameterizedType(
                List::class.java,
                EquipmentOperateModel::class.java
            ))
        }
    }
}