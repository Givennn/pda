package com.panda.pda.mes.operation.qms.ng_product_deall

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.extension.getStringObject
import com.panda.pda.mes.base.extension.toast
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.ModelPropertyCreator
import com.panda.pda.mes.common.MultiSelectBottomDialogFragment
import com.panda.pda.mes.common.WheelPickerDialogFragment
import com.panda.pda.mes.common.data.CommonParameters
import com.panda.pda.mes.common.data.DataParamType
import com.panda.pda.mes.databinding.FragmentNgProductEditBinding
import com.panda.pda.mes.operation.qms.data.QualityApi
import com.panda.pda.mes.operation.qms.data.model.NGProductItemModel
import com.panda.pda.mes.operation.qms.data.model.ProcedureItem
import com.panda.pda.mes.operation.qms.data.model.QualityTaskModel
import io.reactivex.rxjava3.core.Single
import java.lang.Exception

/**
 * created by AnJiwei 2022/11/21
 */
class NGProductEditFragment: BaseFragment(R.layout.fragment_ng_product_edit) {

    private val viewBinding by viewBinding<FragmentNgProductEditBinding>()

    private var qualityTask: QualityTaskModel? = null

    private lateinit var ngProductItem: NGProductItemModel

    private var currentTaskProcedureItemList: List<ProcedureItem>? = null

    private var addOrEdit = true

    private val adverseDealTypeMap by lazy {
        CommonParameters.getParameters(DataParamType.QMS_ADVERSE_DEAL_TYPE)
    }
    private val adverseDealTypeDialog by lazy {
        WheelPickerDialogFragment().also {
            it.pickerData = adverseDealTypeMap.map { parameter -> parameter.paramDesc }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        qualityTask = arguments?.getStringObject<QualityTaskModel>()
        val productItem = arguments?.getStringObject<NGProductItemModel>()
        if (productItem != null) {
            viewBinding.topAppBar.title = "编辑不合格品处理"
            ngProductItem = productItem
            addOrEdit = false
        } else {
            viewBinding.topAppBar.title = "新建不合格品处理"
            ngProductItem = NGProductItemModel(1)
        }
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }


        adverseDealTypeDialog.setConfirmButton { result ->
            if (result != null) {
                val method = adverseDealTypeMap.getOrNull(result.second)
                if (method != null) {
                    viewBinding.tvSelectedDealMethod.text = method.paramDesc
                    ngProductItem.dealType = method.paramValue
//                    viewBinding.tvExceptionType.text = selectedException.paramDesc
                }
            }
        }
        viewBinding.apply {
            btnCancel.setOnClickListener {
                navBackListener.invoke(requireView())
            }

            btnConfirm.setOnClickListener {
                commit()
            }

            llDealMethod.setOnClickListener {
                selectDealMethod()
            }

            llNgProcedure.setOnClickListener {
                selectNgProcedure()
            }

            llReturnProcedure.setOnClickListener {
                selectReturnProcedure()
            }
        }

        val modelProperty = ModelPropertyCreator(
            QualityTaskModel::class.java,
            viewBinding.llPropertyInfo,
        )
        if (qualityTask != null) {
            modelProperty.setData(qualityTask!!)
        }
    }

    private fun selectReturnProcedure() {
        requestProcedure().subscribe({
            val ngSelectDialog = MultiSelectBottomDialogFragment<ProcedureItem>().apply {
                pickerData = it
            }.setConfirmButton {
                ngProductItem.procedureReturnList = it
                viewBinding.tvSelectedReturnProcedure.text = it.joinToString { item -> item.procedureName }
            }

            ngSelectDialog.show(parentFragmentManager, TAG)
        }, {})

    }

    private fun selectNgProcedure() {
        requestProcedure().subscribe({
            val ngSelectDialog = MultiSelectBottomDialogFragment<ProcedureItem>().apply {
                pickerData = it
            }.setConfirmButton {
                ngProductItem.procedureBadList = it
                viewBinding.tvSelectedNgProcedure.text = it.joinToString { item -> item.procedureName }
            }

            ngSelectDialog.show(parentFragmentManager, TAG)
        }, {})

    }

    private fun selectDealMethod() {
        if (adverseDealTypeDialog.pickerData.isEmpty()) {
            toast(R.string.net_work_error)
        } else {
            adverseDealTypeDialog.show(parentFragmentManager, TAG)
        }
    }

    private fun requestProcedure(): Single<List<ProcedureItem>> {
        if (currentTaskProcedureItemList != null) {
            return Single.just(currentTaskProcedureItemList!!)
        }
        if (qualityTask != null) {
            return WebClient.request(QualityApi::class.java)
                .adverseDealProcedureListGet(qualityTask!!.id)
                .bindToFragment()
                .map { it.dataList }
        }
        return Single.error(Exception(getString(R.string.net_work_error)))
    }

    private fun commit() {
        val remark = viewBinding.etRemark.text.toString()

        ngProductItem.remark = remark
        val api = if (addOrEdit) {
            WebClient.request(QualityApi::class.java)
                .adverseDealAddRecordPost(ngProductItem)
        } else {
            WebClient.request(QualityApi::class.java)
                .adverseDealEditRecordPost(ngProductItem)
        }
        api.bindToFragment()
            .subscribe({
                val message = if (addOrEdit) "不良品记录添加成功。" else "不良品记录编辑成功"
                toast(message)
                navBackListener.invoke(requireView())
            }, {})
    }
}