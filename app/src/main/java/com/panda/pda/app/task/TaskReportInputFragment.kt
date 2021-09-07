package com.panda.pda.app.task

import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.app.BuildConfig
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.base.extension.toast
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.databinding.FragmentTaskReportInputBinding
import com.panda.pda.app.task.data.TaskApi
import com.panda.pda.app.task.data.model.TaskInfoModel
import com.panda.pda.app.task.data.model.TaskReportRequest
import java.io.File

/**
 * created by AnJiwei 2021/9/3
 */
class TaskReportInputFragment : BaseFragment(R.layout.fragment_task_report_input) {

    private lateinit var photoAdapter: TaskReportInputPhotoAdapter
    private val viewBinding by viewBinding<FragmentTaskReportInputBinding>()
    private val viewModel by activityViewModels<TaskViewModel>()

    private var count = 0
    private var max = 1

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
        setupReportNumberEditText()
        setupPhotoAdapter()
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }
        viewModel.taskInfoData.observe(viewLifecycleOwner, { info ->
            val detail = info.detail
            max = detail.taskNum - detail.reportNum
            viewBinding.apply {
                tvPlanCode.text = detail.planCode
                tvTaskCode.text = detail.taskCode
                tvTaskDesc.text = detail.taskDesc
                tvProductCode.text = detail.productCode
                tvProductDesc.text = detail.productName
                tvTaskCount.text = detail.taskNum.toString()
                tvReportNumber.text = detail.reportNum.toString()
            }.btnConfirm.setOnClickListener {
                report(info)
            }
        })
    }

    private fun report(info: TaskInfoModel) {
        //todo 上传图片， 上传进度
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
        val tmpFile =
            File.createTempFile("tmp_image_file", ".png", requireContext().cacheDir).apply {
                createNewFile()
                deleteOnExit()
            }

        return FileProvider.getUriForFile(requireActivity().applicationContext,
            "${BuildConfig.APPLICATION_ID}.provider",
            tmpFile)
    }

    private fun updatePhoto(uri: Uri) {
        photoAdapter.addPhoto(uri)
    }

    private fun setupReportNumberEditText() {
        viewBinding.tilReportNum.apply {
            setEndIconOnClickListener {
                updateReportNum(++count)
                editText?.setText(count.toString())
            }
            setStartIconOnClickListener {
                updateReportNum(--count)
                editText?.setText(count.toString())
            }
            editText?.doOnTextChanged { text, _, _, _ ->
                val str = text.toString()
                if (str.isEmpty())
                    updateReportNum(0)
                else {
                    updateReportNum(str.toInt())
                }
                val number = str.toInt()
                if (count != number)
                    editText?.setText(count.toString())
            }
        }
    }

    private fun updateReportNum(number: Int) {
        val inputLayout = viewBinding.tilReportNum
        when {
            number in 1 until max -> {
                inputLayout.setStartIconTintList(ColorStateList.valueOf(requireContext().getColor(R.color.numberEditTextBtnEnable)))
                inputLayout.setEndIconTintList(ColorStateList.valueOf(requireContext().getColor(R.color.numberEditTextBtnEnable)))

            }
            number <= 0 -> {
                inputLayout.setStartIconTintList(ColorStateList.valueOf(requireContext().getColor(R.color.numberEditTextBtnDisable)))
                count = 0
            }
            number >= max -> {
                inputLayout.setEndIconTintList(ColorStateList.valueOf(requireContext().getColor(
                    R.color.numberEditTextBtnDisable)))
                count = max
            }
        }
    }
}