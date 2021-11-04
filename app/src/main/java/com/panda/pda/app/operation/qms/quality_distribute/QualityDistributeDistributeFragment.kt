package com.panda.pda.app.operation.qms.quality_distribute

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.base.extension.toast
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.common.ModelPropertyCreator
import com.panda.pda.app.common.OrgNodeSelectFragment
import com.panda.pda.app.common.data.model.OrgNodeModel
import com.panda.pda.app.databinding.FragmentQualityDistributeDistributeBinding
import com.panda.pda.app.operation.qms.QualityViewModel
import com.panda.pda.app.operation.qms.data.QualityApi
import com.panda.pda.app.operation.qms.data.model.QualityDetailModel
import com.panda.pda.app.operation.qms.data.model.QualityTaskDistributeRequest
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * created by AnJiwei 2021/10/9
 */
class QualityDistributeDistributeFragment :
    BaseFragment(R.layout.fragment_quality_distribute_distribute) {

    private val viewBinding by viewBinding<FragmentQualityDistributeDistributeBinding>()

    private val viewModel by activityViewModels<QualityViewModel>()

    private var selectedVerifier: OrgNodeModel? = null

    private var planTime: Pair<String, String>? = null

    private lateinit var currentQualityTask: QualityDetailModel

    private val datePicker by lazy {
        MaterialDatePicker.Builder.dateRangePicker().setTitleText(R.string.plan_data).build()
    }

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

        datePicker.addOnPositiveButtonClickListener {

            planTime = Pair(convertLongToTime(it.first), convertLongToTime(it.second))
            viewBinding.tvPlanDate.text =
                "${planTime!!.first}~${planTime!!.second}"
        }
        viewBinding.llSelectPlanDate.clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .bindToLifecycle(requireView())
            .subscribe {
                datePicker.show(parentFragmentManager, "plan_date")
            }
        viewBinding.btnConfirm.clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .bindToLifecycle(requireView())
            .subscribe {
                distribute()
            }
    }

    private fun distribute() {
        if (selectedVerifier == null) {
            toast("请选择审核人")
            return
        }
        if (planTime == null) {
            toast("请选择预计时间")
            return
        }
        WebClient.request(QualityApi::class.java)
            .pdaQmsDistributeDistributePost(
                QualityTaskDistributeRequest(
                    currentQualityTask.id,
                    viewBinding.tilReportNum.getValue,
                    selectedVerifier!!.id,
                    planTime!!.first,
                    planTime!!.second
                )
            )
            .bindToFragment()
            .subscribe({
                toast("质检任务派发成功")
                navBackListener.invoke(requireView())
            }, {})
    }

    private fun updateVerifier(verifier: OrgNodeModel) {
        selectedVerifier = verifier
        viewBinding.tvVerifier.text = verifier.nodeName
    }

    private fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(date)
    }

//    private fun transfer() {
//        if (selectedVerifier == null) {
//            toast("请选择审核人")
//            return
//        }
//        val verifierId = selectedVerifier!!.nodeId!!
//        val remark = viewBinding.etRemark.text.toString()
//        WebClient.request(QualityApi::class.java)
//            .pdaQmsReviewTransferPost(
//                QualityTaskTransferRequest(
//                    currentQualityTask.id,
//                    verifierId,
//                    remark
//                )
//            )
//            .bindToFragment()
//            .subscribe({
//                toast(R.string.quality_task_commit_success)
//                navBackListener.invoke(requireView())
//            }, {})
//    }
}