package com.panda.pda.mes.user

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.core.widget.doOnTextChanged
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.extension.toast
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.databinding.FragmentChangePwdOldVerifyBinding
import com.panda.pda.mes.user.data.UserApi
import com.panda.pda.mes.user.data.model.PwdCheckRequest
import com.panda.pda.library.android.utils.AESUtils

/**
 * create by AnJiwei 2021/9/4
 */
class ChangePwdOldVerifyFragment : BaseFragment(R.layout.fragment_change_pwd_old_verify) {
    private val viewBinding by viewBinding<FragmentChangePwdOldVerifyBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.apply {
            topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }
            btnStepNext.setOnClickListener {
                verifyPassword()
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
    }

    private fun verifyPassword() {
        val oldPwd = viewBinding.etPassword.text.toString()
        if (oldPwd.isEmpty()) {
            toast("请输入旧密码")
            return
        }
        WebClient.request(UserApi::class.java)
            .pdaAdminUserPasswordCheckPost(PwdCheckRequest(AESUtils.encrypt(oldPwd)))
            .bindToFragment()
            .subscribe({ navToNewPwdFragment(oldPwd) }, { })
    }

    private fun navToNewPwdFragment(oldPwd: String) {
        navController.navigate(R.id.action_changePwdOldVerifyFragment_to_changePwdNewVerifyFragment, Bundle().apply {
            putString(OLD_PWD, oldPwd)
        })
    }

    companion object {
        const val OLD_PWD = "old_password"
    }
}