package com.panda.pda.mes.operation.ems.equipment_workorder.maintenance

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
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
//                    待完工单数：0，预计总工时：0小时
                    //待完工单数，默认是0
                    var totalNum = "0"
                    //预计总工时，默认是0
                    var totalTime = "0"
                    //用户的工单对象，保存有工时与工单数量
                    if (null != data.emsUserWorkOrderReport) {
                        //提取工单数量
                        if (!TextUtils.isEmpty(data.emsUserWorkOrderReport!!.totalWorkOrderNum)) {
                            totalNum = data.emsUserWorkOrderReport!!.totalWorkOrderNum
                        }
                        //提取总工时
                        if (!TextUtils.isEmpty(data.emsUserWorkOrderReport!!.totalExpectWorkTime)) {
                            totalTime = data.emsUserWorkOrderReport!!.totalExpectWorkTime
                        }
                        tvDetail.isVisible = true
                        tvDetail.text = "待完工单数：${totalNum}，预计总工时：${totalTime}小时"
                    } else {
                        tvDetail.isVisible = false
                    }
                    //展示人员姓名+部门
                    cbPerson.text = "${data.userName}-${data.orgName}"
                    cbPerson.isSelected = data.isChecked
                    rootView.setOnClickListener {
                        val isChecked = data.isChecked
                        cbPerson.isSelected = !isChecked
                        data.isChecked = !isChecked
                    }
                }
            }
        }
    }

    //确认选择人员列表，将人员带入上游页面
    private fun confirm(reasons: List<EquipmentPersonChooseModel>) {
        setFragmentResult(REQUEST_KEY, Bundle().apply {
            putString(NG_REASON_ARG_KEY,
                bindingAdapter.toJson(reasons))

        })
        navBackListener.invoke(requireView())
    }

    //设置进入页面的接口获取人员列表
    override fun api(key: String?): Single<DataListNode<EquipmentPersonChooseModel>> {
        return WebClient.request(EquipmentApi::class.java)
            .pdaEmsPersonChooseListGet(teamId, key)
            .doFinally { scrollListener.resetState() }
    }

    //产品列表
    override val titleResId: Int
        get() = R.string.equipment_title_person_choose

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

    //由于是多选，展示底部确认按钮
    override val showBottomBtm: Boolean
        get() = true
}