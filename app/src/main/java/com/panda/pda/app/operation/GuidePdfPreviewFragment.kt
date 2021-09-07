package com.panda.pda.app.operation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.databinding.FragmentGuidePdfPreviewBinding
import java.io.FileDescriptor
import java.io.IOException

/**
 * create by AnJiwei 2021/9/5
 */
class GuidePdfPreviewFragment : BaseFragment(R.layout.fragment_guide_pdf_preview) {

    private val viewBinding by viewBinding<FragmentGuidePdfPreviewBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.topAppBar.setNavigationOnClickListener {
            navBackListener.invoke(it)
        }

        viewBinding.pdfPreview.fromUri(arguments?.getString(PDF_URI_KEY)?.toUri() ?: return)
    }

    companion object {
        const val PDF_URI_KEY = "pdfUrl"
    }
}