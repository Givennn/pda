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
import com.panda.pda.app.databinding.FragmentMaterialUnbindBinding
import com.panda.pda.app.databinding.FrameEmptyViewBinding
import com.panda.pda.app.databinding.ItemMaterialUnbindBinding
import com.panda.pda.app.operation.data.model.MaterialModel
import com.panda.pda.app.task.TaskViewModel

/**
 * created by AnJiwei 2021/8/30
 */
class MaterialUnbindFragment: BaseFragment(R.layout.fragment_material_unbind) {

    private val viewBinding by viewBinding<FragmentMaterialUnbindBinding>()

    private lateinit var adapter: BaseRecycleViewAdapter<ItemMaterialUnbindBinding, MaterialModel>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createAdapter()
        viewBinding.root.setOnRefreshListener { refreshData() }
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener(it) }
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    private fun createAdapter() {
        adapter =
            object : BaseRecycleViewAdapter<ItemMaterialUnbindBinding, MaterialModel>(mutableListOf()) {
                override fun createBinding(parent: ViewGroup): ItemMaterialUnbindBinding {
                    return ItemMaterialUnbindBinding.inflate(LayoutInflater.from(parent.context))
                }

                override fun createEmptyViewBinding(parent: ViewGroup): ViewBinding {
                    return FrameEmptyViewBinding.inflate(LayoutInflater.from(parent.context),
                        parent,
                        false)
                }

                override fun onBindViewHolderWithData(
                    holder: ViewBindingHolder,
                    data: MaterialModel,
                    position: Int,
                ) {
                    holder.itemViewBinding.apply {
                        tvMaterialCode.text = data.materialCode
                        tvMaterialDesc.text = data.materialName
                        btnAction.setOnClickListener {
                            onItemActionClicked(data)
                        }
                    }
                }


            }
        viewBinding.rvTaskList.adapter = adapter
    }

    private fun navToDetailFragment(id: Int) {
        navController.navigate(R.id.taskDetailFragment,
            Bundle().apply { putInt(TaskViewModel.TASK_ID, id) })
    }


    private fun refreshData() {
//        WebClient.request(TaskApi::class.java)
//            .taskCompleteListGet()
//            .onMainThread()
//            .unWrapperData()
//            .doFinally { viewBinding.root.isRefreshing = false }
//            .subscribe({ data -> adapter.refreshData(data.dataList) },
//                { toast(it.message ?: getString(R.string.net_work_error)) })
    }

    private fun onItemActionClicked(data: MaterialModel) {

//        val dialog = ConfirmDialogFragment().setTitle(getString(R.string.task_finish_confirm))
//            .setConfirmButton({ _, _ ->
//                WebClient.request(TaskApi::class.java)
//                    .taskCompleteConfirmPost(TaskIdRequest(data.id))
//                    .onMainThread()
//                    .unWrapperData()
//                    .bindToFragment()
//                    .subscribe({ toast(R.string.task_finish_toast) },
//                        { })
//            })
//        dialog.show(parentFragmentManager, TAG)
    }
}