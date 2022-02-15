package com.panda.pda.mes

import android.content.Context
import androidx.fragment.app.activityViewModels
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.extension.toast
import com.panda.pda.mes.base.retrofit.*
import com.panda.pda.mes.base.retrofit.onMainThread
import com.panda.pda.mes.common.CommonViewModel
import com.panda.pda.mes.common.data.CommonApi
import com.panda.pda.mes.common.data.CommonParameters
import com.panda.pda.mes.user.UserViewModel
import com.panda.pda.mes.user.UserViewModel.Companion.SP_PASSWORD
import com.panda.pda.mes.user.UserViewModel.Companion.SP_USER_NAME
import com.panda.pda.mes.user.data.UserApi
import com.panda.pda.mes.user.data.model.LoginRequest
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit

/**
 * created by AnJiwei 2021/8/9
 */
class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    private val viewModel by activityViewModels<UserViewModel>()

    private val commonViewModel by activityViewModels<CommonViewModel>()

    override val isStatusBarLight: Boolean
        get() = true

    override fun onResume() {
        super.onResume()
        requestLocalUserInfo()
        test()
    }

    // todo test
    private fun test() {
//        val datePicker =
//            MaterialDatePicker.Builder.dateRangePicker().setTitleText(R.string.plan_data).build()
//
//        datePicker.show(parentFragmentManager, "tag")
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
                }, {
                    navController.navigate(R.id.action_splashFragment_to_loginFragment)
                })
        } else {
            Single.zip(
                WebClient.request(CommonApi::class.java).getAuthorityTree()
                    .onErrorReturn { DataListNode(listOf()) } //TODO mock test
                ,
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
                }, {
                    navController.navigate(R.id.action_splashFragment_to_loginFragment)
                })
        }
    }

    private fun queryCommonParameters() {
//        return //TODO mock test
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