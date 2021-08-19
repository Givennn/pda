package com.panda.pda.app

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.app.databinding.ActivityShellBinding
import timber.log.Timber

class ShellActivity : AppCompatActivity(R.layout.activity_shell) {

    private val viewBinding by viewBinding<ActivityShellBinding>(R.id.container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBottomNavigation()
    }

    private fun initBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_nav_host_shell) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            Timber.d("destination changed ${destination.id}")
            val visible = destination.id in arrayOf(
                R.id.operationFragment,
                R.id.taskFragment,
                R.id.discoveryFragment,
                R.id.profileFragment
            )
            viewBinding.nvBottom.visibility = if (visible) View.VISIBLE else View.GONE
        }
        viewBinding.nvBottom.setupWithNavController(navController)
        viewBinding.nvBottom.itemIconTintList = null
        val taskBadge = viewBinding.nvBottom.getOrCreateBadge(R.id.taskFragment)
        taskBadge.isVisible = true
    }

    
}
