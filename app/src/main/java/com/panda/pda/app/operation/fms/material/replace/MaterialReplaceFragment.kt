package com.panda.pda.app.operation.fms.material.replace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.panda.pda.app.R
import com.panda.pda.app.common.adapter.CommonViewBindingAdapter
import com.panda.pda.app.base.retrofit.DataListNode
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.common.CommonSearchListFragment
import com.panda.pda.app.databinding.ItemMaterialReplaceBinding
import com.panda.pda.app.operation.fms.data.MaterialApi
import com.panda.pda.app.operation.fms.data.model.MaterialModel
import com.panda.pda.app.operation.fms.material.MaterialViewModel
import io.reactivex.rxjava3.core.Single

/**
 * created by AnJiwei 2021/8/30
 */
class MaterialReplaceFragment : CommonSearchListFragment<MaterialModel>() {
    private val materialViewModel by activityViewModels<MaterialViewModel>()
    private val productModel by lazy { materialViewModel.scannedProductData.value!! }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.tilSearchBar.visibility = View.GONE
    }

    override fun createAdapter(): CommonViewBindingAdapter<*, MaterialModel> {
        return object : CommonViewBindingAdapter<ItemMaterialReplaceBinding, MaterialModel>(
            mutableListOf()
        ) {
            override fun createBinding(parent: ViewGroup): ItemMaterialReplaceBinding {
                return ItemMaterialReplaceBinding.inflate(
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
                    tvMaterialCode.text = data.materialSerialCode
                    tvMaterialDesc.text = data.combineInfoStr()
                }.btnAction.setOnClickListener {
                    replaceMaterial(data)
                }
            }

        }
    }

    private fun replaceMaterial(data: MaterialModel) {
        materialViewModel.materialData.postValue(data)
        navController.navigate(R.id.action_materialReplaceFragment_to_scanToReplaceFragment)
    }

    override fun api(key: String?): Single<DataListNode<MaterialModel>> =
        WebClient.request(MaterialApi::class.java)
            .materialTaskQueryBindByProductGet(productModel.code)
            .map {
                DataListNode(it.bindList)
            }

    override val titleResId: Int
        get() = R.string.material_replace
}