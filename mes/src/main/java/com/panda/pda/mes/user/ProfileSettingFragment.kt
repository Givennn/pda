package com.panda.pda.mes.user

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.databinding.FragmentProfileSettingBinding
import com.panda.pda.mes.user.data.UserApi
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import java.util.concurrent.TimeUnit

/**
 * created by AnJiwei 2022/8/19
 */
class ProfileSettingFragment : BaseFragment(R.layout.fragment_profile_setting) {

    private val viewBinding by viewBinding<FragmentProfileSettingBinding>()

    private val userViewModel by activityViewModels<UserViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.apply {
            topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }
            llChangePassword.clicks()
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
        navController.navigate(R.id.action_profileSettingFragment_to_changePwdOldVerifyFragment)
    }
}