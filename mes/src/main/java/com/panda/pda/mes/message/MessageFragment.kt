package com.panda.pda.mes.message

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.databinding.FragmentMessageBinding
import com.panda.pda.mes.databinding.FragmentTaskBinding
import com.panda.pda.mes.task.TaskMessageNavigationAdapter

/**
 * created by AnJiwei 2022/6/21
 */
class MessageFragment : BaseFragment(R.layout.fragment_message) {

    private val viewBinding by viewBinding<FragmentMessageBinding>()

    override val isStatusBarLight: Boolean
        get() = true
    private lateinit var taskMessageNavigationAdapter: TaskMessageNavigationAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskMessageNavigationAdapter = TaskMessageNavigationAdapter(
            R.menu.task_msg_nav_menu,
            requireContext()
        ) { true }
//            .also {
//            it.navAction = { navId -> navController.navigate(navId) }
//        }
        viewBinding.rvModuleArea.adapter = taskMessageNavigationAdapter

//        viewModel.taskMsgData.observe(viewLifecycleOwner) { msg ->
//            taskMessageNavigationAdapter.updateBadge(msg)
//        }
    }

//    private fun filterAuthority(item: MenuItem): Boolean {
//        val userAuthor = userViewModel.loginData.value?.menus ?: return true
//        val authorTree =
//            commonViewModel.authorityViewModel.value?.firstOrNull { it.name == getString(R.string.operation) }
//                ?: return false
//
//        val authorCode =
//            authorTree.children.firstOrNull { it.name == item.title }?.id ?: return false
//        return userAuthor.contains(authorCode.toString())
//    }
}