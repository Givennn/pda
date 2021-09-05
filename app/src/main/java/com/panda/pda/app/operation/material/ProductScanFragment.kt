package com.panda.pda.app.operation.material

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.base.extension.toast
import com.panda.pda.app.databinding.FragmentProductScanBinding

/**
 * created by AnJiwei 2021/8/31
 */
class ProductScanFragment : BaseFragment(R.layout.fragment_product_scan) {

    private val viewBinding by viewBinding<FragmentProductScanBinding>()
    private lateinit var materialAction: MaterialAction
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val actionName = arguments?.getString(ACTION_KEY)
        if (actionName == null || !MaterialAction.values().map { it.name }.contains(actionName)) {
            toast("未定义物料操作")
            return
        } else {
            materialAction = MaterialAction.valueOf(actionName)
        }
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener(it) }
        viewBinding.etSearchBar.setOnEditorActionListener { editText, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onProductInput()
            } else if (event.keyCode == KeyEvent.KEYCODE_ENTER) {
                onProductInput()
                (editText as EditText).selectAll()
                return@setOnEditorActionListener true
            }
            false
        }
    }

    private fun onProductInput() {
        //TODO update search bar tint

        val navId = when (materialAction) {
            MaterialAction.Bind -> R.id.action_productScanBindFragment_to_materialBindingProductFragment
            MaterialAction.Unbind -> R.id.action_productScanUnbindFragment_to_materialUnbindFragment
            MaterialAction.Replace -> R.id.action_productScanReplaceFragment_to_materialReplaceFragment
        }

        //todo verify product code, api undefined
        navController.navigate(navId, Bundle().apply {
            putString(SCANNED_PRODUCT_CODE, viewBinding.etSearchBar.text.toString())
        })
    }

    enum class MaterialAction {
        Bind,
        Unbind,
        Replace
    }

    companion object {
        const val ACTION_KEY = "material_action"
        const val SCANNED_PRODUCT_CODE = "product_code"
    }
}