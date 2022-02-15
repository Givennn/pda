package com.panda.pda.mes.operation.fms.guide

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.extension.toast
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.data.CommonApi
import com.panda.pda.mes.databinding.FragmentGuidePdfPreviewBinding
import com.panda.pda.mes.operation.fms.data.model.GuideInfoModel
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.Exception

/**
 * create by AnJiwei 2021/9/5
 */
class GuidePdfPreviewFragment : BaseFragment(R.layout.fragment_guide_pdf_preview) {

    private val viewBinding by viewBinding<FragmentGuidePdfPreviewBinding>()

    private lateinit var guideInfo: GuideInfoModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.topAppBar.setNavigationOnClickListener {
            navBackListener.invoke(it)
        }

        guideInfo = requireArguments().getSerializable(PDF_KEY) as GuideInfoModel

        viewBinding.topAppBar.title = guideInfo.fileName
        downLoadPdf()
    }

    private fun downLoadPdf() {
        WebClient.downLoader().create(CommonApi::class.java)
            .downloadFile(guideInfo.fileUrl, guideInfo.fileName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .bindToLifecycle(requireView())
            .bindLoadingStatus()
            .subscribe({
                try {
                    viewBinding.pdfPreview.fromStream(it.body()?.byteStream()).load()
                } catch (err: Exception) {
                    toast(err.message ?: "")
                }
            }, { toast(it.message ?: "") })
    }

    companion object {
        const val PDF_KEY = "pdfUrl"
    }
}