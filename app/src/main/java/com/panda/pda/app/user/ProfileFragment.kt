package com.panda.pda.app.user

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.databinding.FragmentProfileBinding
import com.panda.pda.app.user.data.UserApi
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import java.util.concurrent.TimeUnit

/**
 * created by AnJiwei 2021/8/16
 */
class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    private val viewBinding by viewBinding<FragmentProfileBinding>()
    private val userViewModel by activityViewModels<UserViewModel>()
    private val userInfoModel by lazy { userViewModel.loginData.value?.userInfo }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.apply {
            topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }
            tvName.text = userInfoModel?.userName
            tvMyPhone.text = userInfoModel?.phoneNumber
            tvDepartment.text = userInfoModel?.getDepartment()
            tvVersion.text = userViewModel.getAppVersionName(requireContext())
            llChangePwd.clicks()
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .bindToLifecycle(view)
                .subscribe { changePwd() }
            btnLogout.clicks()
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .bindToLifecycle(view)
                .subscribe { logout() }
        }
    }

    private fun logout() {
        WebClient.request(UserApi::class.java)
            .pdaAdminUserLogoutPost()
            .bindToFragment()
            .onErrorComplete()
            .subscribe {
                navBackListener.invoke(requireView())
                userViewModel.logout(null)
            }
    }

    private fun changePwd() {
        navController.navigate(R.id.action_profileFragment_to_changePwdOldVerifyFragment)
    }
}