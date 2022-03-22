package com.panda.pda.mes.operation.ems.equipment_workorder.maintenance

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.panda.pda.library.android.controls.EndlessRecyclerViewScrollListener
import com.panda.pda.mes.R
import com.panda.pda.mes.base.ConfirmDialogFragment
import com.panda.pda.mes.base.extension.toast
import com.panda.pda.mes.base.retrofit.DataListNode
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.databinding.FrameEmptyViewBinding
import com.panda.pda.mes.databinding.ItemChoosePersonBinding
import com.panda.pda.mes.operation.ems.EquipmentCommonChooseSearchListFragment
import com.panda.pda.mes.operation.ems.data.EquipmentApi
import com.panda.pda.mes.operation.ems.data.model.EquipmentPersonChooseModel
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.rxjava3.core.Single

/**
 * 待分配人员选择列表
 */
class EquipmentPersonChooseListFragment :
    EquipmentCommonChooseSearchListFragment<EquipmentPersonChooseModel>() {

    private val bindingAdapter = getNgReasonAdapter()
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    override val searchBarHintResId: Int?
        get() = R.string.equipment_info_person_search_hint
    var teamId: String = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        teamId = arguments?.getString(EquipmentWorkOrderWaitFenpeiFragment.TEAMID).toString()
        val layoutManager = viewBinding.rvTaskList.layoutManager as? LinearLayoutManager ?: return
        scrollListener = object :
            EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                WebClient.request(EquipmentApi::class.java)
                    .pdaEmsPersonChooseListGet(teamId,
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

    override fun createAdapter(): CommonViewBindingAdapter<*, EquipmentPersonChooseModel> {
        //适配器中的列表，已选也包含未选
        //这段是测试代码
//        if (dataList.isEmpty()) {
//            for (index in 0..30) {
//                var item =
//                    EquipmentPersonChooseModel(index,
//                        "name$index",
//                        "rolename$index",
//                        "部门$index",
//                        "workshopNames$index",
//                        false)
//                dataList.add(item)
//            }
//        }
        return object :
            CommonViewBindingAdapter<ItemChoosePersonBinding, EquipmentPersonChooseModel>(
                dataList) {
            override fun createBinding(parent: ViewGroup): ItemChoosePersonBinding {
                return ItemChoosePersonBinding.inflate(
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

            @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
            override fun onBindViewHolderWithData(
                holder: ViewBindingHolder,
                data: EquipmentPersonChooseModel,
                position: Int,
            ) {
                holder.itemViewBinding.apply {
//                    cbPerson.text = data.realName
                    cbPerson.text = "${data.userName}-${data.orgName}"
                    cbPerson.isSelected = data.isChecked
//                    cbPerson.setOnCheckedChangeListener { _, isChecked ->
//                        data.isChecked = isChecked
//                    }
                    rootView.setOnClickListener {
                        val isChecked = data.isChecked
                        cbPerson.isSelected = !isChecked
                        data.isChecked = !isChecked
                    }
                }
            }
        }
    }

    private fun confirm(reasons: List<EquipmentPersonChooseModel>) {
        setFragmentResult(REQUEST_KEY, Bundle().apply {
            putString(NG_REASON_ARG_KEY,
                bindingAdapter.toJson(reasons))

        })
        navBackListener.invoke(requireView())
    }

    override fun api(key: String?): Single<DataListNode<EquipmentPersonChooseModel>> {
        return WebClient.request(EquipmentApi::class.java)
            .pdaEmsPersonChooseListGet(teamId, key)
            .doFinally { scrollListener.resetState() }
    }

    //产品列表
    override val titleResId: Int
        get() = R.string.equipment_title_person_choose
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
        const val REQUEST_KEY = "CHOOSEPERSON_REASON_REQUEST"
        const val NG_REASON_ARG_KEY = "CHOOSEPERSON_REASON_ARGS"
        fun getNgReasonAdapter(): JsonAdapter<List<EquipmentPersonChooseModel>> {
            return Moshi.Builder().build().adapter(
                Types.newParameterizedType(
                    List::class.java,
                    EquipmentPersonChooseModel::class.java
                )
            )
        }
    }

    override val showBottomBtm: Boolean
        get() = true
}