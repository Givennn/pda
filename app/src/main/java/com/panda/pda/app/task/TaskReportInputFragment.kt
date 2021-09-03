package com.panda.pda.app.task

import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.app.BuildConfig
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.base.extension.toast
import com.panda.pda.app.databinding.FragmentTaskReportInputBinding
import timber.log.Timber
import java.io.File

/**
 * created by AnJiwei 2021/9/3
 */
class TaskReportInputFragment : BaseFragment(R.layout.fragment_task_report_input) {

    private lateinit var photoAdapter: TaskReportInputPhotoAdapter
    private val viewBinding by viewBinding<FragmentTaskReportInputBinding>()

    private var count = 0
    private val max = 3

    private val takeImageResult = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            latestTmpUri?.let { uri ->
                updatePhoto(uri)
            }
        } else {
            toast("打开摄像头失败")
        }
    }


    private var latestTmpUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupReportNumberEditText()
        setupPhotoAdapter()
    }

    private fun setupPhotoAdapter() {
        viewBinding.rvPhotoList.adapter = TaskReportInputPhotoAdapter()
            .also {
                it.onTakePhotoAction = { takePhoto() }
                photoAdapter = it
            }
    }

    private fun takePhoto() {

        toast("开始拍照")
        Timber.e("开始拍照")
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                Timber.e("获取缓存地址")
                latestTmpUri = uri
                Timber.e("打开相机应用")
                takeImageResult.launch(uri)
            }
        }
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png", requireContext().cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(requireActivity().applicationContext, "${BuildConfig.APPLICATION_ID}.provider", tmpFile)
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