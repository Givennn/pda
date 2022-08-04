package com.panda.pda.mes.operation.ems.equipment_workorder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.extension.getStringObject
import com.panda.pda.mes.base.extension.toast
import com.panda.pda.mes.common.ModelPropertyCreator
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.common.data.CommonApi
import com.panda.pda.mes.common.data.model.FileInfoModel
import com.panda.pda.mes.databinding.*
import com.panda.pda.mes.discovery.TaskReportViewModel
import com.panda.pda.mes.discovery.data.TaskReportDetailModel
import com.panda.pda.mes.operation.ems.data.model.EquipmentInfoDeviceModel
import com.panda.pda.mes.operation.ems.data.model.EquipmentOperateModel
import com.panda.pda.mes.operation.ems.data.model.EquipmentWorkOrderModel
import com.panda.pda.mes.operation.qms.QualityViewModel
import com.panda.pda.mes.operation.qms.data.model.QualityProblemRecordDetailModel

/**
 * 操作记录详情页面
 */
class EquipmentOperationDetailFragment :
    BaseFragment(R.layout.fragment_equipment_workorder_report_detail) {
    private lateinit var picBindingAdapter: CommonViewBindingAdapter<ItemTaskReportDetailPicBinding, FileInfoModel>
    private val viewBinding by viewBinding<FragmentEquipmentWorkorderReportDetailBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener.invoke(requireView()) }
        val operateModel = arguments?.getStringObject<EquipmentOperateModel>()
        if (operateModel == null) {
            toast("未取得操作详情数据")
            navBackListener.invoke(requireView())
            return
        }
        viewBinding.apply {
            tvOperation.text = operateModel.remark
            //有图片列表，展示图片标题及列表
            if (operateModel.fileList.isEmpty()) {
                tvPictitle.isVisible=false
                rvPicList.isVisible=false
            }else{
                tvPictitle.isVisible=true
                rvPicList.isVisible=true
            }
        }

        picBindingAdapter = object :
            CommonViewBindingAdapter<ItemTaskReportDetailPicBinding, FileInfoModel>(operateModel.fileList.toMutableList()) {
            override fun createBinding(parent: ViewGroup): ItemTaskReportDetailPicBinding {
                return ItemTaskReportDetailPicBinding.inflate(
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

                holder.itemViewBinding.ivTaskReportDetailPic.load(
                    CommonApi.getFIleUrl(
                        data.fileUrl,
                        data.fileName
                    )
                )
            }
        }
        viewBinding.rvPicList.adapter = picBindingAdapter
        picBindingAdapter.refreshData(operateModel.fileList)
    }
}