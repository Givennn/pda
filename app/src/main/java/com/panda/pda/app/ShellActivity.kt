package com.panda.pda.app

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.app.base.extension.toast
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.base.retrofit.onMainThread
import com.panda.pda.app.base.retrofit.unWrapperData
import com.panda.pda.app.databinding.ActivityShellBinding
import com.panda.pda.app.task.TaskViewModel
import com.panda.pda.app.task.data.TaskApi
import com.panda.pda.app.user.UserViewModel
import com.panda.pda.library.android.material.extension.customIcons
import com.panda.pda.library.android.material.extension.hideWhenDestinationExclude
import com.panda.pda.library.android.material.extension.setupNavControllerToFinalStack

class ShellActivity : AppCompatActivity(R.layout.activity_shell) {

    private lateinit var navController: NavController

    private val viewBinding by viewBinding<ActivityShellBinding>(R.id.container)

    @Suppress("unused")
    private val shellViewModel by viewModels<ShellViewModel>()
    private val taskViewModel by viewModels<TaskViewModel>()
    private val userViewModel by viewModels<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNavController()
        initBottomNavigation()
        initViewModel()
        initTokenEvent()
        customThemes()
    }

    private fun initTokenEvent() {
        userViewModel.logoutActionData.observe(this, { _ ->

            navController.navigate(R.id.loginFragment,
                null,
                NavOptions.Builder().setPopUpTo(R.id.shell_nav_graph, true).build())
        })
    }

    private fun createNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_nav_host_shell) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun customThemes() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                in arrayOf(R.id.splashFragment,
                    R.id.loginFragment,
                    R.id.taskFragment,
                    R.id.operationFragment,
                    R.id.discoveryFragment,
                    R.id.profileFragment),
                -> {
                    window.statusBarColor = getColor(R.color.white)
                    if (destination.id != R.id.loginFragment) {
                        updateTaskCount()
                    }
                }
                else -> {
                    window.statusBarColor = getColor(R.color.colorPrimaryDark)
                }

            }
        }
    }

    private fun initViewModel() {
        // update badge from viewModel
        taskViewModel.taskMsgData.observe(this, Observer {
            val badge = viewBinding.nvBottom.getOrCreateBadge(R.id.taskFragment)
            badge.isVisible = it.hasNewMessage()
        })
    }

    private fun updateTaskCount() {
        WebClient.request(TaskApi::class.java)
            .pdaTaskMsgCountGet()
            .onMainThread()
            .unWrapperData()
            .subscribe({
                taskViewModel.taskMsgData.postValue(it)
            }, {})
    }

    private fun initBottomNavigation() {
        viewBinding.nvBottom.hideWhenDestinationExclude(navController)
            .customIcons()
            .setupNavControllerToFinalStack(navController)
    }
}
