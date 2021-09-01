package com.panda.pda.app.discovery

import android.os.Bundle
import android.view.View
import androidx.core.view.children
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.databinding.FragmentDiscoveryBinding

/**
 * created by AnJiwei 2021/8/16
 */
class DiscoveryFragment: BaseFragment(R.layout.fragment_discovery) {
    private val viewBinding by viewBinding<FragmentDiscoveryBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.clDiscoveryArea.children.forEach {
            it.setOnClickListener { llBtn ->
                navController.navigate(llBtn.id)
            }
        }
    }
}