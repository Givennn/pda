package com.panda.pda.app.operation.qms.quality_task

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
import com.panda.pda.app.databinding.FragmentQualityCommitBinding
import com.panda.pda.app.operation.qms.QualityViewModel
import com.panda.pda.app.operation.qms.data.QualityApi
import com.panda.pda.app.operation.qms.data.model.QualityDetailModel
import com.panda.pda.app.operation.qms.data.model.QualityTaskCommitRequest
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import java.util.concurrent.TimeUnit

/**
 * created by AnJiwei 2021/9/29
 */
class QualityTaskCommitFragment : BaseFragment(R.layout.fragment_quality_commit) {

    private val viewBinding by viewBinding<FragmentQualityCommitBinding>()

    private val viewModel by activityViewModels<QualityViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }
        val modelProperty = ModelPropertyCreator(
            QualityDetailModel::class.java,
            viewBinding.llPropertyInfo
        )
        viewModel.qualityDetailInfoData.observe(viewLifecycleOwner) {
            modelProperty.setData(it)
        }

        viewBinding.btnConfirm.clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .bindToLifecycle(requireView())
            .subscribe {
                commit()
            }
    }

    private fun commit() {
        val verifierId = 0 //TODO get verifierId
        val remark = viewBinding.etRemark.text.toString()
        WebClient.request(QualityApi::class.java)
            .pdaQmsTaskCommitPost(QualityTaskCommitRequest(verifierId, remark))
            .bindToFragment()
            .subscribe({
                toast(R.string.quality_task_commit_success)
                navBackListener.invoke(requireView())
            }, {})
    }
}