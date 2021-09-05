package com.panda.pda.app.operation.material

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseRecycleViewAdapter
import com.panda.pda.app.base.retrofit.BaseResponse
import com.panda.pda.app.base.retrofit.DataListNode
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.common.CommonSearchListFragment
import com.panda.pda.app.databinding.ItemMaterialReplaceBinding
import com.panda.pda.app.operation.data.MaterialApi
import com.panda.pda.app.operation.data.model.MaterialModel
import io.reactivex.rxjava3.core.Single

/**
 * created by AnJiwei 2021/8/30
 */
class MaterialReplaceFragment : CommonSearchListFragment<MaterialModel>() {
    private val materialViewModel by activityViewModels<MaterialViewModel>()
    private val productModel by lazy { materialViewModel.scannedProductData.value!! }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.etSearchBar.visibility = View.GONE
    }

    override fun createAdapter(): BaseRecycleViewAdapter<*, MaterialModel> {
        return object : BaseRecycleViewAdapter<ItemMaterialReplaceBinding, MaterialModel>(
            mutableListOf()) {
            override fun createBinding(parent: ViewGroup): ItemMaterialReplaceBinding {
                return ItemMaterialReplaceBinding.inflate(LayoutInflater.from(parent.context),
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
                    replaceMaterial(data)
                }
            }

        }
    }

    private fun replaceMaterial(data: MaterialModel) {
        materialViewModel.materialData.postValue(data)
        navController.navigate(R.id.action_materialReplaceFragment_to_scanToReplaceFragment)
    }

    override fun api(key: String?): Single<BaseResponse<DataListNode<MaterialModel>>> =
        WebClient.request(MaterialApi::class.java)
            .materialTaskQueryBindByProductGet(-1, productModel.code) //todo find task id
            .map { BaseResponse(it.message, DataListNode(it.data?.bindList ?: listOf()), it.code) }

    override val titleResId: Int
        get() = R.string.material_replace
}