package com.panda.pda.mes.operation.qms.ng_product_deall

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.StringRes
import by.kirich1409.viewbindingdelegate.viewBinding
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.ConfirmDialogFragment
import com.panda.pda.mes.base.extension.getStringObject
import com.panda.pda.mes.base.extension.putObjectString
import com.panda.pda.mes.base.extension.toast
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.common.data.model.IdRequest
import com.panda.pda.mes.databinding.FragmentNgProductListBinding
import com.panda.pda.mes.databinding.ItemNgProductBinding
import com.panda.pda.mes.operation.bps.data.MainPlanApi
import com.panda.pda.mes.operation.bps.data.model.MainPlanFinishRequest
import com.panda.pda.mes.operation.qms.data.QualityApi
import com.panda.pda.mes.operation.qms.data.model.NGProductItemModel
import com.panda.pda.mes.operation.qms.data.model.QualityTaskModel
import com.trello.rxlifecycle4.kotlin.bindToLifecycle

/**
 * created by AnJiwei 2022/11/17
 */
class NGProductListFragment : BaseFragment(R.layout.fragment_ng_product_list) {

    private lateinit var qualityTask: QualityTaskModel

    private val viewBinding by viewBinding<FragmentNgProductListBinding>()

    private val itemListAdapter by lazy { createAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val task = arguments?.getStringObject<QualityTaskModel>()

        if (task == null) {
            toast(R.string.net_work_error)
            navBackListener.invoke(requireView())
        } else {
            qualityTask = task
        }
        viewBinding.commonLayout
            .apply {
                rvTaskList.adapter = itemListAdapter
                swipe.setOnRefreshListener { refreshData() }
                topAppBar.setTitle(titleResId)
                topAppBar.setNavigationOnClickListener { navBackListener(it) }
                etSearchBar.setHint(searchBarHintResId)
                etSearchBar.setOnEditorActionListener { editText, actionId, event ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        onSearching()
                    } else if (event.keyCode == KeyEvent.KEYCODE_ENTER) {
                        onSearching()
                        (editText as EditText).selectAll()
                    }
                    false
                }
            }

        viewBinding.floatingActionButton.clicks()
            .bindToLifecycle(requireView())
            .subscribe {
                addNgProduct()
            }

    }

    private fun addNgProduct() {
        navController.navigate(R.id.action_NGProductListFragment_to_NGProductEditFragment,
            Bundle().apply {
                putObjectString(qualityTask)
            })
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    fun createAdapter(): CommonViewBindingAdapter<*, NGProductItemModel> {
        return object : CommonViewBindingAdapter<ItemNgProductBinding, NGProductItemModel>() {
            override fun createBinding(parent: ViewGroup): ItemNgProductBinding {
                return ItemNgProductBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            }

            override fun onBindViewHolderWithData(
                holder: ViewBindingHolder,
                data: NGProductItemModel,
                position: Int,
            ) {

                holder.itemViewBinding.apply {
                    tvNgProcedure.text =
                        data.procedureBadList.joinToString(",", "不良工序：") { it.procedureName }
                    tvReturnProcedure.text =
                        data.procedureReturnList.joinToString(",", "返工工序：") { it.procedureName }
                    tvProductBarCode.text = data.productBarCode
                    setupDealType(tvDealMethod, data.dealType)
                    tvDealNumber.text = data.productCount
                    tvDealStatus.text = data.status
                    tvDealTime.text = data.dealTime
                    tvActualFinishTime.text = data.realFinishTime
                    btnActionEdit.setOnClickListener {
                        editNgProduct(data)
                    }
//                    btnActionSubmit.setOnClickListener {
//                        submitNgProduct(data)
//                    }
                    btnActionDelete.setOnClickListener {
                        deleteNgProduct(data)
                    }
                }
            }

        }
    }

    private fun deleteNgProduct(data: NGProductItemModel) {

        val dialog = ConfirmDialogFragment().setTitle(getString(R.string.ng_product_delete_hint))
            .setConfirmButton({ _, _ ->
                WebClient.request(QualityApi::class.java)
                    .deleteNgProductPost(IdRequest(data.id))
                    .bindToFragment()
                    .subscribe({
                        toast(R.string.ng_product_delte_finish_success_toast)
                        refreshData()
                    },
                        { })
            })
        dialog.show(parentFragmentManager, TAG)
    }

    private fun submitNgProduct(data: NGProductItemModel) {

    }

    private fun editNgProduct(data: NGProductItemModel) {
        navController.navigate(R.id.action_NGProductListFragment_to_NGProductEditFragment,
            Bundle().apply {
                putObjectString(qualityTask)
                putObjectString(data)
            })

    }

    @get:StringRes
    val titleResId = R.string.ng_product_list

    @get:StringRes
    val searchBarHintResId: Int = R.string.ng_product_search_hint


    private fun onSearching() {
        refreshData()
    }

    private fun refreshData() {
        WebClient.request(QualityApi::class.java)
            .pdaQmsNgProductListGet(qualityTask.id)
            .bindToFragment()
            .subscribe({
                itemListAdapter.refreshData(it.dataList)
                viewBinding.commonLayout.swipe.isRefreshing = false
            }, {})
    }

    private fun setupDealType(textView: TextView, type: Int) {
        val typeDrawer = when (type) {
            1 -> NgDealType.StepQualified
            2 -> NgDealType.ReWork
            3 -> NgDealType.Scrap
            4 -> NgDealType.Undetermined
            else -> null
        }

        if (typeDrawer != null) {
            textView.text = typeDrawer.type
            textView.setBackgroundResource(typeDrawer.bgResource)
            textView.setTextColor(typeDrawer.textColor)
        }
    }
}

enum class NgDealType(val bgResource: Int, val type: String, val textColor: Int) {
    StepQualified(R.drawable.bg_item_tag_green, "让步合格", R.color.tag_green),
    ReWork(R.drawable.bg_item_tag_orange, "返工", R.color.tag_orange),
    Scrap(R.drawable.bg_item_tag_red, "报废", R.color.tag_red),
    Undetermined(R.drawable.bg_item_tag_blue, "待定", R.color.tag_blue)
}