package com.panda.pda.mes.operation.ems.equipment_workorder.store

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.mes.R
import com.panda.pda.mes.base.ConfirmDialogFragment
import com.panda.pda.mes.base.extension.toast
import com.panda.pda.mes.base.retrofit.DataListNode
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.databinding.FrameEmptyViewBinding
import com.panda.pda.library.android.controls.EndlessRecyclerViewScrollListener
import com.panda.pda.mes.databinding.ItemEquipmentChooseSingleBinding
import com.panda.pda.mes.operation.ems.EquipmentCommonChooseSearchListFragment
import com.panda.pda.mes.operation.ems.data.EquipmentApi
import com.panda.pda.mes.operation.ems.data.model.EquipmentProductChooseModel
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit

/**
 * 入库确认产品选择列表
 */
class EquipmentProductChooseListFragment :
    EquipmentCommonChooseSearchListFragment<EquipmentProductChooseModel>() {

    private val bindingAdapter = getNgReasonAdapter()
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    override val searchBarHintResId: Int?
        get() = R.string.equipment_info_product_search_hint

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = viewBinding.rvTaskList.layoutManager as? LinearLayoutManager ?: return
        scrollListener = object :
            EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                WebClient.request(EquipmentApi::class.java)
                    .pdaEmsProductChooseListGet(
                        viewBinding.etSearchBar.text?.toString(), 10, page
                    )
                    .bindToFragment()
                    .subscribe({
                        dataList.addAll(it.dataList)
                        itemListAdapter.addData(it.dataList)
                    }, { })
            }
        }
        viewBinding.rvTaskList.addOnScrollListener(scrollListener)
        viewBinding.btnConfirm.setOnClickListener {
            val reasons = dataList.filter { it.isChecked }
            if (reasons.isEmpty()) {
                toast(getString(R.string.ng_reason_empty_message))
            } else {
                confirm(reasons)
            }
        }
    }

    override fun createAdapter(): CommonViewBindingAdapter<*, EquipmentProductChooseModel> {
        //适配器中的列表，已选也包含未选
        //这段是测试代码
        return object :
            CommonViewBindingAdapter<ItemEquipmentChooseSingleBinding, EquipmentProductChooseModel>(
                dataList) {
            override fun createBinding(parent: ViewGroup): ItemEquipmentChooseSingleBinding {
                return ItemEquipmentChooseSingleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            }

            override fun createEmptyViewBinding(parent: ViewGroup): ViewBinding {
                return FrameEmptyViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onBindViewHolderWithData(
                holder: ViewBindingHolder,
                data: EquipmentProductChooseModel,
                position: Int,
            ) {

                holder.itemViewBinding.cbNgReason.text = dataList[holder.bindingAdapterPosition].productName
                holder.itemViewBinding.cbNgReason.isChecked = dataList[holder.bindingAdapterPosition].isChecked
//                holder.itemViewBinding.cbNgReason.setOnCheckedChangeListener { _, isChecked ->
//                    Timber.e("当前点击：$position")
//
//                }
                holder.itemViewBinding.cbNgReason.clicks()
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .bindToLifecycle(holder.itemView)
                    .subscribe {
                        val isChecked = data.isChecked
                        dataList[holder.bindingAdapterPosition].isChecked = !isChecked
                        notifyDataSetChanged()
                    }
            }
        }
    }

    private fun confirm(reasons: List<EquipmentProductChooseModel>) {
        setFragmentResult(REQUEST_KEY, Bundle().apply {
            putString(NG_REASON_ARG_KEY, bindingAdapter.toJson(reasons))

        })
        navBackListener.invoke(requireView())
    }

    override fun api(key: String?): Single<DataListNode<EquipmentProductChooseModel>> {
        return WebClient.request(EquipmentApi::class.java)
            .pdaEmsProductChooseListGet(key)
            .doFinally { scrollListener.resetState() }
    }

    //产品列表
    override val titleResId: Int
        get() = R.string.equipment_title_product_choose

    protected fun showActionRequestDialog(
        request: Single<*>,
        dialogTitle: String,
        successMessage: String,
    ) {
        val dialog =
            ConfirmDialogFragment().setTitle(dialogTitle)
                .setConfirmButton({ _, _ ->
                    request.bindToFragment()
                        .subscribe({
                            toast(successMessage)
                            refreshData()
                        }, {})
                })
        dialog.show(parentFragmentManager, TAG)
    }

    companion object {
        const val REQUEST_KEY = "CHOOSEPRODUCT_REASON_REQUEST"
        const val NG_REASON_ARG_KEY = "CHOOSEPRODUCT_REASON_ARGS"
        fun getNgReasonAdapter(): JsonAdapter<List<EquipmentProductChooseModel>> {
            return Moshi.Builder().build().adapter(
                Types.newParameterizedType(
                    List::class.java,
                    EquipmentProductChooseModel::class.java
                )
            )
        }
    }

    override val showBottomBtm: Boolean
        get() = true
}