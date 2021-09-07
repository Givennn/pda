package com.panda.pda.app

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.base.extension.toast
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.base.retrofit.onMainThread
import com.panda.pda.app.base.retrofit.unWrapperData
import com.panda.pda.app.user.LogoutReasonCode
import com.panda.pda.app.user.UserViewModel
import com.panda.pda.app.user.UserViewModel.Companion.SP_PASSWORD
import com.panda.pda.app.user.UserViewModel.Companion.SP_USER_NAME
import com.panda.pda.app.user.data.UserApi
import com.panda.pda.app.user.data.model.LoginRequest
import com.panda.pda.library.android.AESUtils
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.BiFunction
import java.util.concurrent.TimeUnit

/**
 * created by AnJiwei 2021/8/9
 */
class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    private val viewModel by activityViewModels<UserViewModel>()

    override fun onResume() {
        super.onResume()
        requestLocalUserInfo()
    }

    private fun requestLocalUserInfo() {
        val loginRequest = getCacheLoginInfo()
        if (loginRequest == null) {
            Completable.timer(1, TimeUnit.SECONDS)
                .onMainThread()
                .bindLoadingStatus()
                .subscribe {
                    navController.navigate(R.id.action_splashFragment_to_loginFragment)
                }
        } else {
            WebClient.request(UserApi::class.java)
                .userNameLoginPost(loginRequest)
                .zipWith(Single.timer(1, TimeUnit.SECONDS), { data, _ -> data })
                .bindToFragment()
                .subscribe({
                    viewModel.updateLoginData(it, loginRequest)
                    navController.navigate(R.id.action_splashFragment_to_taskFragment)
                }, { })
        }
    }

    private fun getCacheLoginInfo(): LoginRequest? {
        val viewModelSp =
            requireContext().getSharedPreferences(UserViewModel.SP_NAME, Context.MODE_PRIVATE)
        val userName = viewModelSp.getString(SP_USER_NAME, null) ?: return null
        val pwd = viewModelSp.getString(SP_PASSWORD, null) ?: return null
        return LoginRequest(userName, pwd)
    }


}