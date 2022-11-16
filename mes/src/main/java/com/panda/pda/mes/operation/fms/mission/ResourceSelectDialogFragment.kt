package com.panda.pda.mes.operation.fms.mission

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.mes.R
import com.panda.pda.mes.base.extension.toast
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.base.retrofit.onMainThread
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.databinding.DialogResourceSelectBinding
import com.panda.pda.mes.databinding.ItemResourceBinding
import com.panda.pda.mes.operation.fms.data.TaskApi
import com.panda.pda.mes.operation.fms.data.model.ResourceModel
import com.panda.pda.mes.operation.fms.data.model.ResourceType
import com.trello.rxlifecycle4.kotlin.bindToLifecycle

/**
 * created by AnJiwei 2022/11/15
 */
class ResourceSelectDialogFragment(private val workCenterId: Int) :
    DialogFragment(R.layout.dialog_resource_select) {

    private lateinit var resourceAdapter: CommonViewBindingAdapter<ItemResourceBinding, ResourceModel>
    private val viewBinding by viewBinding<DialogResourceSelectBinding>()

    private var selectedType = ResourceType.Person

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.rgResourceTypeSelector.setOnCheckedChangeListener { _, checkedId ->
            selectedType = when (checkedId) {
                R.id.rb_person -> ResourceType.Person
                R.id.rb_group -> ResourceType.Group
                R.id.rb_eqp -> ResourceType.Equipment
                else -> ResourceType.Undefined
            }
            refreshResource()
        }
        viewBinding.tilSearchBarBtn.setOnClickListener {
            refreshResource()
        }

        viewBinding.ivCloseDialog.setOnClickListener {
            closeDialog()
        }

        viewBinding.btnConfirm.setOnClickListener {
            confirmDialog()
        }

        resourceAdapter = object : CommonViewBindingAdapter<ItemResourceBinding, ResourceModel>() {
            override fun createBinding(parent: ViewGroup): ItemResourceBinding {
                return ItemResourceBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            }

            override fun onBindViewHolderWithData(
                holder: ViewBindingHolder,
                data: ResourceModel,
                position: Int,
            ) {
                holder.itemViewBinding.cbResourceItem.text = data.resourceDisplayName
                holder.itemViewBinding.cbResourceItem.setOnCheckedChangeListener { _, isChecked ->
                    onItemChecked(data)
                }
            }

            override fun onViewRecycled(holder: ViewBindingHolder) {
                super.onViewRecycled(holder)
                holder.itemViewBinding.cbResourceItem.setOnCheckedChangeListener(null)
            }
        }


    }

    private fun onItemChecked(data: ResourceModel) {

    }

    private fun confirmDialog() {

    }

    private fun closeDialog() {

    }

    private fun refreshResource(page: Int = 0) {

        val searchKey = viewBinding.etSearchBar.text.toString()
        WebClient.request(TaskApi::class.java)
            .resourceListGetByWorkCenter(workCenterId, selectedType.code, searchKey, page)
            .onMainThread()
            .bindToLifecycle(requireView())
            .subscribe({
                resourceAdapter.refreshData(it.dataList)
            }, {
                toast(it.message ?: getString(R.string.net_work_error))
            })
    }
}