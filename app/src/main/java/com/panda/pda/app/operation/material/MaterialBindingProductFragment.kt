package com.panda.pda.app.operation.material

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.text.color
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.common.adapter.CommonRecycleViewAdapter
import com.panda.pda.app.base.extension.deepCopy
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.databinding.FragmentMaterialBindingProductBinding
import com.panda.pda.app.databinding.ItemBindMaterialCodeBinding
import com.panda.pda.app.operation.data.MaterialApi
import com.panda.pda.app.operation.data.model.MaterialBindRequest
import com.panda.pda.app.operation.data.model.MaterialModel
import com.panda.pda.app.operation.data.model.ProductModel
import com.panda.pda.app.operation.data.model.TaskBandedMaterialModel
import com.panda.pda.app.task.data.model.TaskModel
import timber.log.Timber

/**
 * created by AnJiwei 2021/8/31
 */
class MaterialBindingProductFragment : BaseFragment(R.layout.fragment_material_binding_product) {

    private lateinit var taskModel: TaskModel
    private lateinit var productModel: ProductModel
    private lateinit var bindingAdapter: CommonRecycleViewAdapter<ItemBindMaterialCodeBinding, MaterialModel>
    private val viewBinding by viewBinding<FragmentMaterialBindingProductBinding>()

    private val materialViewModel by activityViewModels<MaterialViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener(it) }
        materialViewModel.scannedProductData.observe(viewLifecycleOwner, {
            viewBinding.tvProductSerialCode.text =
                getString(R.string.product_bar_code_formatter, it.also { productModel = it }.code)
        })
        materialViewModel.selectedTaskData.observe(viewLifecycleOwner, {
            taskModel = it
        })

        viewBinding.etSearchBar.setOnEditorActionListener { editText, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onScanMaterialCode(editText.text.toString())
            } else if (event.keyCode == KeyEvent.KEYCODE_ENTER) {
                onScanMaterialCode(editText.text.toString())
                (editText as EditText).selectAll()
            }
            false
        }
        viewBinding.rvBindList.adapter =
            object : CommonRecycleViewAdapter<ItemBindMaterialCodeBinding, MaterialModel>(
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
                            if (data.materialSerialCode.isNullOrEmpty()) SpannableStringBuilder()
                                .color(requireContext().getColor(R.color.textHeaderLineColor)) {
                                    append("-")
                                } else data.materialSerialCode
                        tvMaterialDesc.text = data.combineInfoStr()
                    }
                    Timber.e("banded item: ${data.materialName}, ${data.materialSerialCode}")
                }
            }.also { bindingAdapter = it }

        materialViewModel.taskBandedMaterialData.observe(viewLifecycleOwner, {
            updateBindMaterial(it)
        })
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
            tvBindCount.text = SpannableStringBuilder()
                .append("已绑定 ")
                .color(requireContext().getColor(R.color.textHighLightColor)) { append(data.totalBindCount.toString()) }
                .append("/${data.totalToBindCount}个")
        }
        bindingAdapter.refreshData(formatBandedData(data))
    }

    private fun formatBandedData(data: TaskBandedMaterialModel): List<MaterialModel> {
        val result = mutableListOf<MaterialModel>()
        data.toBindList.forEach { model ->
            val bandedCount =
                data.bindList.map { bandedModel -> if (bandedModel.materialCode == model.materialCode) 1 else 0 }
                    .sum()
            val toBindCount = model.materialNum - bandedCount
            if (toBindCount > 0) {
                result.addAll(List(model.materialNum) { model.deepCopy() })
            }
        }
        result.addAll(data.bindList)
        return result
    }
}