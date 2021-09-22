package com.panda.pda.app.base

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.panda.pda.app.R
import com.panda.pda.app.base.extension.toast
import com.panda.pda.app.base.retrofit.*
import com.panda.pda.app.common.CommonViewModel
import com.panda.pda.app.user.UserViewModel
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import io.reactivex.rxjava3.core.*
import timber.log.Timber

/**
 * created by AnJiwei 2020/10/12
 */
abstract class BaseFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {

    private var loadingDialog: DialogFragment? = null

    private val userViewModel by activityViewModels<UserViewModel>()
//    private val commonViewModel by activityViewModels<CommonViewModel>()

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

    override fun onDestroy() {
        super.onDestroy()
        Timber.v("onDestroy ${javaClass.simpleName}")
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        userViewModel.loginData.observe(viewLifecycleOwner, {
//            setupUserAuth(it.menus)
//        })
//    }

//    private fun setupUserAuth(authorities: List<String>) {
//        val rootView = requireView()
//        val authorModel = commonViewModel.authorityViewModel.value ?: return
//        val tag = rootView.tag ?: return
//        val rootAuthor = authorModel.firstOrNull { it.id.toString() == tag } ?: return
//        rootAuthor.children.forEach {
//            val item = requireView().findViewWithTag<View>(it.id.toString()) ?: return@forEach
//            if (authorities.contains(it.id.toString())) {
//                item.visibility = View.VISIBLE
//
//            } else {
//                item.visibility = View.GONE
//            }
//        }
//    }

    protected fun <T> Single<T>.bindLoadingStatus(loadMessage: String = ""): Single<T> {
        val dialogFragment = DialogFragment(R.layout.dialog_loading).apply {
            isCancelable = false
            loadingDialog = this
        }
        return this
            .doOnSubscribe { dialogFragment.show(parentFragmentManager, TAG) }
            .doFinally { dialogFragment.dismiss() }

    }

    protected fun <T> Observable<T>.bindLoadingStatus(): Observable<T> {
        val dialogFragment = DialogFragment(R.layout.dialog_loading).apply {
            isCancelable = false
            loadingDialog = this
        }
        return this
            .doOnSubscribe { dialogFragment.show(parentFragmentManager, TAG) }
            .doFinally { dialogFragment.dismiss() }
    }

    protected fun Completable.bindLoadingStatus(loadMessage: String = ""): Completable {
        val dialogFragment = DialogFragment(R.layout.dialog_loading).apply {
            isCancelable = false
            loadingDialog = this
        }
        return this
            .doOnSubscribe { dialogFragment.show(parentFragmentManager, TAG) }
            .doFinally { dialogFragment.dismiss() }

    }

    protected fun <T> Single<T>.catchError(): Single<T> {
        return this
            .doOnError { this@BaseFragment.onNetworkError(it) }
    }

    protected fun Completable.catchError(): Completable {
        return this
            .doOnError { this@BaseFragment.onNetworkError(it) }
    }

    protected fun <T> Single<BaseResponse<T>>.bindToFragment(loadMessage: String = ""): Single<T> {
        return this
            .onMainThread()
            .unWrapperData()
            .bindToLifecycle(this@BaseFragment.requireView())
            .catchError()
            .bindLoadingStatus(loadMessage)
    }

    protected fun Single<BaseResponse<Any>>.bindToFragment(loadMessage: String = ""): Completable {
        return this
            .onMainThread()
            .unWrapperData()
            .bindToLifecycle(this@BaseFragment.requireView())
            .catchError()
            .bindLoadingStatus(loadMessage)
    }

    private fun onNetworkError(throwable: Throwable) {
        if (throwable is HttpInnerException) {
            when (throwable.code) {
                in UserViewModel.LogoutCodeList ->
                    userViewModel.logout(throwable.code)
                in UserViewModel.IgnoreToastCodeList -> return
            }
        }
        toast(throwable.message ?: getString(R.string.net_work_error))
    }

    protected fun dismissLoadingDialog() {
        loadingDialog?.dismiss()
    }

    protected fun showLoadingDialog() {
        DialogFragment(R.layout.dialog_loading).apply {
            isCancelable = false
            loadingDialog = this
        }.show(parentFragmentManager, TAG)
    }


}