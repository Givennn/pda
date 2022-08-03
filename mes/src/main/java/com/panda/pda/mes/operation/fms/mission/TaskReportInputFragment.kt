package com.panda.pda.mes.operation.fms.mission

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.mes.BuildConfig
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.extension.*
import com.panda.pda.mes.base.retrofit.DataListNode
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.PersonSelectFragment
import com.panda.pda.mes.common.data.CommonApi
import com.panda.pda.mes.common.data.model.PersonModel
import com.panda.pda.mes.common.data.model.UserModel
import com.panda.pda.mes.databinding.FragmentTaskReportInputBinding
import com.panda.pda.mes.operation.fms.data.TaskApi
import com.panda.pda.mes.operation.fms.data.model.TaskInfoModel
import com.panda.pda.mes.operation.fms.data.model.TaskReportRequest
import com.panda.pda.mes.operation.qms.data.model.QualityTaskRecordModel
import com.panda.pda.mes.user.UserViewModel
import com.squareup.moshi.Types
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
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
    private val userViewModel by activityViewModels<UserViewModel>()

    private var selectedPerson = listOf<PersonModel>()

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
        viewModel.taskInfoData.observe(viewLifecycleOwner) { info ->
            val detail = info.detail
            viewBinding.apply {
                tvPlanCode.text = detail.workOrderCode
                tvTaskCode.text = detail.dispatchOrderCode
                tvTaskDesc.text = detail.dispatchOrderDesc
                tvProductCode.text = detail.productCode
                tvProductDesc.text = detail.productName
                tvTaskCount.text = detail.dispatchOrderNum.toString()
                tvReportNumber.text = detail.reportNum.toString()
                tilReportNum.minValue = 1
                tilReportNum.maxValue = detail.dispatchOrderNum - detail.reportNum
                llOperator.setOnClickListener {
                    navToPersonSelect()
                }
            }.btnConfirm.setOnClickListener {
                report(info)
            }
        }

        setFragmentResultListener(PersonSelectFragment.PERSON_SELECTED) { _, bundle ->

            selectedPerson =
                bundle.getGenericObjectString<List<PersonModel>>(
                    Types.newParameterizedType(
                        List::class.java,
                        PersonModel::class.java
                    )) ?: listOf()

            viewBinding.tvSelectedOperator.text = selectedPerson.joinToString { it.userName }
        }
        val currentUser = userViewModel.loginData.value?.userInfo ?: return
        selectedPerson = listOf(PersonModel(currentUser.id, -1, listOf(), -1, -1, "", "", currentUser.userName, ""))
        viewBinding.tvSelectedOperator.text = selectedPerson.joinToString { it.userName }
    }

    private fun navToPersonSelect() {
        navController.navigate(R.id.personSelectFragment,
            Bundle().apply {
                putGenericObjectString(selectedPerson, Types.newParameterizedType(
                    List::class.java,
                    PersonModel::class.java
                ))
            })
    }

    private fun report(info: TaskInfoModel) {
//        val remark = viewBinding.etRemark.text.toString()
//        if (remark.isEmpty()) {
//            toast(R.string.remark_empty_message)
//        }
        if (selectedPerson.isEmpty()) {
            toast(getString(R.string.operator_not_selected_message))
        }
        val request = TaskReportRequest(info.detail.id,
            viewBinding.etReportNum.text.toString().toInt(),
            viewBinding.etRemark.text.toString(),
            photoAdapter.getDataSource(),
            selectedPerson.map { it.id }
        )
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

        WebClient.request(CommonApi::class.java)
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
        const val IMAGE_TYPE = "jpg"
    }
}