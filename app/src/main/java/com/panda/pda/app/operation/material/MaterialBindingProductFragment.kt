package com.panda.pda.app.operation.material

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.databinding.FragmentMaterialBindingProductBinding

/**
 * created by AnJiwei 2021/8/31
 */
class MaterialBindingProductFragment: BaseFragment(R.layout.fragment_material_binding_product) {

    private val viewBinding by viewBinding<FragmentMaterialBindingProductBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener(it) }

        //TODO 物料绑定
    }
}