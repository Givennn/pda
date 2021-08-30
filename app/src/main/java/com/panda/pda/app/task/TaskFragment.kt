package com.panda.pda.app.task

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.databinding.FragmentTaskBinding

/**
 * created by AnJiwei 2021/8/16
 */
class TaskFragment: BaseFragment(R.layout.fragment_task) {

    private val binding by viewBinding<FragmentTaskBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.llTaskReceive.setOnClickListener {
            navController.navigate(R.id.taskReceiveFragment)
        }
        binding.llTaskFinish.setOnClickListener {
            navController.navigate(R.id.taskFinishFragment)
        }

        binding.llTaskExecute.setOnClickListener {
            navController.navigate(R.id.taskExecuteFragment)
        }

        binding.llTaskReport.setOnClickListener {
            navController.navigate(R.id.taskReportFragment)
        }
    }
}