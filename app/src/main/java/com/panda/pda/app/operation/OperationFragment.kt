package com.panda.pda.app.operation

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.common.CommonViewModel
import com.panda.pda.app.common.adapter.HeaderAdapter
import com.panda.pda.app.common.adapter.ModuleNavigationAdapter
import com.panda.pda.app.databinding.FragmentOperationBinding
import com.panda.pda.app.operation.fms.material.MaterialViewModel
import com.panda.pda.app.operation.fms.material.ProductScanFragment
import com.panda.pda.app.user.UserViewModel

/**
 * created by AnJiwei 2021/8/16
 */
class OperationFragment : BaseFragment(R.layout.fragment_operation) {

    private lateinit var moduleListAdapter: ConcatAdapter
    private lateinit var layoutManager: GridLayoutManager

    private val viewBinding by viewBinding<FragmentOperationBinding>()

    private val materialViewModel by activityViewModels<MaterialViewModel>()

    private val userViewModel by activityViewModels<UserViewModel>()

    private val commonViewModel by activityViewModels<CommonViewModel>()

    override val isStatusBarLight: Boolean
        get() = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initModuleList()

    }

    private fun initModuleList() {
        layoutManager = GridLayoutManager(requireContext(), 4)
        moduleListAdapter = ConcatAdapter()
        viewBinding.rvModuleArea.layoutManager = layoutManager
        viewBinding.rvModuleArea.adapter = moduleListAdapter
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {

                return when {
                    moduleListAdapter.getItemViewType(position) % 2 == 0 -> layoutManager.spanCount
                    else -> 1
                }
            }
        }
        addModuleGrid(
            R.menu.fms_nav_menu,
            R.string.factory_manage,
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

        addModuleGrid(
            R.menu.qms_nav_menu,
            R.string.quality_manage,
        ) { navId ->
            navController.navigate(navId)
        }
    }

    private fun addModuleGrid(
        @MenuRes menuRes: Int,
        @StringRes headerRes: Int,
        navAction: (navId: Int) -> Unit,
    ) {
        moduleListAdapter.addAdapter(HeaderAdapter(getString(headerRes)))
        moduleListAdapter.addAdapter(
            ModuleNavigationAdapter(
                menuRes,
                requireContext()
            ) { filterFmsAuthority(it) }
                .also {
                    it.navAction = navAction
                })
    }

    private fun filterFmsAuthority(item: MenuItem): Boolean {
//        val userAuthor = userViewModel.loginData.value?.menus ?: return true
//        val authorTree =
//            commonViewModel.authorityViewModel.value?.firstOrNull { it.name == getString(R.string.operation) }
//                ?: return false
//
//        val authorCode =
//            authorTree.children.firstOrNull { it.name == item.title }?.id ?: return false
//        return userAuthor.contains(authorCode.toString())
        return true
    }
}