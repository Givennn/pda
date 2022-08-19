package com.panda.pda.mes.user

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.BaseRootFragment
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.databinding.FragmentProfileBinding
import com.panda.pda.mes.user.data.UserApi
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import java.util.concurrent.TimeUnit

/**
 * created by AnJiwei 2021/8/16
 */
class ProfileFragment : BaseRootFragment(R.layout.fragment_profile) {

    private val viewBinding by viewBinding<FragmentProfileBinding>()

    private val userViewModel by activityViewModels<UserViewModel>()

    private val userInfoModel by lazy { userViewModel.loginData.value?.userInfo }

    override val isStatusBarLight: Boolean
        get() = true

    override fun getTopToolBar(): MaterialToolbar {
        return viewBinding.topAppBar
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.apply {
//            val badgeDrawable = BadgeDrawable.create(requireContext()).apply {
//                isVisible = true
//                number = 11
//            }
//            BadgeUtils.attachBadgeDrawable(badgeDrawable, topAppBar, R.id.message)
            tvName.text = userInfoModel?.userName
            tvMyPhone.text = userInfoModel?.phoneNumber
            tvDepartment.text = userInfoModel?.getDepartment()
            tvVersion.text = userViewModel.getAppVersionName(requireContext())
            llSetting.clicks()
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .bindToLifecycle(view)
                .subscribe { navController.navigate(R.id.profile_setting_nav_graph) }
//            llChangePwd.clicks()
//                .throttleFirst(500, TimeUnit.MILLISECONDS)
//                .bindToLifecycle(view)
//                .subscribe { changePwd() }
//            btnLogout.clicks()
//                .throttleFirst(500, TimeUnit.MILLISECONDS)
//                .bindToLifecycle(view)
//                .subscribe { logout() }
        }
    }

//    private fun logout() {
//        WebClient.request(UserApi::class.java)
//            .pdaAdminUserLogoutPost()
//            .bindToFragment()
//            .onErrorComplete()
//            .subscribe {
//                navBackListener.invoke(requireView())
//                userViewModel.logout(null)
//            }
//    }
//
//    private fun changePwd() {
//        navController.navigate(R.id.action_profileFragment_to_changePwdOldVerifyFragment)
//    }
}