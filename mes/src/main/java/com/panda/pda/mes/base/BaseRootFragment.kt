package com.panda.pda.mes.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.panda.pda.mes.R

/**
 * created by AnJiwei 2022/8/19
 */
abstract class BaseRootFragment(@LayoutRes contentLayoutId: Int) : BaseFragment(contentLayoutId) {

    private lateinit var badgeDrawable: BadgeDrawable

    abstract fun getTopToolBar(): MaterialToolbar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topAppBar = getTopToolBar()
        topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }
        badgeDrawable = BadgeDrawable.create(requireContext()).apply {
            isVisible = false
        }
        BadgeUtils.attachBadgeDrawable(badgeDrawable, topAppBar, R.id.message)
        topAppBar.setOnMenuItemClickListener {
            if (it.itemId == R.id.message) {
                showMessage()
            }
            true
        }
    }

    private fun showMessage() {
//        navController.navigate(R.id.messageFragment)
        navController.navigate(R.id.messageHistoryFragment)
    }

    protected fun setMessageCount(count: Int) {
        val badgeText = when {
            count <= 0 -> null
            count > 99 -> "99+"
            else -> count.toString()
        }
        badgeDrawable.isVisible = badgeText != null
        badgeDrawable.number = count
    }
}