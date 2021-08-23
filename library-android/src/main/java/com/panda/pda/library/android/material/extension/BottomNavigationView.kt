package com.panda.pda.library.android.material.extension

import android.view.View
import androidx.core.view.forEach
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.panda.pda.library.android.R

/**
 * created by AnJiwei 2021/8/20
 */

fun BottomNavigationView.hideWhenDestinationExclude(navController: NavController): BottomNavigationView = this.let { bottomNav ->
    val menuItemsId = mutableListOf<Int>()
    bottomNav.menu.forEach { menuItemsId.add(it.itemId) }
    navController.addOnDestinationChangedListener { _, destination, _ ->

        val visible = destination.id in menuItemsId
        bottomNav.visibility = if (visible) View.VISIBLE else View.GONE
    }
    return bottomNav
}

fun BottomNavigationView.customIcons(): BottomNavigationView = this.apply { itemIconTintList = null }

fun BottomNavigationView.setupNavControllerToFinalStack(navController: NavController): BottomNavigationView = this.apply {
    setOnItemSelectedListener {
    // 覆盖默认backStack逻辑，避免返回到最startDestination
    val builder = NavOptions.Builder()
        .setLaunchSingleTop(true)
        .setEnterAnim(R.anim.nav_default_enter_anim)
        .setExitAnim(R.anim.nav_default_exit_anim)
        .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
        .setPopExitAnim(R.anim.nav_default_pop_exit_anim)
    builder.setPopUpTo(navController.graph.id, false)
    val options = builder.build()
    navController.navigate(it.itemId, null, options)
    true
}
}