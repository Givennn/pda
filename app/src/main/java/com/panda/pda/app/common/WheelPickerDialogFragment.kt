package com.panda.pda.app.common

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aigestudio.wheelpicker.WheelPicker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.panda.pda.app.R
import com.panda.pda.app.databinding.DialogWheelPickerSelectBinding

/**
 * created by AnJiwei 2021/10/26
 */
class WheelPickerDialogFragment : BottomSheetDialogFragment() {

    private var confirmAction: ((Pair<String, Int>?) -> Unit)? = null

    private var cancelAction: DialogInterface.OnClickListener? = null

    private val viewBinding by viewBinding<DialogWheelPickerSelectBinding>()

    var pickerData = listOf<String>()

    private var selectedItem: Pair<String, Int>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_wheel_picker_select, container)
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

        setupWhealViewStyle(viewBinding.wheelPicker)
        viewBinding.wheelPicker.setOnItemSelectedListener { _, data, position ->
            selectedItem = Pair(data.toString(), position)
        }
        viewBinding.wheelPicker.data = pickerData
        if (pickerData.isNotEmpty()) {
            selectedItem = Pair(pickerData.first(), 0)
        }
    }


    fun setConfirmButton(
        listener: (Pair<String, Int>?) -> Unit,
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