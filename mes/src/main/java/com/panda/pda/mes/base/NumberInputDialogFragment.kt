package com.panda.pda.mes.base

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.mes.R
import com.panda.pda.mes.base.extension.toast
import com.panda.pda.mes.databinding.DialogConfirmBinding
import com.panda.pda.mes.databinding.DialogNumberInputBinding
import timber.log.Timber

/**
 * created by AnJiwei 2022/11/7
 */
class NumberInputDialogFragment: DialogFragment(R.layout.dialog_number_input) {

    private var cancelTextResId: Int = R.string.cancel
    private var confirmTextResId: Int = R.string.confirm

    private var confirmAction: ((Int)-> Unit)? = null

    private var cancelAction: DialogInterface.OnClickListener? = null

    private var title = ""

    private val viewBinding by viewBinding<DialogNumberInputBinding>()

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
                val number = binding.etInputNumber.text.toString().toIntOrNull()
                if (number != null) {
                    confirmAction?.invoke(number)
                    dismiss()
                } else {
                    toast("请输入送检数量")
                }
            }
            binding.dialogMsg.text = title
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutParams = requireDialog().window?.attributes
        if (layoutParams != null) {
            layoutParams.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
            requireDialog().window?.attributes = layoutParams
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun setConfirmButton(
        listener: (Int)-> Unit,
        @StringRes resId: Int = R.string.confirm,
    ): NumberInputDialogFragment {
        confirmTextResId = resId
        confirmAction = listener
        return this
    }

    fun setCancelButton(
        listener: DialogInterface.OnClickListener,
        @StringRes resId: Int = R.string.cancel,
    ): NumberInputDialogFragment {
        cancelTextResId = resId
        cancelAction = listener
        return this
    }

    fun setTitle(message: String): NumberInputDialogFragment {
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