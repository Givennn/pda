package com.panda.pda.app.operation.qms.quality_problem_record

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.SingleDateSelector
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.checkedChanges
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.base.extension.toast
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.common.OrgNodeSelectFragment
import com.panda.pda.app.common.WheelPickerDialogFragment
import com.panda.pda.app.common.data.CommonParameters
import com.panda.pda.app.common.data.DataParamType
import com.panda.pda.app.common.data.model.OrgNodeModel
import com.panda.pda.app.databinding.FragmentProblemRecordEditBinding
import com.panda.pda.app.operation.qms.NgReasonFragment
import com.panda.pda.app.operation.qms.data.QualityApi
import com.panda.pda.app.operation.qms.data.model.QualityNgReasonModel
import com.panda.pda.app.operation.qms.data.model.QualityProblemRecordDetailModel
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import io.reactivex.rxjava3.core.Single
import org.w3c.dom.Text
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * created by AnJiwei 2021/10/12
 */
class ProblemRecordEditFragment : BaseFragment(R.layout.fragment_problem_record_edit) {

    private lateinit var detailModel: QualityProblemRecordDetailModel

    private val viewBinding by viewBinding<FragmentProblemRecordEditBinding>()

    private val ngReasonAdapter by lazy { NgReasonFragment.getNgReasonAdapter() }

    private val verifyDialog by lazy {
        WheelPickerDialogFragment().also {
            it.pickerData = verifyData
        }
    }

    private val datePicker by lazy {
        MaterialDatePicker.Builder.datePicker().setTitleText(R.string.time_pick).build()
    }

    private lateinit var verifyData: List<String>

    private var selectedDateProperty: TextView? = null

    private var isEditProblem = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val detailStr = arguments?.getString(DETAIL_KEY)
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }
        detailModel = if (detailStr == null) {

            QualityProblemRecordDetailModel.create()
        } else {
            viewBinding.topAppBar.setTitle(R.string.edit_problem_record)
            isEditProblem = true
            getProblemRecordJsonAdapter().fromJson(detailStr)
                ?: QualityProblemRecordDetailModel.create()
        }

        updateDetail(detailModel)
        verifyData = CommonParameters.getParameters(DataParamType.CONCLUSION_DECIDE_OPTION)
            .sortedBy { it.paramValue }.map { it.paramDesc }
        viewBinding.apply {
            cbBaseInfoVisible.isClickable = false
            cbQualityCheckInfoVisible.isClickable = false
            llBaseInfoToggle.clicks()
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .bindToLifecycle(requireView())
                .subscribe { cbBaseInfoVisible.isChecked = !cbBaseInfoVisible.isChecked }
            llQualityCheckInfoToggle.clicks()
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .bindToLifecycle(requireView())
                .subscribe {
                    cbQualityCheckInfoVisible.isChecked = !cbQualityCheckInfoVisible.isChecked
                }
            cbBaseInfoVisible.checkedChanges()
                .bindToLifecycle(requireView())
                .subscribe { llBaseInfo.isVisible = it }
            cbQualityCheckInfoVisible.checkedChanges()
                .bindToLifecycle(requireView())
                .subscribe { llQualityCheckInfo.isVisible = it }
            llNgReason.clicks()
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .bindToLifecycle(requireView())
                .subscribe { showNgReasonSelect() }
            llVerifyResult.clicks()
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .bindToLifecycle(requireView())
                .subscribe { showVerifyResultSelect() }
            datePicker.addOnPositiveButtonClickListener { onTimePicked(it) }

            llInspectorTime.clicks()
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .bindToLifecycle(requireView())
                .subscribe {
                    selectedDateProperty = viewBinding.tvInspectorTime
                    datePicker.show(parentFragmentManager, TAG)
                }

            llTraceTime.clicks()
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .bindToLifecycle(requireView())
                .subscribe {
                    selectedDateProperty = viewBinding.tvTraceTime
                    datePicker.show(parentFragmentManager, TAG)
                }
            llTraceUser.clicks()
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .bindToLifecycle(requireView())
                .subscribe {
                    navController.navigate(
                        R.id.orgNodeSelectFragment,
                        Bundle().apply {
                            putString(
                                OrgNodeSelectFragment.TITLE_KEY,
                                getString(R.string.trace_user)
                            )
                        })
                }

            btnConfirm.clicks()
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .bindToLifecycle(requireView())
                .subscribe { commit(getDetailModel()) }
        }

        setFragmentResultListener(OrgNodeSelectFragment.ORG_NODE_RESULT) { requestKey, bundle ->
            if (requestKey == OrgNodeSelectFragment.ORG_NODE_RESULT) {
                val nodeModel =
                    bundle.getSerializable(OrgNodeSelectFragment.PERSON_SELECT_KEY) as? OrgNodeModel
                if (nodeModel != null) {
                    updateTraceUser(nodeModel)
                }
            }
        }
        setFragmentResultListener(NgReasonFragment.REQUEST_KEY) { requestKey, bundle ->
            if (requestKey == NgReasonFragment.REQUEST_KEY) {
                val ngReasonsStr = bundle.getString(NgReasonFragment.NG_REASON_ARG_KEY, "")
                detailModel.adverseCauseInfoList = ngReasonAdapter.fromJson(ngReasonsStr)
                viewBinding.tvNgReason.text = detailModel.adverseCauseInfoList?.joinToString(";") {
                    it.badnessReasonName
                }
            }
        }
    }

    private fun updateTraceUser(nodeModel: OrgNodeModel) {
        detailModel.traceUser = nodeModel.nodeId
        detailModel.traceDepartment = nodeModel.department?.nodeName
        detailModel.traceDepartmentId = nodeModel.department?.id
        viewBinding.tvTraceUser.text = nodeModel.nodeName
        viewBinding.tvTraceDepartment.text = nodeModel.department?.nodeName

    }

    private fun onTimePicked(it: Long) {
        val time = convertLongToTime(it)
        val tv = selectedDateProperty ?: return
        tv.text = time
        when (tv.id) {
            R.id.tv_inspector_time -> detailModel.inspectorTime = time
            R.id.tv_trace_time -> detailModel.traceTime = time
        }
    }

    private fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
        return format.format(date)
    }

    private fun showVerifyResultSelect() {

        verifyDialog.setConfirmButton { result ->
            detailModel.conclusion = result?.first
            viewBinding.tvVerifyResult.text = result!!.first
        }.show(parentFragmentManager, TAG)
    }

    private fun showNgReasonSelect() {

        WebClient.request(QualityApi::class.java)
            .pdaQmsQualityProblemGetBadnessListGet()
            .bindToFragment()
            .subscribe({

                if (it.dataList.isEmpty()) {
                    toast("请配置不良原因。")
                } else {
                    val ngReasons = ngReasonAdapter.toJson(it.dataList)
                    navController.navigate(
                        R.id.ngReasonFragment,
                        Bundle().apply { putString(NgReasonFragment.NG_REASON_ARG_KEY, ngReasons) }
                    )
                }
            }, {})
    }

    private fun getDetailModel(): QualityProblemRecordDetailModel {
        viewBinding.apply {

            detailModel.problemCode = etQualityProblemCode.text.toString()
            detailModel.productBarCode = etProductSerialCode.text.toString()
            detailModel.productCode = etProductCode.text.toString()
            detailModel.productName = etProductDesc.text.toString()
            detailModel.occurrencePlace = etOccurrencePlace.text.toString()
            detailModel.qualityCode = etQualityTaskCode.text.toString()
            detailModel.taskCode = etTaskCode.text.toString()
            detailModel.taskDesc = etTaskDescription.text.toString()
            detailModel.planCode = etPlanCode.text.toString()
            detailModel.workNo = etWorkOrderCode.text.toString()
            detailModel.batchNo = etBatchCode.text.toString()
            detailModel.orderNo = etOrderCode.text.toString()
            detailModel.inspector = etInspector.text.toString()
            detailModel.causeAnalysis = etCauseAnalysis.text.toString()
            detailModel.solution = etSolution.text.toString()
            detailModel.optimization = etOptimization.text.toString()
            detailModel.remark = etRemark.text.toString()
        }
        return detailModel
    }

    private fun updateDetail(model: QualityProblemRecordDetailModel) {
        viewBinding.apply {

            etQualityProblemCode.setText(model.problemCode)
            etProductSerialCode.setText(model.productBarCode)
            etProductCode.setText(model.productCode)
            etProductDesc.setText(model.productName)
            etOccurrencePlace.setText(model.occurrencePlace)
            etQualityTaskCode.setText(model.qualityCode)
            etTaskCode.setText(model.taskCode)
            etTaskDescription.setText(model.taskDesc)
            etPlanCode.setText(model.planCode)
            etWorkOrderCode.setText(model.workNo)
            etBatchCode.setText(model.batchNo)
            etOrderCode.setText(model.orderNo)
            etInspector.setText(model.inspector)
            etCauseAnalysis.setText(model.causeAnalysis)
            etSolution.setText(model.solution)
            etOptimization.setText(model.optimization)
            etRemark.setText(model.remark)
            tvNgReason.text = model.adverseCauseInfoList?.joinToString(";") {
                it.badnessReasonName
            }
            tvVerifyResult.text = model.conclusion
            etProcessCycle.setText(model.processCycle)
        }
    }

    private fun commit(model: QualityProblemRecordDetailModel) {
        val api = if (isEditProblem) {
            WebClient.request(QualityApi::class.java).pdaQmsQualityProblemEditPost(model)
        } else {
            WebClient.request(QualityApi::class.java).pdaQmsQualityProblemAddPost(model)
        }
        api.bindToFragment()
            .subscribe({
                toast(
                    if (!isEditProblem) {
                        "质检问题记录新建成功"
                    } else {
                        "质检问题记录编辑成功"
                    }
                )
                navBackListener.invoke(requireView())
            }, {})
    }

    //    private fun buildDetailModel(): QualityProblemRecordDetailModel {
//        val result = QualityProblemRecordDetailModel
//    }
    companion object {
        const val DETAIL_KEY = "detail_key"

        fun getProblemRecordJsonAdapter(): JsonAdapter<QualityProblemRecordDetailModel> {
            return Moshi.Builder().build().adapter(QualityProblemRecordDetailModel::class.java)
        }
    }
}