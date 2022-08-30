package com.panda.pda.mes.operation.fms.mission

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.ConfirmDialogFragment
import com.panda.pda.mes.base.extension.getStringObject
import com.panda.pda.mes.base.extension.toast
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.common.data.model.IdRequest
import com.panda.pda.mes.databinding.*
import com.panda.pda.mes.operation.fms.data.TaskApi
import com.panda.pda.mes.operation.fms.data.model.ProducePrepareItem
import com.panda.pda.mes.operation.fms.data.model.DispatchOrderModel

/**
 * created by AnJiwei 2022/6/21
 */
public class ProducePrepareFragment : BaseFragment(R.layout.fragment_produce_prepare) {

    private val prepareAdapter by lazy { createAdapter() }
    private val viewBinding by viewBinding<FragmentProducePrepareBinding>()

    private lateinit var taskModel: DispatchOrderModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener(it) }
        val task = arguments?.getStringObject<DispatchOrderModel>()
        if (task != null) {
            taskModel = task
        } else {
            toast("派工单数据异常")
            navBackListener.invoke(requireView())
        }

        viewBinding.tvDispatchOrderNo.text =
            getString(R.string.dispatch_order_code_formatter, taskModel.dispatchOrderCode)
        viewBinding.tvDispatchOrderDesc.text =
            getString(R.string.dispatch_order_desc_formatter, taskModel.dispatchOrderDesc)

        viewBinding.rvPrepareList.adapter = prepareAdapter
        refreshData()

    }

    fun createAdapter(): CommonViewBindingAdapter<*, ProducePrepareItem> {
        return object :
            CommonViewBindingAdapter<ItemProductPrepareBinding, ProducePrepareItem>(mutableListOf()) {
            override fun createBinding(parent: ViewGroup): ItemProductPrepareBinding {
                return ItemProductPrepareBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            }

//            override fun createEmptyViewBinding(parent: ViewGroup): ViewBinding {
//                return FrameEmptyViewBinding.inflate(
//                    LayoutInflater.from(parent.context),
//                    parent,
//                    false
//                )
//            }

            override fun onBindViewHolderWithData(
                holder: ViewBindingHolder,
                data: ProducePrepareItem,
                position: Int,
            ) {
                holder.itemViewBinding.apply {
                    tvProducePrepareName.text = data.name
                    tvProducePrepareDesc.text = data.detail
                    if (data.finishTime.isNullOrEmpty()) {
                        btnActionFinish.isEnabled = true
                    } else {
                        btnActionFinish.isEnabled = false
                        btnActionFinish.text =
                            getString(R.string.prepare_finish_time_formatter, data.finishTime)
                    }
                    btnActionFinish.setOnClickListener {
                        finishItem(data)
                    }
                }
            }
        }
    }

    private fun finishItem(data: ProducePrepareItem) {
        val dialog = ConfirmDialogFragment().setTitle(getString(R.string.produce_prepare_confirm))
            .setConfirmButton({ _, _ ->
                WebClient.request(TaskApi::class.java)
                    .finishPrepareItem(IdRequest(data.id))
                    .bindToFragment()
                    .subscribe({
                        toast(R.string.produce_prepare_success)
                        refreshData()
                    },
                        { })
            })
        dialog.show(parentFragmentManager, TAG)


    }

    private fun refreshData() {
       WebClient.request(TaskApi::class.java)
           .prepareListGetByDispatchOrderId(taskModel.id)
           .bindToFragment()
           .subscribe({
               prepareAdapter.refreshData(it.dataList)
           }, {})
    }
}