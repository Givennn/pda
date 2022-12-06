package com.panda.pda.mes.common

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.panda.pda.mes.R
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.databinding.DialogMultiSelectBinding
import com.panda.pda.mes.databinding.ItemResourceBinding
import com.panda.pda.mes.operation.fms.data.model.ResourceModel

/**
 * created by AnJiwei 2022/11/30
 */
class MultiSelectBottomDialogFragment<TSource: Any>: BottomSheetDialogFragment() {

    private var confirmAction: ((List<TSource>) -> Unit)? = null

    private var cancelAction: DialogInterface.OnClickListener? = null

    private val viewBinding by viewBinding<DialogMultiSelectBinding>()

    var pickerData = listOf<TSource>()

    val selectedItem = mutableListOf<TSource>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_multi_select, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isCancelable = false
        viewBinding.let { binding ->
            binding.btnCancel.setOnClickListener {
                cancelAction?.onClick(
                    requireDialog(),
                    it.id
                )
                dismiss()
            }
            binding.btnConfirm.setOnClickListener {
                confirmAction?.invoke(selectedItem)
                dismiss()
            }
        }

        val selectAdapter = object : CommonViewBindingAdapter<ItemResourceBinding, TSource>(pickerData.toMutableList()) {
            override fun createBinding(parent: ViewGroup): ItemResourceBinding {
                return ItemResourceBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            }

            override fun onBindViewHolderWithData(
                holder: ViewBindingHolder,
                data: TSource,
                position: Int,
            ) {
                if (selectedItem.contains(data)) {
                    holder.itemViewBinding.cbResourceItem.isChecked = true
                }
                holder.itemViewBinding.cbResourceItem.text = data.toString()
                holder.itemViewBinding.cbResourceItem.setOnCheckedChangeListener { _, isChecked ->
                    onItemChecked(data, isChecked)
                }
            }

            override fun onViewRecycled(holder: ViewBindingHolder) {
                super.onViewRecycled(holder)
                holder.itemViewBinding.cbResourceItem.setOnCheckedChangeListener(null)
                holder.itemViewBinding.cbResourceItem.isChecked = false
            }
        }

        viewBinding.rvSelectList.adapter = selectAdapter
    }

    private fun onItemChecked(data: TSource, isChecked: Boolean) {
        if (isChecked) {
            if (!selectedItem.contains(data)) {
                selectedItem.add(data)
            }
        } else {
            if (selectedItem.contains(data)) {
                selectedItem.remove(data)
            }
        }
    }


    fun setConfirmButton(
        listener: (List<TSource>) -> Unit,
    ): MultiSelectBottomDialogFragment<TSource> {
        confirmAction = listener
        return this
    }

    fun setCancelButton(
        listener: DialogInterface.OnClickListener,
    ): MultiSelectBottomDialogFragment<TSource> {
        cancelAction = listener
        return this
    }
}