package com.panda.pda.app.task

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.app.BuildConfig
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.base.extension.toast
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.common.data.CommonApi
import com.panda.pda.app.common.data.model.FileInfoModel
import com.panda.pda.app.databinding.FragmentTaskReportInputBinding
import com.panda.pda.app.task.data.TaskApi
import com.panda.pda.app.task.data.model.TaskInfoModel
import com.panda.pda.app.task.data.model.TaskReportRequest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.internal.notify
import timber.log.Timber
import java.io.File

/**
 * created by AnJiwei 2021/9/3
 */
class TaskReportInputFragment : BaseFragment(R.layout.fragment_task_report_input) {

    private lateinit var tmpFile: File
    private lateinit var photoAdapter: TaskReportInputPhotoAdapter
    private val viewBinding by viewBinding<FragmentTaskReportInputBinding>()
    private val viewModel by activityViewModels<TaskViewModel>()

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    updatePhoto(uri)
                }
            }
        }
    private var latestTmpUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPhotoAdapter()
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }
        viewModel.taskInfoData.observe(viewLifecycleOwner, { info ->
            val detail = info.detail
            viewBinding.apply {
                tvPlanCode.text = detail.planCode
                tvTaskCode.text = detail.taskCode
                tvTaskDesc.text = detail.taskDesc
                tvProductCode.text = detail.productCode
                tvProductDesc.text = detail.productName
                tvTaskCount.text = detail.taskNum.toString()
                tvReportNumber.text = detail.reportNum.toString()
                tilReportNum.minValue = 1
                tilReportNum.maxValue = detail.taskNum - detail.reportNum
            }.btnConfirm.setOnClickListener {
                report(info)
            }
        })
    }

    private fun report(info: TaskInfoModel) {
        val request = TaskReportRequest(info.detail.id,
            viewBinding.etReportNum.text.toString().toInt(),
            viewBinding.etRemark.text.toString(),
            photoAdapter.getDataSource())
        WebClient.request(TaskApi::class.java)
            .pdaFmsTaskReportConfirmPost(request)
            .bindToFragment()
            .subscribe({
                toast("任务报工成功")
                navBackListener.invoke(requireView())
            }, {})
    }

    private fun setupPhotoAdapter() {
        viewBinding.rvPhotoList.adapter = TaskReportInputPhotoAdapter()
            .also {
                it.onTakePhotoAction = { takePhoto() }
                photoAdapter = it
            }
    }

    private fun takePhoto() {

        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    private fun getTmpFileUri(): Uri {
        tmpFile =
            File.createTempFile("tmp_image_file", ".$IMAGE_TYPE", requireContext().cacheDir).apply {
                createNewFile()
                deleteOnExit()
            }

        return FileProvider.getUriForFile(requireActivity().applicationContext,
            "${BuildConfig.APPLICATION_ID}.provider",
            tmpFile)
    }

    private fun updatePhoto(uri: Uri) {

        WebClient.downLoader().create(CommonApi::class.java)
            .pdaCommonUploadFilePost(MultipartBody.Part.createFormData("file",
                tmpFile.name,
                tmpFile.asRequestBody("image/$IMAGE_TYPE".toMediaType())))
            .bindToFragment()
            .subscribe({
                it.fileLocalUri = uri
                photoAdapter.getDataSource().add(it)
                photoAdapter.notifyDataSetChanged()
                Timber.d("url: ${it.fileUrl}")
            }, {})
    }

    companion object {
        const val IMAGE_TYPE = "png"
    }
}