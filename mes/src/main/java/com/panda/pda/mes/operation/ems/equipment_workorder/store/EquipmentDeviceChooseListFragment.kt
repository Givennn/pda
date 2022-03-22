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
import com.panda.pda.mes.base.extension.putObjectString
import com.panda.pda.mes.base.extension.toast
import com.panda.pda.mes.base.retrofit.DataListNode
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.databinding.FrameEmptyViewBinding
import com.panda.pda.library.android.controls.EndlessRecyclerViewScrollListener
import com.panda.pda.mes.databinding.ItemEquipmentChooseSingleBinding
import com.panda.pda.mes.operation.ems.EquipmentCommonChooseSearchListFragment
import com.panda.pda.mes.operation.ems.data.EquipmentApi
import com.panda.pda.mes.operation.ems.data.model.EquipmentInfoDeviceModel
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit

/**
 * 出库时设备选择列表
 */
class EquipmentDeviceChooseListFragment :
    EquipmentCommonChooseSearchListFragment<EquipmentInfoDeviceModel>() {
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    override val searchBarHintResId: Int?
        get() = R.string.equipment_info_device_search_hint

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = viewBinding.rvTaskList.layoutManager as? LinearLayoutManager ?: return
        scrollListener = object :
            EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                WebClient.request(EquipmentApi::class.java)
                    .pdaEmsDeviceListByPageGet(1,
                        "1",
                        "",
                        viewBinding.etSearchBar.text?.toString(),
                        page,
                        10)
                    .bindToFragment()
                    .subscribe({
                        itemListAdapter.addData(it.dataList)
                    }, { })
            }
        }
        viewBinding.rvTaskList.addOnScrollListener(scrollListener)
    }

    override fun createAdapter(): CommonViewBindingAdapter<*, EquipmentInfoDeviceModel> {
        //适配器中的列表，已选也包含未选
        return object :
            CommonViewBindingAdapter<ItemEquipmentChooseSingleBinding, EquipmentInfoDeviceModel>(
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
                data: EquipmentInfoDeviceModel,
                position: Int,
            ) {
                holder.itemViewBinding.cbNgReason.text = "${data.facilityDesc}-${data.facilityModel}"
                holder.itemViewBinding.cbNgReason.isChecked = data.isChecked
//                holder.itemViewBinding.cbNgReason.setOnCheckedChangeListener { _, isChecked ->
//                    Timber.e("当前点击：$position")
//
//                }
                holder.itemViewBinding.cbNgReason.clicks()
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .bindToLifecycle(holder.itemView)
                    .subscribe {
                        //单选
                        confirm(data)
//                        val isChecked = data.isChecked
//                        dataList.forEach { item -> item.isChecked = false }
//                        dataList[position].isChecked = !isChecked
//                        notifyDataSetChanged()
//                        dataList.forEach { item ->
//                            if (item.isChecked) {
//                                toast("选择了${item.facilityDesc}")
//                            }
//                        }
//                        val reasons = dataList.filter { it.isChecked }
//                        if (reasons.isEmpty()) {
//                            toast(getString(R.string.ng_reason_empty_message))
//                        } else {
//                            confirm(data)
//                        }
                    }
            }
        }
    }

    private fun confirm(reasons: EquipmentInfoDeviceModel) {
//        setFragmentResult(EquipmentChoosePersonFragment.REQUEST_KEY, Bundle().apply {
//            val clazz = EquipmentSupplierChooseModel::class.java
//            val jsonAdapter = Moshi.Builder().build().adapter(clazz)
//            this.putString(EquipmentChoosePersonFragment.NG_REASON_ARG_KEY ?: clazz.simpleName,
//                jsonAdapter.toJson(reasons))
//        })
        setFragmentResult(REQUEST_KEY,
            Bundle().apply {
                putObjectString(reasons,
                    NG_REASON_ARG_KEY)
            })
        navBackListener.invoke(requireView())
    }

    override fun api(key: String?): Single<DataListNode<EquipmentInfoDeviceModel>> {
        return WebClient.request(EquipmentApi::class.java)
            .pdaEmsDeviceListByPageGet(1, "1", "", key)
            .doFinally { scrollListener.resetState() }
    }

    //厂商列表
    override val titleResId: Int
        get() = R.string.equipment_title_device

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

    override val showBottomBtm: Boolean
        get() = false

    companion object {
        const val REQUEST_KEY = "CHOOSEDEVICE_REASON_REQUEST"
        const val NG_REASON_ARG_KEY = "CHOOSEDEVICE_REASON_ARGS"
    }
}