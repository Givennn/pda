package com.panda.pda.mes.operation.exchange_card

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.databinding.FragmentExchangeScanBinding

/**
 * created by AnJiwei 2022/8/19
 */
class ExchangeScanFragment: BaseFragment(R.layout.fragment_exchange_scan) {

    val viewBinding by viewBinding<FragmentExchangeScanBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener(it) }
        viewBinding.etScanBar.setOnEditorActionListener { editText, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onSearching()
            } else if (event.keyCode == KeyEvent.KEYCODE_ENTER) {
                onSearching()
                (editText as EditText).selectAll()
            }
            false
        }
    }

    private fun onSearching() {

    }
}