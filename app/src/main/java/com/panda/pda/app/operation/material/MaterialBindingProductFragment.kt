package com.panda.pda.app.operation.material

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.base.BaseRecycleViewAdapter
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.databinding.FragmentMaterialBindingProductBinding
import com.panda.pda.app.databinding.ItemBindMaterialCodeBinding
import com.panda.pda.app.operation.data.MaterialApi
import com.panda.pda.app.operation.data.model.MaterialBindRequest
import com.panda.pda.app.operation.data.model.MaterialModel
import com.panda.pda.app.operation.data.model.ProductModel
import com.panda.pda.app.operation.data.model.TaskBandedMaterialModel
import com.panda.pda.app.task.data.model.TaskModel

/**
 * created by AnJiwei 2021/8/31
 */
class MaterialBindingProductFragment : BaseFragment(R.layout.fragment_material_binding_product) {

    private lateinit var taskModel: TaskModel
    private lateinit var productModel: ProductModel
    private lateinit var bindingAdapter: BaseRecycleViewAdapter<ItemBindMaterialCodeBinding, MaterialModel>
    private val viewBinding by viewBinding<FragmentMaterialBindingProductBinding>()

    private val materialViewModel by activityViewModels<MaterialViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener(it) }
        materialViewModel.scannedProductData.observe(viewLifecycleOwner, {
            viewBinding.tvProductSerialCode.text = it.also { productModel = it }.code
            taskModel = materialViewModel.selectedTaskData.value!!
            requestBindData(taskModel.id, it.code)
        })
        viewBinding.etSearchBar.setOnEditorActionListener { editText, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onScanMaterialCode(editText.text.toString())
            } else if (event.keyCode == KeyEvent.KEYCODE_ENTER) {
                onScanMaterialCode(editText.text.toString())
                (editText as EditText).selectAll()
                return@setOnEditorActionListener true
            }
            false
        }
        viewBinding.rvBindList.adapter =
            object : BaseRecycleViewAdapter<ItemBindMaterialCodeBinding, MaterialModel>(
                mutableListOf()) {
                override fun createBinding(parent: ViewGroup): ItemBindMaterialCodeBinding {
                    return ItemBindMaterialCodeBinding.inflate(LayoutInflater.from(parent.context),
                        parent,
                        false)
                }

                override fun onBindViewHolderWithData(
                    holder: ViewBindingHolder,
                    data: MaterialModel,
                    position: Int,
                ) {
                    holder.itemViewBinding.apply {
                        tvMaterialCode.text =
                            if (data.materialCode.isEmpty()) "-" else data.materialCode
                        tvMaterialDesc.text = data.materialName
                    }
                }
            }.also { bindingAdapter = it }
    }

    private fun onScanMaterialCode(code: String) {
        WebClient.request(MaterialApi::class.java)
            .taskMaterialBindPost(MaterialBindRequest(taskModel.id, productModel.code, code))
            .bindToFragment()
            .subscribe({ requestBindData(taskModel.id, productModel.code) }, {})
    }

    private fun requestBindData(taskId: Int, productCode: String) {
        WebClient.request(MaterialApi::class.java)
            .materialTaskQueryBindByProductGet(productCode, taskId)
            .bindToFragment()
            .subscribe({ data -> updateBindMaterial(data) }, {})
    }

    private fun updateBindMaterial(data: TaskBandedMaterialModel) {
        viewBinding.apply {
            tvBindCount.text = getString(R.string.material_banded_count_format,
                data.totalBindCount,
                data.totalBindCount + data.totalToBindCount)
        }
        bindingAdapter.refreshData(data.bindList + data.toBindList)
    }
}