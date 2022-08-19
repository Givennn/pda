package com.panda.pda.mes

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.library.android.material.extension.customIcons
import com.panda.pda.library.android.material.extension.hideWhenDestinationExclude
import com.panda.pda.library.android.material.extension.isDestinationInMenu
import com.panda.pda.library.android.material.extension.setupNavControllerToFinalStack
import com.panda.pda.mes.base.retrofit.DataListNode
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.base.retrofit.onMainThread
import com.panda.pda.mes.common.data.CommonApi
import com.panda.pda.mes.common.data.model.TaskMessageCountModel
import com.panda.pda.mes.databinding.ActivityShellBinding
import com.panda.pda.mes.operation.ems.data.EquipmentApi
import com.panda.pda.mes.operation.fms.mission.TaskViewModel
import com.panda.pda.mes.user.UserViewModel
import com.panda.pda.mes.user.data.UserApi
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit


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
        customNavAction()
        viewBinding.btnScan.setOnClickListener {
            navController.navigate(R.id.order_exchange_nav_graph)
        }
    }

    private fun initTokenEvent() {
        userViewModel.logoutActionData.observe(this) {
            viewBinding.nvBottom.selectedItemId = R.id.taskFragment

            navController.navigate(
                R.id.loginFragment,
                null,
                NavOptions.Builder().setPopUpTo(R.id.shell_nav_graph, true).build()
            )
        }
    }

    private fun createNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_nav_host_shell) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun customNavAction() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (
//                destination.id in arrayOf(
//                    R.id.operationFragment,
//                    R.id.taskFragment,
//                    R.id.profileFragment,
//                    R.id.operationFragment
//                )
                viewBinding.nvBottom.isDestinationInMenu(destination.id)
            ) {
                updateTaskCount()
            }
        }
    }

    private fun initViewModel() {
        // update badge from viewModel
        taskViewModel.taskMsgData.observe(this, Observer {
            val badge = viewBinding.nvBottom.getOrCreateBadge(R.id.taskFragment)
            badge.isVisible = it.sumOf { msg -> msg.count } > 0
        })
    }

    private fun updateTaskCount() {
        WebClient.request(CommonApi::class.java)
            .pdaTaskMsgCountGet()
            .concatMap { old ->
                //ems待处理任务数量获取，如果获取到的数量为0，则不展示维保任务
                WebClient.request(EquipmentApi::class.java)
                    .pdaEquipmentTaskMsgCountGet().map {
                        old + it
                    } // ems api
            }
            .onMainThread()
//            .unWrapperData()
            .subscribe({
                taskViewModel.taskMsgData.postValue(it)
            }, { })
    }

    private fun initBottomNavigation() {
        viewBinding.nvBottom.background = null
        viewBinding.nvBottom.menu[2].isEnabled = false
        viewBinding.nvBottom.hideWhenDestinationExclude(navController) {
            viewBinding.btnScan.visibility = it
            viewBinding.bottomAppBar.visibility = it
        }
            .customIcons()
            .setupNavControllerToFinalStack(navController)
    }
}
