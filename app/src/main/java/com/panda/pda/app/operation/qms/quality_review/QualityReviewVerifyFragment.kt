package com.panda.pda.app.operation.qms.quality_review

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.base.extension.toast
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.common.ModelPropertyCreator
import com.panda.pda.app.databinding.FragmentQualityReviewVerifyBinding
import com.panda.pda.app.operation.qms.QualityViewModel
import com.panda.pda.app.operation.qms.data.QualityApi
import com.panda.pda.app.operation.qms.data.model.QualityDetailModel
import com.panda.pda.app.operation.qms.data.model.QualityTaskReviewRequest
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import java.util.concurrent.TimeUnit

/**
 * created by AnJiwei 2021/10/8
 */
class QualityReviewVerifyFragment : BaseFragment(R.layout.fragment_quality_review_verify) {

    private val viewBinding by viewBinding<FragmentQualityReviewVerifyBinding>()

    private val viewModel by activityViewModels<QualityViewModel>()

    private val verifyResult: Int? = null

    private lateinit var currentQualityDetail: QualityDetailModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }
        val modelProperty = ModelPropertyCreator(
            QualityDetailModel::class.java,
            viewBinding.llPropertyInfo
        )
        viewModel.qualityDetailInfoData.observe(viewLifecycleOwner) {
            modelProperty.setData(it)
            currentQualityDetail = it
        }

        viewBinding.llVerifyResult.clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .bindToLifecycle(requireView())
            .subscribe {
                showVerityDialog()
            }
        viewBinding.btnConfirm.clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .bindToLifecycle(requireView())
            .subscribe {
                review()
            }
    }

    private fun review() {
        val conclusion = verifyResult
        if (conclusion == null) {
            toast("请选择审核结果。")
            return
        }
        val remark = viewBinding.etRemark.text.toString()
        WebClient.request(QualityApi::class.java)
            .pdaQmsReviewReviewPost(QualityTaskReviewRequest(currentQualityDetail.id, conclusion, remark))
            .bindToFragment()
            .subscribe({
                toast(R.string.quality_review_success_message)
                navBackListener.invoke(requireView())
            }, {})
    }

    private fun showVerityDialog() {
        TODO("Not yet implemented")
    }
}