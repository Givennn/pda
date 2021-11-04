package com.panda.pda.app.operation.qms.quality_sign

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import by.kirich1409.viewbindingdelegate.viewBinding
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.base.extension.toast
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.common.ModelPropertyCreator
import com.panda.pda.app.common.OrgNodeSelectFragment
import com.panda.pda.app.common.data.model.IdRequest
import com.panda.pda.app.common.data.model.OrgNodeModel
import com.panda.pda.app.databinding.FragmentQualityDistributeBackOutBinding
import com.panda.pda.app.operation.qms.QualityViewModel
import com.panda.pda.app.operation.qms.data.QualityApi
import com.panda.pda.app.operation.qms.data.model.QualityDetailModel
import com.panda.pda.app.operation.qms.data.model.QualitySubTaskDetailModel
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import java.util.concurrent.TimeUnit

/**
 * created by AnJiwei 2021/10/9
 */
class QualitySignBackOutFragment :
    BaseFragment(R.layout.fragment_quality_distribute_back_out) {

    private val viewBinding by viewBinding<FragmentQualityDistributeBackOutBinding>()

    private val viewModel by activityViewModels<QualityViewModel>()

    private var selectedVerifier: OrgNodeModel? = null

    private lateinit var currentQualityTask: QualitySubTaskDetailModel

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
            QualitySubTaskDetailModel::class.java,
            viewBinding.llPropertyInfo
        )
        viewModel.qualityDetailSubTaskData.observe(viewLifecycleOwner) {
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
                cancel()
            }
    }

    private fun updateVerifier(verifier: OrgNodeModel) {
        selectedVerifier = verifier
        viewBinding.tvVerifier.text = verifier.nodeName
    }

    private fun cancel() {
        if (selectedVerifier == null) {
            toast("请选择审核人")
            return
        }
        WebClient.request(QualityApi::class.java)
            .qualityDistributeCancel(
                IdRequest(currentQualityTask.id)
            )
            .bindToFragment()
            .subscribe({
                toast(R.string.quality_task_back_out_success)
                navBackListener.invoke(requireView())
            }, {})
    }
}