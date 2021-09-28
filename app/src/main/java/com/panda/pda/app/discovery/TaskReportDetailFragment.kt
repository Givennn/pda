package com.panda.pda.app.discovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.api.load
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.common.adapter.ViewBindingAdapter
import com.panda.pda.app.base.extension.toast
import com.panda.pda.app.common.ModelPropertyCreator
import com.panda.pda.app.common.data.CommonApi
import com.panda.pda.app.common.data.model.FileInfoModel
import com.panda.pda.app.databinding.FragmentTaskReportDetailBinding
import com.panda.pda.app.databinding.ItemTaskReportDetailPicBinding
import com.panda.pda.app.discovery.data.TaskReportDetailModel

/**
 * created by AnJiwei 2021/9/1
 */
class TaskReportDetailFragment : BaseFragment(R.layout.fragment_task_report_detail) {
    private lateinit var picBindingAdapter: ViewBindingAdapter<ItemTaskReportDetailPicBinding, FileInfoModel>
    private val viewBinding by viewBinding<FragmentTaskReportDetailBinding>()
    private val viewModel by activityViewModels<TaskReportViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener.invoke(requireView()) }
        val detailData = viewModel.taskReportDetailData.value.also {
            if (it == null) {
                toast("未取得报工详情数据")
                navBackListener.invoke(requireView())
            }
        } ?: return
        val modelPropertyCreator = ModelPropertyCreator(
            TaskReportDetailModel::class.java,
            viewBinding.modelLayout,
            1
        )
        viewBinding.apply {
//            tvOrderCode.text = detailData.orderNo
//            tvPlanCode.text = detailData.planCode
//            tvWorkOrderCode.text = detailData.workNo
//            tvUnifyCode.text = detailData.batchNo
//            tvTaskCode.text = detailData.taskCode
//            tvTaskDesc.text = detailData.taskDesc
//            tvProductCode.text = detailData.productCode
//            tvProductDesc.text = detailData.productName
//            tvReportNumber.text = detailData.taskNum.toString()
//            tvManHour.text =  DateUtils.getManHour(detailData.reportTime)
            tvRemark.text = detailData.remark
            modelPropertyCreator.setData(detailData)
        }

        picBindingAdapter = object :
            ViewBindingAdapter<ItemTaskReportDetailPicBinding, FileInfoModel>(detailData.fileList.toMutableList()) {
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
        picBindingAdapter.refreshData(detailData.fileList)
    }
}