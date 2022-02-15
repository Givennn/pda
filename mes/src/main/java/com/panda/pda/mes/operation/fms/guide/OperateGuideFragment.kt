package com.panda.pda.mes.operation.fms.guide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.panda.pda.mes.R
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.base.retrofit.DataListNode
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.CommonSearchListFragment
import com.panda.pda.mes.databinding.FrameEmptyViewBinding
import com.panda.pda.mes.databinding.ItemGuideInfoBinding
import com.panda.pda.mes.operation.fms.data.GuideApi
import com.panda.pda.mes.operation.fms.data.model.GuideInfoModel
import com.panda.pda.library.android.controls.EndlessRecyclerViewScrollListener
import io.reactivex.rxjava3.core.Single

/**
 * created by AnJiwei 2021/8/30
 */
class OperateGuideFragment : CommonSearchListFragment<GuideInfoModel>() {

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    override val searchBarHintResId: Int?
        get() = R.string.operate_guide_search_hint

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = viewBinding.rvTaskList.layoutManager as? LinearLayoutManager ?: return
        scrollListener = object :
            EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                WebClient.request(GuideApi::class.java)
                    .pdaFmsWorkGuideListGet(viewBinding.etSearchBar.text?.toString(), page)
                    .bindToFragment()
                    .subscribe({
                        itemListAdapter.addData(it.dataList)
                    }, { })
            }
        }
        viewBinding.rvTaskList.addOnScrollListener(scrollListener)
    }

    override fun createAdapter(): CommonViewBindingAdapter<*, GuideInfoModel> {
        return object :
            CommonViewBindingAdapter<ItemGuideInfoBinding, GuideInfoModel>(mutableListOf()) {
            override fun createBinding(parent: ViewGroup): ItemGuideInfoBinding {
                return ItemGuideInfoBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            }

            override fun createEmptyViewBinding(parent: ViewGroup): ViewBinding {
                return FrameEmptyViewBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            }

            override fun onBindViewHolderWithData(
                holder: ViewBindingHolder,
                data: GuideInfoModel,
                position: Int,
            ) {
                holder.itemViewBinding.apply {
                    tvProductCode.text = getString(R.string.desc_and_code_formatter,
                        data.productName, data.productCode
                    )
                    tvFileName.text = data.fileName
                    tvTechDesc.text = getString(R.string.desc_and_code_formatter, data.technicsDesc,
                        data.technicsVersion
                    )
                    tvProcedureDesc.text = getString(R.string.desc_and_code_formatter,
                        data.procedureDesc,
                        data.procedureCode
                    )
                }.clInfo.setOnClickListener {
                    onItemClicked(data)
                }
            }

        }
    }

    private fun onItemClicked(data: GuideInfoModel) {
        navController.navigate(R.id.action_operateGuideFragment_to_guidePdfPreviewFragment,
            Bundle().apply { putSerializable(GuidePdfPreviewFragment.PDF_KEY, data) })
    }

    override fun api(key: String?): Single<DataListNode<GuideInfoModel>> =
        WebClient.request(GuideApi::class.java)
            .pdaFmsWorkGuideListGet(key)
            .map { listModel ->
                DataListNode(listModel.dataList)
            }.doFinally {
                scrollListener.resetState()
            }

    override val titleResId: Int
        get() = R.string.operate_guide


}
