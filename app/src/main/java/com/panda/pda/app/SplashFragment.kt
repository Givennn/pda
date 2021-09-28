package com.panda.pda.app

import android.content.Context
import androidx.fragment.app.activityViewModels
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.base.extension.toast
import com.panda.pda.app.base.retrofit.*
import com.panda.pda.app.base.retrofit.onMainThread
import com.panda.pda.app.common.CommonViewModel
import com.panda.pda.app.common.data.CommonApi
import com.panda.pda.app.common.data.CommonParameters
import com.panda.pda.app.user.UserViewModel
import com.panda.pda.app.user.UserViewModel.Companion.SP_PASSWORD
import com.panda.pda.app.user.UserViewModel.Companion.SP_USER_NAME
import com.panda.pda.app.user.data.UserApi
import com.panda.pda.app.user.data.model.LoginRequest
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * created by AnJiwei 2021/8/9
 */
class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    private val viewModel by activityViewModels<UserViewModel>()

    private val commonViewModel by activityViewModels<CommonViewModel>()
    override fun onResume() {
        super.onResume()
        requestLocalUserInfo()
    }

    private fun requestLocalUserInfo() {
        val loginRequest = getCacheLoginInfo()
        if (loginRequest == null) {
            WebClient.request(CommonApi::class.java).getAuthorityTree()
                .onErrorReturn { DataListNode(listOf()) } //TODO mock test
                .delay(1, TimeUnit.SECONDS)
                .onMainThread()
                .catchError()
                .bindToLifecycle(requireView())
                .subscribe({
                    commonViewModel.authorityViewModel.postValue(it.dataList)
                    navController.navigate(R.id.action_splashFragment_to_loginFragment)
                    queryCommonParameters()
                }, {})
        } else {

            Single.zip(
                WebClient.request(CommonApi::class.java).getAuthorityTree()
                    .onErrorReturn { DataListNode(listOf()) }, //TODO mock test
                WebClient.request(UserApi::class.java)
                    .userNameLoginPost(loginRequest)
            ) { auto, loginInfo ->
                Pair(auto.dataList, loginInfo)
            }.delay(1, TimeUnit.SECONDS)
                .onMainThread()
                .catchError()
                .bindToLifecycle(requireView())
                .subscribe({
                    commonViewModel.authorityViewModel.postValue(it.first)
                    viewModel.updateLoginData(it.second, loginRequest)
                    navController.navigate(R.id.action_splashFragment_to_taskFragment)
                    queryCommonParameters()
                }, { })
        }
    }

    private fun queryCommonParameters() {
        return //TODO mock test
        WebClient.request(CommonApi::class.java)
            .pdaConfigSysParamListByParamGet()
            .onMainThread()
            .catchError()
            .subscribe({
                CommonParameters.pushParameters(it)
            }, { toast(it.message ?: "无法获取数据字典") })
    }

    private fun getCacheLoginInfo(): LoginRequest? {
        val viewModelSp =
            requireContext().getSharedPreferences(UserViewModel.SP_NAME, Context.MODE_PRIVATE)
        val userName = viewModelSp.getString(SP_USER_NAME, null) ?: return null
        val pwd = viewModelSp.getString(SP_PASSWORD, null) ?: return null
        return LoginRequest(userName, pwd)
    }


}