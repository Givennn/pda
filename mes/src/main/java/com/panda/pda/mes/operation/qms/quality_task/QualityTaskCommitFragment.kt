package com.panda.pda.mes.operation.qms.quality_task

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import by.kirich1409.viewbindingdelegate.viewBinding
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.extension.toast
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.ModelPropertyCreator
import com.panda.pda.mes.common.OrgNodeSelectFragment
import com.panda.pda.mes.common.data.model.OrgNodeModel
import com.panda.pda.mes.databinding.FragmentQualityCommitBinding
import com.panda.pda.mes.operation.qms.QualityViewModel
import com.panda.pda.mes.operation.qms.data.QualityApi
import com.panda.pda.mes.operation.qms.data.model.QualityDetailModel
import com.panda.pda.mes.operation.qms.data.model.QualityTaskCommitRequest
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import java.util.concurrent.TimeUnit

/**
 * created by AnJiwei 2021/9/29
 */
class QualityTaskCommitFragment : BaseFragment(R.layout.fragment_quality_commit) {

    private val viewBinding by viewBinding<FragmentQualityCommitBinding>()

    private val viewModel by activityViewModels<QualityViewModel>()

    private var selectedVerifier: OrgNodeModel? = null

    private lateinit var currentQualityTask: QualityDetailModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentResultListener(OrgNodeSelectFragment.ORG_NODE_RESULT) { requestKey, bundle ->
            if (requestKey == OrgNodeSelectFragment.ORG_NODE_RESULT) {
                val nodeModel =
                    bundle.getSerializable(OrgNodeSelectFragment.PERSON_SELECT_KEY) as? OrgNodeModel
                if (nodeModel != null) {
                    updateVerifier(nodeModel)
                }
            }
        }
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }
        val modelProperty = ModelPropertyCreator(
            QualityDetailModel::class.java,
            viewBinding.llPropertyInfo
        )
        viewModel.qualityDetailInfoData.observe(viewLifecycleOwner) {
            modelProperty.setData(it)
            currentQualityTask = it
        }

        viewBinding.llSelectVerifier.clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .bindToLifecycle(requireView())
            .subscribe {
                navController.navigate(
                    R.id.orgNodeSelectFragment,
                    Bundle().apply {
                        putString(
                            OrgNodeSelectFragment.TITLE_KEY,
                            getString(R.string.verifier)
                        )
                    })
            }
        viewBinding.btnConfirm.clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .bindToLifecycle(requireView())
            .subscribe {
                commit()
            }
    }

    private fun updateVerifier(verifier: OrgNodeModel) {
        selectedVerifier = verifier
        viewBinding.tvVerifier.text = verifier.nodeName
    }

    private fun commit() {
        if (selectedVerifier == null) {
            toast("请选择审核人")
            return
        }
        val verifierId = selectedVerifier!!.id
        val remark = viewBinding.etRemark.text.toString()
        WebClient.request(QualityApi::class.java)
            .pdaQmsTaskCommitPost(QualityTaskCommitRequest(currentQualityTask.id, verifierId, remark))
            .bindToFragment()
            .subscribe({
                toast(R.string.quality_task_commit_success)
                navBackListener.invoke(requireView())
            }, {})
    }
}