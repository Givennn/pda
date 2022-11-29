package com.panda.pda.mes.operation.qms.ng_product_deall

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.extension.getStringObject
import com.panda.pda.mes.common.ModelPropertyCreator
import com.panda.pda.mes.databinding.FragmentNgProductEditBinding
import com.panda.pda.mes.operation.qms.data.model.NGProductItemModel
import com.panda.pda.mes.operation.qms.data.model.QualityTaskModel

/**
 * created by AnJiwei 2022/11/21
 */
class NGProductEditFragment: BaseFragment(R.layout.fragment_ng_product_edit) {

    private val viewBinding by viewBinding<FragmentNgProductEditBinding>()

    private var qualityTask: QualityTaskModel? = null

    private var ngProductItem: NGProductItemModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        qualityTask = arguments?.getStringObject<QualityTaskModel>()
        ngProductItem = arguments?.getStringObject<NGProductItemModel>()
        viewBinding.topAppBar.title =  if (ngProductItem != null) {
            "编辑不合格品处理"
        } else {
            "新建不合格品处理"
        }
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }

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

    }

    private fun selectNgProcedure() {

    }

    private fun selectDealMethod() {

    }

    private fun commit() {
    }
}