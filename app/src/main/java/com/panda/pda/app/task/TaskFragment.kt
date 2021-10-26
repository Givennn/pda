package com.panda.pda.app.task

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ConcatAdapter
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.common.adapter.HeaderAdapter
import com.panda.pda.app.common.adapter.ModuleNavigationAdapter
import com.panda.pda.app.databinding.FragmentTaskBinding
import com.panda.pda.app.operation.fms.mission.TaskViewModel
import com.panda.pda.app.common.data.model.TaskMessageCountModel

/**
 * created by AnJiwei 2021/8/16
 */
class TaskFragment : BaseFragment(R.layout.fragment_task) {

    private val viewBinding by viewBinding<FragmentTaskBinding>()
    private val viewModel by activityViewModels<TaskViewModel>()
    override val isStatusBarLight: Boolean
        get() = true
    private lateinit var taskMessageNavigationAdapter: TaskMessageNavigationAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskMessageNavigationAdapter = TaskMessageNavigationAdapter(
            R.menu.task_msg_nav_menu,
            requireContext()
        ) { filterAuthority(it) }
            .also {
                it.navAction = {navId -> navController.navigate(navId)}
            }
        viewBinding.rvModuleArea.adapter = taskMessageNavigationAdapter

        viewModel.taskMsgData.observe(viewLifecycleOwner, { msg ->
            taskMessageNavigationAdapter.updateBadge(msg)
        })
    }

    private fun filterAuthority(item: MenuItem): Boolean {
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