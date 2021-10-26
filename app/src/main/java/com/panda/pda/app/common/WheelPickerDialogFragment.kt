package com.panda.pda.app.common

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.aigestudio.wheelpicker.WheelPicker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.panda.pda.app.R
import com.panda.pda.app.base.ConfirmDialogFragment
import com.panda.pda.app.databinding.DialogWheelPickerSelectBinding

/**
 * created by AnJiwei 2021/10/26
 */
class WheelPickerDialogFragment : BottomSheetDialogFragment() {

    private var confirmAction: DialogInterface.OnClickListener? = null

    private var cancelAction: DialogInterface.OnClickListener? = null

    private lateinit var viewBinding: DialogWheelPickerSelectBinding

    var pickerData = listOf("未定义")

    var selectedItem = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = DialogWheelPickerSelectBinding.inflate(inflater, container, false)
        .also {
            viewBinding = it
        }.root

    //    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.let { binding ->
            binding.btnCancel.setOnClickListener {
                cancelAction?.onClick(
                    requireDialog(),
                    it.id
                )
                dismiss()
            }
            binding.btnConfirm.setOnClickListener {
                confirmAction?.onClick(requireDialog(), it.id)
                dismiss()
            }
        }

        setupWhealViewStyle(viewBinding.wheelPicker)
        viewBinding.wheelPicker.setOnItemSelectedListener { _, _, position ->
            selectedItem = position
        }
    }


    fun setConfirmButton(
        listener: DialogInterface.OnClickListener,
    ): WheelPickerDialogFragment {
        confirmAction = listener
        return this
    }

    fun setCancelButton(
        listener: DialogInterface.OnClickListener,
    ): WheelPickerDialogFragment {
        cancelAction = listener
        return this
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        viewBinding.wheelPicker.data = pickerData
        return super.onCreateDialog(savedInstanceState)
    }

    private fun setupWhealViewStyle(wheelView: WheelPicker) {
        wheelView.isCyclic = false
        wheelView.visibleItemCount = 5
        wheelView.setSameWidth(true)
        wheelView.itemTextSize = 32
        wheelView.itemSpace = 55
        wheelView.setIndicator(true)
        wheelView.indicatorColor = Color.parseColor("#FFEFEFEF")
        wheelView.itemTextColor = Color.parseColor("#FF8D8D8D")
        wheelView.selectedItemTextColor = Color.parseColor("#333333")
        wheelView.itemAlign = WheelPicker.ALIGN_CENTER
        wheelView.indicatorSize = 2
    }
}