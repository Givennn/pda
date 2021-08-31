package com.panda.pda.app.operation.material

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.databinding.FragmentProductScanBinding

/**
 * created by AnJiwei 2021/8/31
 */
class ProductScanFragment: BaseFragment(R.layout.fragment_product_scan) {

    private val viewBinding by viewBinding<FragmentProductScanBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        navController.navigate(R.id.action_productScanBindFragment_to_materialBindingProductFragment)
    }
}