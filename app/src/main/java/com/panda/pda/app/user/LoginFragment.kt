package com.panda.pda.app.user

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.databinding.FragmentLoginBinding

/**
 * created by AnJiwei 2021/8/9
 */
class LoginFragment: BaseFragment(R.layout.fragment_login) {

    private val binding: FragmentLoginBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvVersion.text = "v1.0.11"
        binding.btLogin.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_taskFragment)
        }
    }
}