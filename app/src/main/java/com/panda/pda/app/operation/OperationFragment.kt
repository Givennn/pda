package com.panda.pda.app.operation

import android.os.Bundle
import android.view.View
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.common.ModuleNavigationAdapter
import com.panda.pda.app.databinding.FragmentOperationBinding
import com.panda.pda.app.operation.material.MaterialViewModel
import com.panda.pda.app.operation.material.ProductScanFragment
import com.panda.pda.app.user.UserViewModel

/**
 * created by AnJiwei 2021/8/16
 */
class OperationFragment : BaseFragment(R.layout.fragment_operation) {

    private val viewBinding by viewBinding<FragmentOperationBinding>()

    private val materialViewModel by activityViewModels<MaterialViewModel>()

    private val userViewModel by activityViewModels<UserViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.rvFmsArea.layoutManager = GridLayoutManager(requireContext(), 4)
        viewBinding.rvFmsArea.adapter =
            ModuleNavigationAdapter(R.menu.fms_nav_menu, requireContext())
                .also {
                    it.navAction = { navId ->
                        when (navId) {
                            R.id.material_unbind_nav_graph ->
                                materialViewModel.materialActionData.postValue(ProductScanFragment.MaterialAction.Unbind)
                            R.id.material_replace_nav_graph ->
                                materialViewModel.materialActionData.postValue(ProductScanFragment.MaterialAction.Replace)
                        }
                        navController.navigate(navId)
                    }
                }
    }
}