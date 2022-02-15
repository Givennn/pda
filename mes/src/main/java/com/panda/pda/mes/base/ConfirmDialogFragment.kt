package com.panda.pda.mes.base

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.mes.R
import com.panda.pda.mes.databinding.DialogConfirmBinding
import timber.log.Timber

/**
 * created by AnJiwei 2021/8/27
 */
val Fragment.activityDecorView: View get() = requireActivity().window.decorView

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
                cancelAction?.onClick(
                    requireDialog(),
                    it.id
                )
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).also {
            Timber.e("${it.window == null}")
            it.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        }
    }
}