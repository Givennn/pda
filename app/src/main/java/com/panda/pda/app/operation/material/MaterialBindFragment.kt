package com.panda.pda.app.operation.material

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.viewbinding.ViewBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.base.BaseRecycleViewAdapter
import com.panda.pda.app.base.extension.toast
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.base.retrofit.onMainThread
import com.panda.pda.app.base.retrofit.unWrapperData
import com.panda.pda.app.databinding.FragmentMaterialBindBinding
import com.panda.pda.app.databinding.FrameEmptyViewBinding
import com.panda.pda.app.databinding.ItemMaterialBindBinding
import com.panda.pda.app.task.data.TaskApi
import com.panda.pda.app.task.data.model.TaskModel

/**
 * created by AnJiwei 2021/8/30
 */
class MaterialBindFragment: BaseFragment(R.layout.fragment_material_bind) {

    private val viewBinding by viewBinding<FragmentMaterialBindBinding>()

    private lateinit var adapter: BaseRecycleViewAdapter<ItemMaterialBindBinding, TaskModel>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createAdapter()
        viewBinding.root.setOnRefreshListener { refreshData() }
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener(it) }
        viewBinding.etSearchBar.setOnEditorActionListener { editText, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                refreshData()
            } else if (event.keyCode == KeyEvent.KEYCODE_ENTER) {
                refreshData()
                (editText as EditText).selectAll()
                return@setOnEditorActionListener true
            }
            false
        }
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    private fun createAdapter() {
        adapter =
            object : BaseRecycleViewAdapter<ItemMaterialBindBinding, TaskModel>(mutableListOf()) {
                override fun createBinding(parent: ViewGroup): ItemMaterialBindBinding {
                    return ItemMaterialBindBinding.inflate(LayoutInflater.from(parent.context))
                }

                override fun createEmptyViewBinding(parent: ViewGroup): ViewBinding {
                    return FrameEmptyViewBinding.inflate(LayoutInflater.from(parent.context),
                        parent,
                        false)
                }

                override fun onBindViewHolderWithData(
                    holder: ViewBindingHolder,
                    data: TaskModel,
                    position: Int,
                ) {
                    holder.itemViewBinding.apply {
                        tvTaskCode.text = data.taskCode
                        tvTaskDesc.text = data.taskDesc
                        tvTaskNumber.text = data.taskNum.toString()
                        btnAction.setOnClickListener {
                            onItemActionClicked(data)
                        }
                    }
                }


            }
        viewBinding.rvTaskList.adapter = adapter
    }

    private fun refreshData() {
        WebClient.request(TaskApi::class.java)
            .taskReceiveListByPageGet(viewBinding.etSearchBar.text?.toString())
            .onMainThread()
            .unWrapperData()
            .doFinally { viewBinding.root.isRefreshing = false }
            .subscribe({ data -> adapter.refreshData(data.dataList) },
                { toast(it.message ?: getString(R.string.net_work_error)) })
    }

    private fun onItemActionClicked(data: TaskModel) {

       navController.navigate(R.id.action_materialBindFragment_to_productScanBindFragment)
    }
}