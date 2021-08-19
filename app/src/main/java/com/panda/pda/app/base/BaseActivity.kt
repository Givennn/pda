package com.panda.pda.app.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.panda.pda.app.R
import com.panda.pda.app.base.extension.toast
import com.panda.pda.library.android.LoadingDialog
import timber.log.Timber

/**
 * created by AnJiwei 2020/10/10
 */
abstract class BaseActivity(@LayoutRes contentLayoutId: Int) : AppCompatActivity(contentLayoutId) {



    private val loadingDialog by lazy { buildLoadingDialog() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        Timber.v("onCreate ${javaClass.simpleName}")
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(currentFocus != null) {
            return (getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
                ?: return super.onTouchEvent(event)

        }
        return super.onTouchEvent(event)
    }

    private fun buildLoadingDialog(): LoadingDialog {
        val dialog =
            LoadingDialog(this, R.layout.dialog_loading, R.id.tv_loading_message)
        dialog.setCancelable(false)
        return dialog
    }

    private fun hideLoading() {
        if (!loadingDialog.isShowing)
            return
        loadingDialog.dismiss()
    }

    private fun showLoading() {
            loadingDialog.show()
    }

    protected fun<TViewModel> bindLoadingStatusAndErrorMessage(viewModel: TViewModel)
            where TViewModel : ILoadingViewModel {
        viewModel.isLoading.observe(this, Observer {
            if (it.first) showLoading()
            else hideLoading()
        })

        viewModel.errorMessage.observe(this, Observer {
            if (it.isNotEmpty()) {
                toast(it)
            }
        })
    }
}

