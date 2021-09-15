package com.panda.pda.library.android.controls

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes

/**
 * created by AnJiwei 2020/10/28
 */
class LoadingDialog(context: Context,
                    @LayoutRes private val layoutRes: Int,
                    @IdRes private val messageTextRes: Int)
    : Dialog(context) {

    private lateinit var tvMessage: TextView
    private var content = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
        tvMessage = findViewById(messageTextRes)
    }

    fun show(message: String) {
        content = message
        show()
    }

    fun changeMessage(message: String) {
        tvMessage.text = message
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        tvMessage.text = content
    }
}