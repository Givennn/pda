package com.panda.pda.mes.operation.qms.quality_execute

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.extension.getStringObject
import com.panda.pda.mes.base.extension.toast
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.ModelPropertyCreator
import com.panda.pda.mes.common.WheelPickerDialogFragment
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.common.data.CommonParameters
import com.panda.pda.mes.common.data.DataParamType
import com.panda.pda.mes.common.data.model.QMSModuleProperty
import com.panda.pda.mes.common.data.model.TaskRunType
import com.panda.pda.mes.databinding.FragmentExecuteInputBinding
import com.panda.pda.mes.databinding.ItemQualityCheckItemBinding
import com.panda.pda.mes.operation.qms.NgReasonFragment
import com.panda.pda.mes.operation.qms.QualityViewModel
import com.panda.pda.mes.operation.qms.data.QualityApi
import com.panda.pda.mes.operation.qms.data.model.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import java.util.concurrent.TimeUnit

/**
 * created by AnJiwei 2021/10/13
 */
class ExecuteInputFragment : BaseFragment(R.layout.fragment_execute_input) {

    private var selectedNgList: List<QualityNgReasonModel>? = null
    private val viewBinding by viewBinding<FragmentExecuteInputBinding>()

    private val subTaskDetailModel: QualitySubTaskDetailModel by lazy {
        requireArguments().getStringObject<QualitySubTaskDetailModel>()!!
    }

    private val ngReasonAdapter by lazy { NgReasonFragment.getNgReasonAdapter() }

    private val conclusionDialog by lazy {
        WheelPickerDialogFragment().also {
            it.pickerData = listOf("合格", "不合格")
        }
    }

    private val verifyOptionDialog by lazy {
        WheelPickerDialogFragment().also {
            it.pickerData = verifyResultList ?: listOf()
        }
    }

    private val inspectItemAdapter by lazy {
        Moshi.Builder().build().adapter<List<QualityInspectItemModel>>(
            Types.newParameterizedType(
                List::class.java,
                QualityInspectItemModel::class.java
            )
        )
    }

    private lateinit var inspectItems: List<QualityInspectItemModel>

    private var verifyResultList: List<String>? = null

    private var selectedVerifyResult: String? = null

    private val qualityItemsAdapter: CommonViewBindingAdapter<ItemQualityCheckItemBinding, QualityInspectItemModel> by lazy {
        object :
            CommonViewBindingAdapter<ItemQualityCheckItemBinding, QualityInspectItemModel>(
                inspectItems.toMutableList()
            ) {
            override fun createBinding(parent: ViewGroup): ItemQualityCheckItemBinding {
                return ItemQualityCheckItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            }

            override fun onBindViewHolderWithData(
                holder: ViewBindingHolder,
                data: QualityInspectItemModel,
                position: Int,
            ) {
                val isValueType =
                    data.valueType == CommonParameters.getParameters(DataParamType.VALUE_TYPE)
                        .firstOrNull { it.paramDesc == "数值型" }?.paramValue
                holder.itemViewBinding.ivArrow.isVisible = !isValueType
                holder.itemViewBinding.tvSelectValue.isVisible = !isValueType
                holder.itemViewBinding.tvInputValue.isVisible = isValueType
                holder.itemViewBinding.tvLabel.text = data.qualityName
                holder.itemViewBinding.tvInputValue.doAfterTextChanged {
                    data.conclusion = it.toString()
                }

                val conclusion = data.conclusion ?: ""
                if (!isValueType) {
                    holder.itemViewBinding.root.setOnClickListener {
                        showConclusionSelectDialog(data, holder.itemViewBinding.tvSelectValue)
                    }
                    holder.itemViewBinding.tvSelectValue.text = conclusion;
                } else {
                    holder.itemViewBinding.tvInputValue.setText(conclusion);
                }
            }
        }
    }

    private val qualityViewModel by activityViewModels<QualityViewModel>()

    private var taskRunType = TaskRunType.SerialCode

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }
        inspectItems =
            inspectItemAdapter.fromJson(requireArguments().getString(QUALITY_ITEM_KEY, ""))
                ?: listOf()
        val modelProperty =
            ModelPropertyCreator(QualitySubTaskDetailModel::class.java, viewBinding.llPropertyInfo)
        modelProperty.setData(subTaskDetailModel)

        viewBinding.rvQualityItem.adapter = qualityItemsAdapter

        lifecycleScope.launchWhenStarted {
            val parameter = qualityViewModel.getQmsSysProperty(QMSModuleProperty.taskRunType)
            if (parameter != null) {
                taskRunType = TaskRunType.getTaskRunType(parameter)
                viewBinding.tvProductCodeOrNumber.text =
                    when(taskRunType) {
                        TaskRunType.SerialCode -> getString(R.string.product_serial_code)
                        TaskRunType.Encode -> getString(R.string.product_number)
                    }
            }

        }
        viewBinding.llNgReason.clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .bindToLifecycle(requireView())
            .subscribe()
            { navToNgReasonSelect() }


        viewBinding.llVerifyResult.clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .bindToLifecycle(requireView())
            .subscribe()
            { showVerifyResultDialog() }

        viewBinding.btnConfirm.clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .bindToLifecycle(requireView())
            .subscribe()
            { confirm() }

        setFragmentResultListener(NgReasonFragment.REQUEST_KEY)
        { requestKey, bundle ->
            if (requestKey == NgReasonFragment.REQUEST_KEY) {
                val ngReasonsStr = bundle.getString(NgReasonFragment.NG_REASON_ARG_KEY, "")
                selectedNgList = ngReasonAdapter.fromJson(ngReasonsStr)
                viewBinding.tvNgReason.text = selectedNgList?.joinToString(";") {
                    it.badnessReasonName
                }
            }
        }
    }

    private fun confirm() {

        var productSerialCode = ""
        var productCount: Int? = null
        when (taskRunType) {
            TaskRunType.SerialCode -> {
                productSerialCode = viewBinding.etProductSerialCode.text.toString()
                if (productSerialCode.isEmpty()) {
                    toast("请输入产品条码！")
                    return
                }
            }
            TaskRunType.Encode -> {
                productCount = viewBinding.etProductSerialCode.text.toString().toIntOrNull()
                if (productCount == null) {
                    toast("请输入产品数量！")
                    return
                }
            }
        }

        if (selectedVerifyResult == null) {
            toast("请选择审核结论！")
            return
        }
        val qualityItems = readQualityItems() ?: return
        val requestBody = QualityTaskExecuteRequest(
            subTaskDetailModel.id,
            selectedVerifyResult!!,
            productSerialCode,
            productCount,
            selectedNgList?.map { it.id },
            qualityItems
        )
        WebClient.request(QualityApi::class.java)
            .pdaQmsQualitySubTaskExecutePost(requestBody)
            .bindToFragment()
            .subscribe({
                toast(R.string.quality_task_execute_success)
                navBackListener.invoke(requireView())
            }, {})
    }

    /**
     * "QUALITY_FILL_IN_REGULATION": [
    {
    "paramName": "QUALITY_FILL_IN_REGULATION",
    "paramValue": "0",
    "paramDesc": "全部填写"
    },
    {
    "paramName": "QUALITY_FILL_IN_REGULATION",
    "paramValue": "1",
    "paramDesc": "可以为空"
    },
    {
    "paramName": "QUALITY_FILL_IN_REGULATION",
    "paramValue": "2",
    "paramDesc": "不选时默认为合格"
    }
    ]
     */
    private fun readQualityItems(): List<QualityItem>? {
        val result = mutableListOf<QualityItem>()
        qualityItemsAdapter.dataSource.forEach { model ->
            when (model.qualityFillInRegulation) {
                0 -> {
                    if (model.conclusion.isNullOrEmpty()) {
                        toast("请填写或选择质检项:${model.qualityName}结果")
                        return null
                    } else {
                        result.add(QualityItem(model.id, model.conclusion!!))
                    }
                }
                1 -> {
                    if (!model.conclusion.isNullOrEmpty()) {
                        result.add(QualityItem(model.id, model.conclusion!!))
                    }
                }
                2 -> {
                    result.add(
                        QualityItem(
                            model.id,
                            if (model.conclusion.isNullOrEmpty()) "合格" else model.conclusion!!
                        )
                    )
                }

                else -> {
                }
            }
        }
        return result
    }

    private fun showVerifyResultDialog() {
        if (verifyResultList == null) {
            WebClient.request(QualityApi::class.java)
                .pdaQmsQualitySubTaskGetConclusionOptionGet(subTaskDetailModel.id)
                .bindToFragment()
                .subscribe({
                    verifyResultList = it.dataList
                    showVerifyResultDialog()
                }, {})
        } else {
            verifyOptionDialog.setConfirmButton { result ->
                selectedVerifyResult = result!!.first
                viewBinding.tvVerifyResult.text = selectedVerifyResult
            }.show(parentFragmentManager, TAG)
        }
    }

    private fun showConclusionSelectDialog(
        data: QualityInspectItemModel,
        tvSelectValue: TextView,
    ) {
        conclusionDialog.setConfirmButton { result ->
            data.conclusion = result!!.first
            tvSelectValue.text = result.first
        }.show(parentFragmentManager, TAG)

    }

    private fun navToNgReasonSelect() {
        WebClient.request(QualityApi::class.java)
            .pdaQmsQualitySubTaskGetBadnessListGet(subTaskDetailModel.id)
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

    companion object {
        const val DETAIL_KEY = "detail_key"
        const val QUALITY_ITEM_KEY = "quality_item_key"
    }
}