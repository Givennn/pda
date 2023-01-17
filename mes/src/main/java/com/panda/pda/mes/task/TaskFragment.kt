package com.panda.pda.mes.task

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.appbar.MaterialToolbar
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.BaseRootFragment
import com.panda.pda.mes.common.CommonViewModel
import com.panda.pda.mes.databinding.FragmentTaskBinding
import com.panda.pda.mes.operation.fms.mission.TaskViewModel
import com.panda.pda.mes.user.UserViewModel

/**
 * created by AnJiwei 2021/8/16
 */
class TaskFragment : BaseRootFragment(R.layout.fragment_task) {

    private val viewBinding by viewBinding<FragmentTaskBinding>()
    private val viewModel by activityViewModels<TaskViewModel>()
    private val userViewModel by activityViewModels<UserViewModel>()
    private val commonViewModel by activityViewModels<CommonViewModel>()

    override val isStatusBarLight: Boolean
        get() = true
    private lateinit var taskMessageNavigationAdapter: TaskMessageNavigationAdapter
    override fun getTopToolBar(): MaterialToolbar {
        return viewBinding.topAppBar
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskMessageNavigationAdapter = TaskMessageNavigationAdapter(
            R.menu.task_msg_nav_menu,
            requireContext()
        ) { filterAuthority(it) || it.title == getString(R.string.equipment_task) }
            .also {
                it.navAction = { navId -> navController.navigate(navId) }
            }
        viewBinding.rvModuleArea.adapter = taskMessageNavigationAdapter

        viewModel.taskMsgData.observe(viewLifecycleOwner) { msg ->
            taskMessageNavigationAdapter.updateBadge(msg)
        }
        userViewModel.topMessageCount.observe(viewLifecycleOwner) { topMessageCount ->
            topMessageCount?.let { setMessageCount(it) }
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
}