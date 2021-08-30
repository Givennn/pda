package com.panda.pda.app.base

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.app.R
import com.panda.pda.app.databinding.DialogConfirmBinding

/**
 * created by AnJiwei 2021/8/27
 */
class ConfirmDialogFragment : DialogFragment(R.layout.dialog_confirm) {

    private var cancelTextResId: Int = R.string.cancel
    private var confirmTextResId: Int = R.string.confirm

    private var confirmAction: DialogInterface.OnClickListener? = null

    private var cancelAction: DialogInterface.OnClickListener? = null

    private var title = ""

    private val viewBinding by viewBinding<DialogConfirmBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.let { binding ->
            binding.cancelBt.setText(cancelTextResId)
            binding.cancelBt.setOnClickListener {
                cancelAction?.onClick(requireDialog(),
                    it.id)
                dismiss()
            }
            binding.okBt.setText(confirmTextResId)
            binding.okBt.setOnClickListener {
                confirmAction?.onClick(requireDialog(), it.id)
                dismiss()
            }
            binding.dialogMsg.text = title
        }
    }

    fun setConfirmButton(
        listener: DialogInterface.OnClickListener,
        @StringRes resId: Int = R.string.confirm,
    ): ConfirmDialogFragment {
        confirmTextResId = resId
        confirmAction = listener
        return this
    }

    fun setCancelButton(
        listener: DialogInterface.OnClickListener,
        @StringRes resId: Int = R.string.cancel,
    ): ConfirmDialogFragment {
        cancelTextResId = resId
        cancelAction = listener
        return this
    }

    fun setTitle(message: String): ConfirmDialogFragment {
        title = message
        return this
    }
}