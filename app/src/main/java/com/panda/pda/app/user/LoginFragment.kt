package com.panda.pda.app.user

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.databinding.DialogLoadingBinding
import com.panda.pda.app.databinding.FragmentLoginBinding
import com.panda.pda.app.user.data.UserApi
import com.panda.pda.app.user.data.model.LoginRequest
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber

/**
 * created by AnJiwei 2021/8/9
 */
class LoginFragment : BaseFragment(R.layout.fragment_login) {

    private val binding: FragmentLoginBinding by viewBinding()
    private val viewModel by activityViewModels<UserViewModel>()
    val loadingBinding by lazy { DialogLoadingBinding.inflate(layoutInflater) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvVersion.text = viewModel.getAppVersionName(requireContext())
        binding.btLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val request =
            LoginRequest(binding.etAccount.text.toString(), binding.etPassword.text.toString())
        WebClient.request(UserApi::class.java)
            .userNameLoginPost(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ _ -> navToMain() }, { e -> Timber.e(e) })
    }

    private fun navToMain() {
        findNavController().navigate(R.id.action_loginFragment_to_taskFragment)

    }

    private fun showloading() {
//        val builder = MaterialAlertDialogBuilder(requireContext()).setView(loadingBinding.root)
//        builder.show()
    }
}