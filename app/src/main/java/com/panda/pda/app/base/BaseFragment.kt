package com.panda.pda.app.base

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.panda.pda.app.R
import com.panda.pda.app.base.extension.toast
import com.panda.pda.app.base.retrofit.BaseResponse
import com.panda.pda.app.base.retrofit.HttpInnerException
import com.panda.pda.app.base.retrofit.onMainThread
import com.panda.pda.app.base.retrofit.unWrapperData
import com.panda.pda.app.user.LogoutReasonCode
import com.panda.pda.app.user.UserViewModel
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import io.reactivex.rxjava3.core.*
import timber.log.Timber

/**
 * created by AnJiwei 2020/10/12
 */
abstract class BaseFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {

    private var loadingDialog: DialogFragment? = null

    //    private var permissionEmit: CompletableEmitter? = null
//    private var openFileEmit: SingleEmitter<Uri>? = null
//    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
//    private lateinit var requestOpenFileLauncher: ActivityResultLauncher<Array<String>>
    private val userViewModel by activityViewModels<UserViewModel>()

    protected val TAG get() = this.javaClass.simpleName
    protected val sp: SharedPreferences by lazy {
        this.requireContext().getSharedPreferences("private", Context.MODE_PRIVATE)
    }

    protected val navController by lazy { findNavController() }

    //todo handle back pressed twice
    protected val navBackListener = { _: View -> navController.popBackStack() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.v("onCreate ${javaClass.simpleName}")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        requestPermissionLauncher = registerForActivityResult(
//            ActivityResultContracts.RequestPermission()
//        ) { isGranted: Boolean ->
//            if (isGranted) {
//                permissionEmit?.onComplete()
//            } else {
//                permissionEmit?.onError(Exception("请允许"))
//            }
//        }
//
//        requestOpenFileLauncher =
//            registerForActivityResult(
//                ActivityResultContracts.OpenDocument()
//            ) { uri ->
//                if (uri == null) {
//                    openFileEmit?.onError(NullPointerException())
//                } else {
//                    openFileEmit?.onSuccess(uri)
//                }
//            }
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.v("onDestroy ${javaClass.simpleName}")
    }

//    protected fun permissionRequest(permission: String): Completable {
//        return Completable.create { emit ->
//            if (ContextCompat.checkSelfPermission(requireContext(),
//                    permission) == PackageManager.PERMISSION_GRANTED
//            ) {
//                emit.onComplete()
//            } else {
//                permissionEmit = emit
//                requestPermissionLauncher.launch(permission)
//            }
//        }
//    }
//
//    protected fun openFileRequest(fileTypes: Array<String>): Single<Uri> {
//        return permissionRequest(android.Manifest.permission.READ_EXTERNAL_STORAGE)
//            .andThen(Single.create { emit ->
//                openFileEmit = emit
//                requestOpenFileLauncher.launch(fileTypes)
//            })
//    }

    protected fun <T> Single<T>.bindLoadingStatus(loadMessage: String = ""): Single<T> {
        return this
            .bindToLifecycle(this@BaseFragment.requireView())
            .doOnSubscribe { showLoadingDialog(loadMessage) }
            .doOnError {this@BaseFragment.onNetworkError(it)  }
            .doFinally { dismissLoadingDialog() }

    }

    protected fun Completable.bindLoadingStatus(loadMessage: String = ""): Completable {
        return this
            .bindToLifecycle(this@BaseFragment.requireView())
            .doOnSubscribe { showLoadingDialog(loadMessage) }
            .doOnError { this@BaseFragment.onNetworkError(it) }
            .doFinally { dismissLoadingDialog() }
    }

    protected fun <T> Single<BaseResponse<T>>.bindToFragment(loadMessage: String = ""): Single<T> {
        return this
            .onMainThread()
            .unWrapperData()
            .bindLoadingStatus(loadMessage)
    }

    protected fun Single<BaseResponse<Any>>.bindToFragment(loadMessage: String = ""): Completable {
        return this
            .onMainThread()
            .unWrapperData()
            .bindLoadingStatus(loadMessage)
    }

    private fun onNetworkError(throwable: Throwable) {
        if (throwable is HttpInnerException) {
            when(throwable.code) {
                in LogoutReasonCode.values().map { it.code } ->
                    userViewModel.logout(LogoutReasonCode.findReason(throwable.code)!!)
                in UserViewModel.IgnoreToastCodeList -> return
            }
        }
        toast(throwable.message ?: getString(R.string.net_work_error))
    }

    private fun dismissLoadingDialog() {
//        loadingDialog?.dismiss()
//        loadingDialog = null
    }

    private fun showLoadingDialog(loadMessage: String) {
//        DialogFragment(R.layout.dialog_loading)
//            .also { loadingDialog = it
//            it.requireDialog().findViewById<TextView>(R.id.tv_load_message).setText(loadMessage)}
//            .show(parentFragmentManager, TAG)
    }

}