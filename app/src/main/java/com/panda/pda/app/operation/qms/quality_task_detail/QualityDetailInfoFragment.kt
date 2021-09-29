package com.panda.pda.app.operation.qms.quality_task_detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.common.ModelPropertyCreator
import com.panda.pda.app.databinding.FragmentQualityDetailInfoBinding
import com.panda.pda.app.operation.qms.QualityViewModel
import com.panda.pda.app.operation.qms.data.model.QualityDetailModel

/**
 * created by AnJiwei 2021/9/28
 */
class QualityDetailInfoFragment : BaseFragment(R.layout.fragment_quality_detail_info) {

    private val viewBinding by viewBinding<FragmentQualityDetailInfoBinding>()
    private val viewModel by activityViewModels<QualityViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val modelProperty = ModelPropertyCreator(
            QualityDetailModel::class.java,
            viewBinding.llPropertyInfo,
            tag = QualityDetailModel.QualityDetailTag
        )
        viewModel.qualityDetailInfoData.observe(viewLifecycleOwner, {
            modelProperty.setData(it)
        })
    }
}