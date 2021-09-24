package com.panda.pda.app

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.base.retrofit.onMainThread
import com.panda.pda.app.base.retrofit.unWrapperData
import com.panda.pda.app.common.data.CommonApi
import com.panda.pda.app.databinding.ActivityShellBinding
import com.panda.pda.app.operation.fms.mission.TaskViewModel
import com.panda.pda.app.operation.fms.mission.data.TaskApi
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
        setTransparentStatusBar()
        createNavController()
        initBottomNavigation()
        initViewModel()
        initTokenEvent()
        customNavAction()
    }

    private fun setTransparentStatusBar() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }


    private fun initTokenEvent() {
        userViewModel.logoutActionData.observe(this, {
            viewBinding.nvBottom.selectedItemId = R.id.taskFragment

            navController.navigate(
                R.id.loginFragment,
                null,
                NavOptions.Builder().setPopUpTo(R.id.shell_nav_graph, true).build()
            )
        })
    }

    private fun createNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_nav_host_shell) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun customNavAction() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id !in arrayOf(R.id.loginFragment, R.id.splashFragment)) {
                updateTaskCount()
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
        WebClient.request(CommonApi::class.java)
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
