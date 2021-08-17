package com.panda.pda.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.panda.pda.app.databinding.ActivityShellBinding

class ShellActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShellBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShellBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBottomNavigation()
    }

    private fun initBottomNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fcv_nav_host_shell) as NavHostFragment
        binding.nvBottom.setupWithNavController(navHostFragment.navController)
    }
}
