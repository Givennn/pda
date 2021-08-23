package com.panda.pda.app

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.app.databinding.ActivityShellBinding
import com.panda.pda.library.android.material.extension.customIcons
import com.panda.pda.library.android.material.extension.hideWhenDestinationExclude
import com.panda.pda.library.android.material.extension.setupNavControllerToFinalStack
import timber.log.Timber

class ShellActivity : AppCompatActivity(R.layout.activity_shell) {

    private lateinit var navController: NavController

    private val viewBinding by viewBinding<ActivityShellBinding>(R.id.container)

    private val viewModel by viewModels<ShellViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNavController()
        initBottomNavigation()
        customThemes()
        initViewModel()
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
                    R.id.profileFragment)
                -> {
                    window.statusBarColor = getColor(R.color.white)
                }
                else -> {
                    window.statusBarColor = getColor(R.color.colorPrimaryDark)
                }

            }
        }
    }

    private fun initViewModel() {
        // update badge from viewModel
    }

    private fun initBottomNavigation() {

        viewBinding.nvBottom.hideWhenDestinationExclude(navController)
            .customIcons()
            .setupNavControllerToFinalStack(navController)
    }
}
