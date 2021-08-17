package com.panda.pda.app

import android.os.Bundle
import android.view.View
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.databinding.FragmentSplashBinding

/**
 * created by AnJiwei 2021/8/9
 */
class SplashFragment: BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestLocalUserInfo()
    }

    private fun requestLocalUserInfo() {
        TODO("Not yet implemented")
    }
}