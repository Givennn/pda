package com.panda.pda.app.operation

import android.os.Bundle
import android.view.View
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.databinding.FragmentOperationBinding
import com.panda.pda.app.operation.material.MaterialViewModel
import com.panda.pda.app.operation.material.ProductScanFragment
import timber.log.Timber

/**
 * created by AnJiwei 2021/8/16
 */
class OperationFragment : BaseFragment(R.layout.fragment_operation) {

    private val viewBinding by viewBinding<FragmentOperationBinding>()
    private val materialViewModel by activityViewModels<MaterialViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.clFactoryManageArea.children.forEach {
            it.setOnClickListener { llBtn ->
                when (llBtn.id) {
                    R.id.material_unbind_nav_graph ->
                    {
                        Timber.e("unbind clicked")
                        materialViewModel.materialActionData.postValue(ProductScanFragment.MaterialAction.Unbind)
                    }

//                        navController.setGraph(R.navigation.material_unbind_nav_graph,
//                            Bundle().apply {
//                                putString(ProductScanFragment.ACTION_KEY,
//                                    ProductScanFragment.MaterialAction.Unbind.name)
//                            })
                    R.id.material_replace_nav_graph -> {
                        Timber.e("replace clicked")
                        materialViewModel.materialActionData.postValue(ProductScanFragment.MaterialAction.Replace)

                    }
//                        navController.setGraph(R.navigation.material_replace_nav_graph,
//                            Bundle().apply {
//                                putString(ProductScanFragment.ACTION_KEY,
//                                    ProductScanFragment.MaterialAction.Replace.name)
//                            })

//                    else -> navController.navigate(llBtn.id)
                }
                navController.navigate(llBtn.id)
            }
        }
    }
}