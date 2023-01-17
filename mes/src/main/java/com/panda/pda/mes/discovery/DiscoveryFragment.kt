package com.panda.pda.mes.discovery

import android.os.Bundle
import android.view.View
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.appbar.MaterialToolbar
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.BaseRootFragment
import com.panda.pda.mes.databinding.FragmentDiscoveryBinding
import com.panda.pda.mes.user.UserViewModel

/**
 * created by AnJiwei 2021/8/16
 */
class DiscoveryFragment : BaseRootFragment(R.layout.fragment_discovery) {

    private val viewBinding by viewBinding<FragmentDiscoveryBinding>()
    private val userViewModel by activityViewModels<UserViewModel>()
    override val isStatusBarLight: Boolean
        get() = true

    override fun getTopToolBar(): MaterialToolbar {
        return viewBinding.topAppBar
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.clDiscoveryArea.children.forEach {
            it.setOnClickListener { llBtn ->
                navController.navigate(llBtn.id)
            }
        }
        userViewModel.topMessageCount.observe(viewLifecycleOwner) { topMessageCount ->
            topMessageCount?.let { setMessageCount(it) }
        }
    }
}