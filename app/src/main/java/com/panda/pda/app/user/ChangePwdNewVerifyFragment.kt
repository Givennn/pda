package com.panda.pda.app.user

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import by.kirich1409.viewbindingdelegate.viewBinding
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.base.extension.toast
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.databinding.FragmentChangePwdNewVerifyBinding
import com.panda.pda.app.user.data.UserApi
import com.panda.pda.app.user.data.model.PwdModifyRequest
import com.panda.pda.library.android.AESUtils
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import java.util.concurrent.TimeUnit

/**
 * create by AnJiwei 2021/9/4
 */
class ChangePwdNewVerifyFragment : BaseFragment(R.layout.fragment_change_pwd_new_verify) {
    private val viewBinding by viewBinding<FragmentChangePwdNewVerifyBinding>()
    private val userViewModel by activityViewModels<UserViewModel>()

    private lateinit var oldPwd: String
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        oldPwd = requireArguments().getString(ChangePwdOldVerifyFragment.OLD_PWD, "")
        viewBinding.apply {
            topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }
            btnChangePwd.clicks()
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .bindToLifecycle(requireView())
                .subscribe {
                    changePwd()
                }
        }

        viewBinding.ivPwdDelete.setOnClickListener {
            viewBinding.etPassword.setText("")
        }
        viewBinding.ivPwdVisible.setOnCheckedChangeListener { _, visible ->
            viewBinding.etPassword.transformationMethod =
                if (visible) HideReturnsTransformationMethod.getInstance() else PasswordTransformationMethod.getInstance()
            viewBinding.etPassword.setSelection(viewBinding.etPassword.text.length)
        }
        viewBinding.etPassword.doOnTextChanged { text, _, _, _ ->
            val visible = if (text?.isEmpty() != false) View.INVISIBLE else View.VISIBLE
            viewBinding.ivPwdDelete.visibility = visible
            viewBinding.ivPwdVisible.visibility = visible
        }

        viewBinding.ivPwdConfirmDelete.setOnClickListener {
            viewBinding.etPasswordConfirm.setText("")
        }
        viewBinding.ivPwdConfirmVisible.setOnCheckedChangeListener { _, visible ->
            viewBinding.etPasswordConfirm.transformationMethod =
                if (visible) HideReturnsTransformationMethod.getInstance() else PasswordTransformationMethod.getInstance()
            viewBinding.etPasswordConfirm.setSelection(viewBinding.etPasswordConfirm.text.length)
        }
        viewBinding.etPasswordConfirm.doOnTextChanged { text, _, _, _ ->
            val visible = if (text?.isEmpty() != false) View.INVISIBLE else View.VISIBLE
            viewBinding.ivPwdConfirmDelete.visibility = visible
            viewBinding.ivPwdConfirmVisible.visibility = visible
        }
    }

    private fun changePwd() {
        val newPwd = viewBinding.etPassword.text.toString()
        val newPwdVerify = viewBinding.etPasswordConfirm.text.toString()
        when {
            newPwd.isEmpty() -> {
                toast("请输入新密码")
                return
            }
            newPwdVerify.isEmpty() -> {
                toast("请输入确认密码")
                return
            }
            newPwd != newPwdVerify -> {
                toast("新密码两次输入不一致，请重新输入")
                return
            }
        }

        WebClient.request(UserApi::class.java)
            .pdaAdminUserPasswordModifyPost(PwdModifyRequest(AESUtils.encrypt(oldPwd),
                AESUtils.encrypt(newPwd)))
            .bindToFragment()
            .subscribe({ showLoginFragment() }, {})
    }

    private fun showLoginFragment() {
        userViewModel.logout()
        navController.setGraph(R.navigation.shell_nav_graph)
        navController.navigate(R.id.loginFragment,
            null,
            NavOptions.Builder().setPopUpTo(R.id.shell_nav_graph, true).build())
        toast(R.string.pwd_changed_message)
    }
}