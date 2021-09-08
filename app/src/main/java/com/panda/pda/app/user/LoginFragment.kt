package com.panda.pda.app.user

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.databinding.FragmentLoginBinding
import com.panda.pda.app.user.data.UserApi
import com.panda.pda.app.user.data.model.LoginRequest
import com.panda.pda.library.android.AESUtils
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import java.util.concurrent.TimeUnit

/**
 * created by AnJiwei 2021/8/9
 */
class LoginFragment : BaseFragment(R.layout.fragment_login) {

    private val viewBinding: FragmentLoginBinding by viewBinding()

    private val viewModel by activityViewModels<UserViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.tvVersion.text = viewModel.getAppVersionName(requireContext())
        viewBinding.ivAccountDelete.setOnClickListener {
            viewBinding.etAccount.setText("")
        }
        viewBinding.ivPwdDelete.setOnClickListener {
            viewBinding.etPassword.setText("")
        }
        viewBinding.ivPwdVisible.setOnCheckedChangeListener { _, visiable ->
            viewBinding.etPassword.transformationMethod =
                if (visiable) HideReturnsTransformationMethod.getInstance() else PasswordTransformationMethod.getInstance()
            viewBinding.etPassword.setSelection(viewBinding.etPassword.text.length)
        }

        viewBinding.etPassword.doOnTextChanged { text, _, _, _ ->
            val visible = if (text?.isEmpty() != false) View.INVISIBLE else View.VISIBLE
            viewBinding.ivPwdDelete.visibility = visible
            viewBinding.ivPwdVisible.visibility = visible
        }

        viewBinding.etAccount.doOnTextChanged { text, _, _, _ ->
            viewBinding.ivAccountDelete.visibility =
                if (text?.isEmpty() != false) View.INVISIBLE else View.VISIBLE
        }
        viewBinding.btLogin.clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .bindToLifecycle(requireView())
            .subscribe {
                login()
            }
    }

    private fun login() {
        if (viewBinding.etAccount.text.isEmpty()) {
            setErrorMsg(getString(R.string.input_user_name_alert))
            return
        }
        if (viewBinding.etPassword.text.isEmpty()) {
            setErrorMsg(getString(R.string.input_password_alert))
            return
        }
        val request =
            LoginRequest(viewBinding.etAccount.text.toString(),
                AESUtils.encrypt(viewBinding.etPassword.text.toString()))
        WebClient.request(UserApi::class.java)
            .userNameLoginPost(request)
            .bindToFragment()
            .subscribe({ data ->
                viewModel.updateLoginData(data, request)
                navToMain()
            }, {
                setErrorMsg(it.message ?: getString(R.string.user_pwd_error))
            })
    }

    private fun setErrorMsg(msg: String) {
        viewBinding.viewLineLogin.setBackgroundColor(requireContext().getColor(R.color.text_field_error))
        viewBinding.viewLinePassword.setBackgroundColor(requireContext().getColor(R.color.text_field_error))
        viewBinding.tvErrorMsg.apply {
            visibility = View.VISIBLE
            text = msg
        }
    }

    private fun navToMain() {
        findNavController().navigate(R.id.action_loginFragment_to_taskFragment)

    }
}