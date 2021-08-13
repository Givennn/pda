package com.panda.pda.app

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.databinding.FragmentMainBinding

/**
 * created by AnJiwei 2021/8/9
 */
class MainFragment: BaseFragment(R.layout.fragment_main) {
    override fun <T : ViewBinding> bindingView(view: View): T {
        return FragmentMainBinding.bind(view)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBottomNavigation()
        initNavManager()
    }

    private fun initNavManager() {
        binding.nvBottom.setupWithNavController(binding.fcvNavHostMain.findNavController())
    }

    private fun initBottomNavigation() {
    }
}