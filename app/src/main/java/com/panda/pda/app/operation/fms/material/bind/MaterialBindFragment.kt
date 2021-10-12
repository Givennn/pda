package com.panda.pda.app.operation.fms.material.bind

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import com.panda.pda.app.R
import com.panda.pda.app.base.retrofit.*
import com.panda.pda.app.common.adapter.CommonViewBindingAdapter
import com.panda.pda.app.common.CommonSearchListFragment
import com.panda.pda.app.databinding.FrameEmptyViewBinding
import com.panda.pda.app.databinding.ItemMaterialBindBinding
import com.panda.pda.app.operation.fms.data.MaterialApi
import com.panda.pda.app.operation.fms.material.MaterialViewModel
import com.panda.pda.app.operation.fms.material.ProductScanFragment
import com.panda.pda.app.operation.fms.mission.TaskViewModel
import com.panda.pda.app.operation.fms.data.TaskApi
import com.panda.pda.app.operation.fms.data.model.TaskInfoModel
import com.panda.pda.app.operation.fms.data.model.TaskModel
import io.reactivex.rxjava3.core.Single

/**
 * created by AnJiwei 2021/8/30
 */
class MaterialBindFragment : CommonSearchListFragment<TaskModel>() {
    private val taskViewModel by activityViewModels<TaskViewModel>()
    val viewModel by activityViewModels<MaterialViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.etSearchBar.setHint(R.string.material_search_bar_hint)
    }

    override fun createAdapter(): CommonViewBindingAdapter<*, TaskModel> {
        return object :
            CommonViewBindingAdapter<ItemMaterialBindBinding, TaskModel>(mutableListOf()) {
            override fun createBinding(parent: ViewGroup): ItemMaterialBindBinding {
                return ItemMaterialBindBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            }

            override fun createEmptyViewBinding(parent: ViewGroup): ViewBinding {
                return FrameEmptyViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
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
                    clInfo.setOnClickListener {
                        onItemInfoClicked(data)
                    }
                }
            }
        }
    }

    private fun onItemInfoClicked(data: TaskModel) {
        if (taskViewModel.taskInfoData.value?.detail?.id == data.id) {
            navToDetailFragment(data.id)
            return
        }
        WebClient.request(TaskApi::class.java)
            .taskGetByIdGet(data.id)
            .zipWith(WebClient.request(TaskApi::class.java).taskOperationRecordGet(data.id),
                { detail, records -> TaskInfoModel(detail, records.dataList) })
            .onMainThread()
            .bindLoadingStatus()
            .subscribe({ info ->
                taskViewModel.taskInfoData.postValue(info)
                navToDetailFragment(data.id)
            }, { })
    }

    private fun navToDetailFragment(id: Int) {
        navController.navigate(R.id.taskDetailFragment,
            Bundle().apply { putInt(TaskViewModel.TASK_ID, id) })
    }


    override fun api(key: String?): Single<DataListNode<TaskModel>> =
        WebClient.request(MaterialApi::class.java)
            .materialTaskListByPageGet(key)

    override val titleResId: Int
        get() = R.string.material_bind

    private fun onItemActionClicked(data: TaskModel) {
        viewModel.selectedTaskData.postValue(data)
        viewModel.materialActionData.postValue(ProductScanFragment.MaterialAction.Bind)
        navController.navigate(R.id.action_materialBindFragment_to_productScanBindFragment,
            Bundle().apply {
//                putString(ProductScanFragment.ACTION_KEY,
//                    ProductScanFragment.MaterialAction.Bind.name)
                putInt(ProductScanFragment.TASK_ID, data.id)
            })
    }
}