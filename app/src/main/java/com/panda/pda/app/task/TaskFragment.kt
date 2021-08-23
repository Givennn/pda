package com.panda.pda.app.task

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.databinding.FragmentTaskBinding

/**
 * created by AnJiwei 2021/8/16
 */
class TaskFragment: BaseFragment(R.layout.fragment_task) {

    private val binding by viewBinding<FragmentTaskBinding>()

    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        binding.llTaskReceive.setOnClickListener {
            navController.navigate(R.id.taskReceiveFragment)
        }
        binding.llTaskFinish.setOnClickListener {
            navController.navigate(R.id.taskFinishFragment)
        }
    }
}