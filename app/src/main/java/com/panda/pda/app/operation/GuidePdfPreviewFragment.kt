package com.panda.pda.app.operation

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.databinding.FragmentGuidePdfPreviewBinding

/**
 * create by AnJiwei 2021/9/5
 */
class GuidePdfPreviewFragment: BaseFragment(R.layout.fragment_guide_pdf_preview) {
    private val viewBinding by viewBinding<FragmentGuidePdfPreviewBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.topAppBar.setNavigationOnClickListener {
            navBackListener.invoke(it)
        }
    }
}