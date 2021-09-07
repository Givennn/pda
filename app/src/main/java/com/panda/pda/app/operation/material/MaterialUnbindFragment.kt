package com.panda.pda.app.operation.material

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseRecycleViewAdapter
import com.panda.pda.app.base.ConfirmDialogFragment
import com.panda.pda.app.base.extension.toast
import com.panda.pda.app.base.retrofit.BaseResponse
import com.panda.pda.app.base.retrofit.DataListNode
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.common.CommonSearchListFragment
import com.panda.pda.app.databinding.ItemMaterialUnbindBinding
import com.panda.pda.app.operation.data.MaterialApi
import com.panda.pda.app.operation.data.model.MaterialModel
import com.panda.pda.app.operation.data.model.MaterialUnbindRequest
import io.reactivex.rxjava3.core.Single

/**
 * created by AnJiwei 2021/8/30
 */
class MaterialUnbindFragment :
    CommonSearchListFragment<MaterialModel>() {
    private val materialViewModel by activityViewModels<MaterialViewModel>()
    private val productModel by lazy { materialViewModel.scannedProductData.value!! }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.etSearchBar.visibility = View.GONE
    }

    override fun createAdapter(): BaseRecycleViewAdapter<*, MaterialModel> {
        return object : BaseRecycleViewAdapter<ItemMaterialUnbindBinding, MaterialModel>(
            mutableListOf()) {
            override fun createBinding(parent: ViewGroup): ItemMaterialUnbindBinding {
                return ItemMaterialUnbindBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            }

            override fun onBindViewHolderWithData(
                holder: ViewBindingHolder,
                data: MaterialModel,
                position: Int,
            ) {
                holder.itemViewBinding.apply {
                    tvMaterialCode.text = data.materialCode
                    tvMaterialDesc.text = data.materialName
                }.btnAction.setOnClickListener {
                    unbindMaterial(data)
                }
            }

        }
    }

    private fun unbindMaterial(data: MaterialModel) {
        ConfirmDialogFragment().setTitle(getString(R.string.task_receive_confirm))
            .setConfirmButton({ _, _ ->
                WebClient.request(MaterialApi::class.java)
                    .materialUnbindPost(MaterialUnbindRequest(productModel.code, data.materialCode))
                    .bindToFragment()
                    .subscribe({
                        toast(getString(R.string.material_unbind_success_message))
                        navBackListener.invoke(requireView())
                    }, { })
            }).show(parentFragmentManager, TAG)
    }

    override fun api(key: String?): Single<BaseResponse<DataListNode<MaterialModel>>> =
        WebClient.request(MaterialApi::class.java)
            .materialTaskQueryBindByProductGet(productModel.code)
            .map { BaseResponse(it.message, DataListNode(it.data?.bindList ?: listOf()), it.code) }

    override val titleResId: Int
        get() = R.string.material_unbind
}