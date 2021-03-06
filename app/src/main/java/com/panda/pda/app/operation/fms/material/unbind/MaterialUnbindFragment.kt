package com.panda.pda.app.operation.fms.material.unbind

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.panda.pda.app.R
import com.panda.pda.app.common.adapter.CommonViewBindingAdapter
import com.panda.pda.app.base.ConfirmDialogFragment
import com.panda.pda.app.base.extension.toast
import com.panda.pda.app.base.retrofit.DataListNode
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.common.CommonSearchListFragment
import com.panda.pda.app.databinding.ItemMaterialUnbindBinding
import com.panda.pda.app.operation.fms.data.MaterialApi
import com.panda.pda.app.operation.fms.data.model.MaterialModel
import com.panda.pda.app.operation.fms.data.model.MaterialUnbindRequest
import com.panda.pda.app.operation.fms.material.MaterialViewModel
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
        viewBinding.tilSearchBar.visibility = View.GONE
    }

    override fun createAdapter(): CommonViewBindingAdapter<*, MaterialModel> {
        return object : CommonViewBindingAdapter<ItemMaterialUnbindBinding, MaterialModel>(
            mutableListOf()
        ) {
            override fun createBinding(parent: ViewGroup): ItemMaterialUnbindBinding {
                return ItemMaterialUnbindBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            }

            override fun onBindViewHolderWithData(
                holder: ViewBindingHolder,
                data: MaterialModel,
                position: Int,
            ) {
                holder.itemViewBinding.apply {
                    tvMaterialInfo.text = data.combineInfoStr()
                    tvMaterialSerialCode.text = data.materialSerialCode
                }.btnAction.setOnClickListener {
                    unbindMaterial(data)
                }
            }

        }
    }

    private fun unbindMaterial(data: MaterialModel) {
        ConfirmDialogFragment().setTitle(getString(R.string.task_unbind_confirm))
            .setConfirmButton({ _, _ ->
                WebClient.request(MaterialApi::class.java)
                    .materialUnbindPost(
                        MaterialUnbindRequest(
                            productModel.code,
                            data.materialSerialCode!!
                        )
                    )
                    .bindToFragment()
                    .subscribe({
                        toast(getString(R.string.material_unbind_success_message))
                        navBackListener.invoke(requireView())
                    }, { })
            }).show(parentFragmentManager, TAG)
    }

    override fun api(key: String?): Single<DataListNode<MaterialModel>> =
        WebClient.request(MaterialApi::class.java)
            .materialTaskQueryBindByProductGet(productModel.code)
            .map {
                DataListNode(it.bindList)
//                BaseResponse(it.message, DataListNode(it.data?.bindList ?: listOf()), it.code)
            }

    override val titleResId: Int
        get() = R.string.material_unbind
}