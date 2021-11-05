package com.panda.pda.app.operation.qms.quality_task_detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.base.extension.getStringObject
import com.panda.pda.app.common.ModelPropertyCreator
import com.panda.pda.app.databinding.FragmentQualityDetailInfoBinding
import com.panda.pda.app.operation.qms.QualityViewModel
import com.panda.pda.app.operation.qms.data.model.QualityDetailModel
import com.panda.pda.app.operation.qms.data.model.QualitySubTaskDetailModel

/**
 * created by AnJiwei 2021/9/28
 */
class QualityDetailInfoFragment(
    private val subTaskDetail: QualitySubTaskDetailModel?,
    private val taskDetail: QualityDetailModel?
) : BaseFragment(R.layout.fragment_quality_detail_info) {

    private val viewBinding by viewBinding<FragmentQualityDetailInfoBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (subTaskDetail != null) {
            val modelProperty = ModelPropertyCreator(
                QualitySubTaskDetailModel::class.java,
                viewBinding.llPropertyInfo,
                tag = QualityDetailModel.QualityDetailTag
            )
            modelProperty.setData(subTaskDetail)
            return
        }
        if (taskDetail != null) {
            val modelProperty = ModelPropertyCreator(
                QualityDetailModel::class.java,
                viewBinding.llPropertyInfo,
                tag = QualityDetailModel.QualityDetailTag
            )
            modelProperty.setData(taskDetail)
        }

    }
}