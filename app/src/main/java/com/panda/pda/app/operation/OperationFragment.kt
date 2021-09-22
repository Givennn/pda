package com.panda.pda.app.operation

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavAction
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.common.CommonViewModel
import com.panda.pda.app.common.adapter.HeaderAdapter
import com.panda.pda.app.common.adapter.ModuleNavigationAdapter
import com.panda.pda.app.databinding.FragmentOperationBinding
import com.panda.pda.app.databinding.ItemOperationHeaderBinding
import com.panda.pda.app.operation.material.MaterialViewModel
import com.panda.pda.app.operation.material.ProductScanFragment
import com.panda.pda.app.user.UserViewModel
import timber.log.Timber

/**
 * created by AnJiwei 2021/8/16
 */
class OperationFragment : BaseFragment(R.layout.fragment_operation) {

    private val viewBinding by viewBinding<FragmentOperationBinding>()

    private val materialViewModel by activityViewModels<MaterialViewModel>()

    private val userViewModel by activityViewModels<UserViewModel>()

    private val commonViewModel by activityViewModels<CommonViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupModuleGrid(
            R.menu.fms_nav_menu,
            R.string.factory_manage,
            viewBinding.rvFmsArea
        ) { navId ->
            when (navId) {
                R.id.material_unbind_nav_graph ->
                    materialViewModel.materialActionData.postValue(
                        ProductScanFragment.MaterialAction.Unbind
                    )
                R.id.material_replace_nav_graph ->
                    materialViewModel.materialActionData.postValue(
                        ProductScanFragment.MaterialAction.Replace
                    )
            }
            navController.navigate(navId)
        }
    }

    private fun setupModuleGrid(
        @MenuRes menuRes: Int,
        @StringRes headerRes: Int,
        recyclerView: RecyclerView,
        navAction: (navId: Int) -> Unit,
    ) {
        val layoutManager = GridLayoutManager(requireContext(), 4)
        recyclerView.layoutManager = layoutManager

        val fmsAdapter = ConcatAdapter(
            HeaderAdapter(getString(headerRes)),
            ModuleNavigationAdapter(
                menuRes,
                requireContext()
            ) { filterFmsAuthority(it) }
                .also {
                    it.navAction = navAction
                })
        viewBinding.rvFmsArea.adapter = fmsAdapter
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {

                Timber.e("viewType: ${fmsAdapter.getItemViewType(position)}")
                return when (fmsAdapter.getItemViewType(position)) {
                    0 -> layoutManager.spanCount
                    else -> 1
                }
            }
        }
    }

    private fun filterFmsAuthority(item: MenuItem): Boolean {
        val userAuthor = userViewModel.loginData.value?.menus ?: return true
        val authorTree =
            commonViewModel.authorityViewModel.value?.firstOrNull { it.name == getString(R.string.operation) }
                ?: return false

        val authorCode =
            authorTree.children.firstOrNull { it.name == item.title }?.id ?: return false
        return userAuthor.contains(authorCode.toString())
    }
}