package com.panda.pda.app.operation

import android.view.LayoutInflater
import android.view.ViewGroup
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.base.BaseRecycleViewAdapter
import com.panda.pda.app.base.retrofit.BaseResponse
import com.panda.pda.app.base.retrofit.DataListNode
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.common.CommonSearchListFragment
import com.panda.pda.app.databinding.ItemGuideInfoBinding
import com.panda.pda.app.operation.data.GuideApi
import com.panda.pda.app.operation.data.model.GuideInfoModel
import io.reactivex.rxjava3.core.Single

/**
 * created by AnJiwei 2021/8/30
 */
class OperateGuideFragment : CommonSearchListFragment<GuideInfoModel>() {

    override fun createAdapter(): BaseRecycleViewAdapter<*, GuideInfoModel> {
        return object :
            BaseRecycleViewAdapter<ItemGuideInfoBinding, GuideInfoModel>(mutableListOf()) {
            override fun createBinding(parent: ViewGroup): ItemGuideInfoBinding {
                return ItemGuideInfoBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            }

            override fun onBindViewHolderWithData(
                holder: ViewBindingHolder,
                data: GuideInfoModel,
                position: Int,
            ) {
                holder.itemViewBinding.apply {
                    tvProductCode.text = data.productCode
                    tvFileName.text = data.fileName
                    tvTechDesc.text = data.technicsDesc
                    tvProcedureDesc.text = data.procedureDesc
                }.clInfo.setOnClickListener {
                    onItemClicked(data)
                }
            }

        }
    }

    private fun onItemClicked(data: GuideInfoModel) {
        TODO("Not yet implemented")
    }

    override fun api(key: String?): Single<BaseResponse<DataListNode<GuideInfoModel>>> =
        WebClient.request(GuideApi::class.java)
            .pdaFmsWorkGuideListGet(key)
            .map { listModel ->
                BaseResponse(listModel.message,
                    DataListNode(listModel.data?.dataList ?: listOf()),
                    listModel.code)
            }

    override val titleResId: Int
        get() = R.string.operate_guide
}