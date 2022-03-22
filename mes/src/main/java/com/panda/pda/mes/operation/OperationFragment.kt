package com.panda.pda.mes.operation

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.common.CommonViewModel
import com.panda.pda.mes.common.adapter.HeaderAdapter
import com.panda.pda.mes.common.adapter.ModuleNavigationAdapter
import com.panda.pda.mes.databinding.FragmentOperationBinding
import com.panda.pda.mes.operation.fms.material.MaterialViewModel
import com.panda.pda.mes.operation.fms.material.ProductScanFragment
import com.panda.pda.mes.user.UserViewModel
import timber.log.Timber

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
    fun GridLayoutManager.configSingleViewSpan(range: (position: Int) -> Boolean) {
        val oldSpanSizeLookup = spanSizeLookup
        val oldSpanCount = spanCount

        spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (range(position)) oldSpanCount else oldSpanSizeLookup.getSpanSize(position)
            }
        }
    }
    fun ConcatAdapter.getAdapterByItemPosition(position: Int): RecyclerView.Adapter<out RecyclerView.ViewHolder>? {
        var pos = position
        val adapters = adapters
        for (adapter in adapters) {
            when {
                pos >= adapter.itemCount -> {
                    pos -= adapter.itemCount
                }
                pos < 0 -> return null
                else -> return adapter
            }
        }
        return null
    }
    private fun initModuleList() {
//        layoutManager = GridLayoutManager(requireContext(), 4)
        layoutManager =  GridLayoutManager(requireContext(), 4).apply {
            configSingleViewSpan {
                //当子adapter 是ModuleNavigationAdapter 时按GirdLayout排列
                //其它adapter 按单行排列
                moduleListAdapter.getAdapterByItemPosition(it) !is ModuleNavigationAdapter
            }
        }
        moduleListAdapter = ConcatAdapter()
        viewBinding.rvModuleArea.adapter = moduleListAdapter

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
        //添加设备管理模块
        addModuleGridForEMS(
            R.menu.ems_nav_menu,
            R.string.equipment_manage,
        ) { navId ->
            navController.navigate(navId)
        }
//        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//            override fun getSpanSize(position: Int): Int {
//                Timber.e("position is:${moduleListAdapter.getItemViewType(position)}")
//                Timber.e("itemcount is:${moduleListAdapter.itemCount}")
//                Timber.e("adapters is:${moduleListAdapter.adapters.size}")
//                return when {
//
//                    moduleListAdapter.getItemViewType(position) % 2 == 0 -> layoutManager.spanCount
//                    else -> 1
//                }
//
//            }
//        }
        viewBinding.rvModuleArea.layoutManager = layoutManager
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
            ) { filterAuthority(it) }
                .also {
                    it.navAction = navAction
                })
    }
    //添加设备管理模块
    private fun addModuleGridForEMS(
        @MenuRes menuRes: Int,
        @StringRes headerRes: Int,
        navAction: (navId: Int) -> Unit,
    ) {
        if (filterDeviceAuthority( getString(headerRes))) {
            //如果配置了设备管理整个条目，下面三条任务。设备、工单都展示
            moduleListAdapter.addAdapter(HeaderAdapter(getString(headerRes)))
            moduleListAdapter.addAdapter(
                ModuleNavigationAdapter(
                    menuRes,
                    requireContext()
                ) { true }
                    .also {
                        it.navAction = navAction
                    })
        }

    }

    private fun filterAuthority(item: MenuItem): Boolean {
        val userAuthor = userViewModel.loginData.value?.menus ?: return true
        val authorTree =
            commonViewModel.authorityViewModel.value?.firstOrNull { it.name == getString(R.string.operation) }
                ?: return false
        val authorCode =
            authorTree.children.firstOrNull { it.name == item.title }?.id ?: return false
        return userAuthor.contains(authorCode.toString())
    }
    //设备管理模块，只要配置了设备管理，则添加维保任务、设备管理、维保工单三个子模块
    private fun filterDeviceAuthority(title:String): Boolean {
        val userAuthor = userViewModel.loginData.value?.menus ?: return true
        val authorTree =
            commonViewModel.authorityViewModel.value?.firstOrNull { it.name == getString(R.string.operation) }
                ?: return false
        val authorCode =
            authorTree.children.firstOrNull { it.name == title }?.id ?: return false
        return userAuthor.contains(authorCode.toString())
    }
}