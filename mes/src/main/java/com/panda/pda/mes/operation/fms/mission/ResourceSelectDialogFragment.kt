package com.panda.pda.mes.operation.fms.mission

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.panda.pda.library.android.controls.EndlessRecyclerViewScrollListener
import com.panda.pda.mes.R
import com.panda.pda.mes.base.extension.toast
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.base.retrofit.onMainThread
import com.panda.pda.mes.common.WheelPickerDialogFragment
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.common.data.CommonApi
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
    BottomSheetDialogFragment() {

    private lateinit var resourceAdapter: CommonViewBindingAdapter<ItemResourceBinding, ResourceModel>
    private val viewBinding by viewBinding<DialogResourceSelectBinding>()

    private var selectedType = ResourceType.Person

    private var confirmAction: ((List<ResourceModel>) -> Unit)? = null

    private var cancelAction: DialogInterface.OnClickListener? = null

    private val selectedResource = mutableMapOf<Int, ResourceModel>()

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_resource_select, container)
    }

    override fun onResume() {
        super.onResume()
        refreshResource()
    }

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
                if (selectedResource.containsKey(data.id)) {
                    holder.itemViewBinding.cbResourceItem.isChecked = true
                }
                holder.itemViewBinding.cbResourceItem.text = data.resourceDisplayName
                holder.itemViewBinding.cbResourceItem.setOnCheckedChangeListener { _, isChecked ->
                    onItemChecked(data, isChecked)
                }
            }

            override fun onViewRecycled(holder: ViewBindingHolder) {
                if (holder.viewType == VIEW_TYPE_ITEM) {
                    holder.itemViewBinding.cbResourceItem.setOnCheckedChangeListener(null)
                    holder.itemViewBinding.cbResourceItem.isChecked = false
                }
                super.onViewRecycled(holder)
            }
        }

        viewBinding.rvResourceList.adapter = resourceAdapter

        val layoutManager = viewBinding.rvResourceList.layoutManager as? LinearLayoutManager ?: return

        scrollListener = object :
            EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                loadMoreResource(page)
            }
        }
        viewBinding.rvResourceList.addOnScrollListener(scrollListener)

    }

    private fun onItemChecked(data: ResourceModel, isChecked: Boolean) {

        if (isChecked) {
            if (!selectedResource.containsKey(data.id)) {
                selectedResource[data.id] = data
            }
        } else {
            if (selectedResource.containsKey(data.id)) {
                selectedResource.remove(data.id)
            }
        }

    }

    private fun confirmDialog() {

        confirmAction?.invoke(selectedResource.values.toMutableList())
        dismiss()
    }

    private fun closeDialog() {

        cancelAction?.onClick(requireDialog(), 0)
        dismiss()
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

    private fun loadMoreResource(page: Int) {
        val searchKey = viewBinding.etSearchBar.text.toString()
        WebClient.request(TaskApi::class.java)
            .resourceListGetByWorkCenter(workCenterId, selectedType.code, searchKey, page)
            .onMainThread()
            .bindToLifecycle(requireView())
            .subscribe({
                resourceAdapter.addData(it.dataList)
            }, {
                toast(it.message ?: getString(R.string.net_work_error))
            })
    }

    fun setConfirmButton(
        listener: (List<ResourceModel>) -> Unit,
    ): ResourceSelectDialogFragment {
        confirmAction = listener
        return this
    }

    fun setCancelButton(
        listener: DialogInterface.OnClickListener,
    ): ResourceSelectDialogFragment {
        cancelAction = listener
        return this
    }
}