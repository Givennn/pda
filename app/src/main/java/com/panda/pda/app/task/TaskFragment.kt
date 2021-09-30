package com.panda.pda.app.task

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.taskMsgData.observe(viewLifecycleOwner, { msg ->
            updateBadge(msg)
        })
        viewBinding.llTaskReceive.setOnClickListener {
            navController.navigate(R.id.taskReceiveFragment)
        }
        viewBinding.llTaskFinish.setOnClickListener {
            navController.navigate(R.id.taskFinishFragment)
        }

        viewBinding.llTaskExecute.setOnClickListener {
            navController.navigate(R.id.taskExecuteFragment)
        }

        viewBinding.llTaskReport.setOnClickListener {
            navController.navigate(R.id.taskReportFragment)
        }
    }

    private fun updateBadge(msgData: TaskMessageCountModel) {
        setBadgeNum(viewBinding.tvTaskReceiveBadge, msgData.taskToReceive)
        setBadgeNum(viewBinding.tvTaskExecuteBadge, msgData.taskToRun)
        setBadgeNum(viewBinding.tvTaskReportBadge, msgData.taskToReport)
        setBadgeNum(viewBinding.tvTaskFinishBadge, msgData.taskToFinish)
    }

    private fun setBadgeNum(textView: TextView, count: Int): Boolean {
        val badgeText = when {
            count <= 0 -> null
            count > 99 -> "99+"
            else -> count.toString()
        }
        textView.visibility = if (badgeText == null) View.INVISIBLE else View.VISIBLE
        textView.text = badgeText ?: ""
        return badgeText == null
    }
}