package com.panda.pda.mes.user

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayout
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.databinding.FragmentLoginBinding
import com.panda.pda.mes.user.data.UserApi
import com.panda.pda.mes.user.data.model.LoginRequest
import com.panda.pda.library.android.utils.AESUtils
import com.panda.pda.mes.base.extension.toast
import com.squareup.moshi.Moshi
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * created by AnJiwei 2021/8/9
 */
class LoginFragment : BaseFragment(R.layout.fragment_login) {

    private val viewBinding: FragmentLoginBinding by viewBinding()

    private val viewModel by activityViewModels<UserViewModel>()

    private var loginMode: Int = 0

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
        viewBinding.tlLoginMode.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val index = tab?.position ?: return
                switchLoginMode(index)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        viewBinding.etScanBar.setOnEditorActionListener { editText, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onQrCodeLogin()
            } else if (event.keyCode == KeyEvent.KEYCODE_ENTER) {
                onQrCodeLogin()
                (editText as EditText).selectAll()
            }
            false
        }
    }

    private fun onQrCodeLogin() {
        val code = viewBinding.etScanBar.text.toString()
        Timber.e(code)
        try {
            val qrcodeJson = JSONObject(code)
            WebClient.request(UserApi::class.java)
                .qrCodeLoginPost(code)
                .bindToFragment()
                .subscribe({ data ->
                    viewModel.updateLoginData(data, null, code)
                    navToMain()
                }, {
                    setErrorMsg(it.message ?: getString(R.string.user_pwd_error))
                })
        } catch (err: JSONException) {
            toast("二维码信息异常")
        } catch (err: Throwable) {
            toast(err.message ?: "")
            err.printStackTrace()
        }
    }


    private fun switchLoginMode(index: Int) {
        loginMode = index
        when (index) {
            0 -> {
                viewBinding.llPasswordGroup.visibility = View.VISIBLE
                viewBinding.llQrcodeScanGroup.visibility = View.INVISIBLE
                viewBinding.etAccount.requestFocus()
            }
            1 -> {
                viewBinding.llPasswordGroup.visibility = View.INVISIBLE
                viewBinding.llQrcodeScanGroup.visibility = View.VISIBLE
                viewBinding.tvQrcodeScan.requestFocus()
            }
        }
    }

    private fun login() {
        if (loginMode == 1) {
            return
        }
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
                viewModel.updateLoginData(data, request, null)
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