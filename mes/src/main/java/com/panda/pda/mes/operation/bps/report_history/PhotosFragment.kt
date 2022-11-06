package com.panda.pda.mes.operation.bps.report_history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.common.data.CommonApi
import com.panda.pda.mes.common.data.model.FileInfoModel
import com.panda.pda.mes.databinding.FragmentReportHistoryPhotosBinding
import com.panda.pda.mes.databinding.ItemTaskReportDetailPicBinding
import com.panda.pda.mes.operation.bps.data.model.MainPlanReportHistoryModel

/**
 * created by AnJiwei 2022/11/1
 */
class PhotosFragment(
    private val detail: MainPlanReportHistoryModel?,
) : BaseFragment(R.layout.fragment_report_history_photos) {

    private val viewBinding by viewBinding<FragmentReportHistoryPhotosBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (detail != null) {
            viewBinding.rvPicList.adapter = object :
                CommonViewBindingAdapter<ItemTaskReportDetailPicBinding, FileInfoModel>(detail.fileList.toMutableList()) {
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
        }
    }
}