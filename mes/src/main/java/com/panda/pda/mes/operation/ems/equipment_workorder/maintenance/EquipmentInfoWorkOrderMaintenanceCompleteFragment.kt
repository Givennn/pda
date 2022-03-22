package com.panda.pda.mes.operation.ems.equipment_workorder.maintenance

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.api.load
import com.panda.pda.mes.BuildConfig
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.extension.toast
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.ModelPropertyCreator
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.common.data.CommonApi
import com.panda.pda.mes.common.data.model.FileInfoModel
import com.panda.pda.mes.databinding.*
import com.panda.pda.mes.operation.ems.adapter.EquipmentInputPhotoAdapter
import com.panda.pda.mes.operation.ems.data.EquipmentApi
import com.panda.pda.mes.operation.ems.data.model.EquipmentInfoDeviceModel
import com.panda.pda.mes.operation.ems.data.model.WorkOrderWBConfirmRequest
import com.panda.pda.mes.operation.ems.data.model.WorkOrderWangongRequest
import com.panda.pda.mes.operation.qms.QualityViewModel
import com.panda.pda.mes.operation.qms.data.model.QualityProblemRecordDetailModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File

/**
 * 维保完工待确认  -->维保完成确认
 */
class EquipmentInfoWorkOrderMaintenanceCompleteFragment :
    BaseFragment(R.layout.fragment_equipment_workorder_complete) {
    //是否合格，1合格，2限度生产，3申请延期
    var result = 1
    private val viewBinding by viewBinding<FragmentEquipmentWorkorderCompleteBinding>()
    private lateinit var tmpFile: File
    private lateinit var photoAdapter: EquipmentInputPhotoAdapter
    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    updatePhoto(uri)
                }
            }
        }
    private var latestTmpUri: Uri? = null

    //工单id
    var workOrderId: String = ""
    //设备type 1设备  2模具
    var facilityType: String = ""

    //设备名称
    var facilityDesc: String = ""

    //设备型号代号
    var facilityModel: String = ""
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        facilityType = arguments?.getString(FACILITYTYPE).toString()
        facilityDesc = arguments?.getString(FACILITYDESC).toString()
        facilityModel = arguments?.getString(FACILITYMODEL).toString()
        viewBinding.tvDeviceTitle.text = "${
            if (facilityType == "1") {
                "设备"
            } else {
                "模具"
            }
        }-${facilityDesc}-${facilityModel}"
        workOrderId = arguments?.getString(WORKORDERID).toString()
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }
        viewBinding.btnConfirm.setOnClickListener{
            submit()
        }
        setupPhotoAdapter()
        viewBinding.apply {
            when (result) {
                1 -> {
                    //合格
                    ivProcessTrue.setImageResource(R.drawable.icon_equipment_check_selected)
                    ivProcessFalse.setImageResource(R.drawable.icon_equipment_check_unselect)
                    ivProcessDelay.setImageResource(R.drawable.icon_equipment_check_unselect)
                }
                2 -> {
                    //限度生产
                    ivProcessTrue.setImageResource(R.drawable.icon_equipment_check_unselect)
                    ivProcessFalse.setImageResource(R.drawable.icon_equipment_check_selected)
                    ivProcessDelay.setImageResource(R.drawable.icon_equipment_check_unselect)
                }
                3 -> {
                    //申请延期
                    ivProcessTrue.setImageResource(R.drawable.icon_equipment_check_unselect)
                    ivProcessFalse.setImageResource(R.drawable.icon_equipment_check_unselect)
                    ivProcessDelay.setImageResource(R.drawable.icon_equipment_check_selected)
                }
            }
            llProcessTrue.setOnClickListener {
                //合格
                result = 1
                ivProcessTrue.setImageResource(R.drawable.icon_equipment_check_selected)
                ivProcessFalse.setImageResource(R.drawable.icon_equipment_check_unselect)
                ivProcessDelay.setImageResource(R.drawable.icon_equipment_check_unselect)
            }
            llProcessFalse.setOnClickListener {
                //限度生产
                result = 2
                ivProcessTrue.setImageResource(R.drawable.icon_equipment_check_unselect)
                ivProcessFalse.setImageResource(R.drawable.icon_equipment_check_selected)
                ivProcessDelay.setImageResource(R.drawable.icon_equipment_check_unselect)
            }
            llProcessDelay.setOnClickListener {
                //申请延期
                result = 3
                ivProcessTrue.setImageResource(R.drawable.icon_equipment_check_unselect)
                ivProcessFalse.setImageResource(R.drawable.icon_equipment_check_unselect)
                ivProcessDelay.setImageResource(R.drawable.icon_equipment_check_selected)
            }
        }
    }

    /**
     *工单提交
     */
    private fun submit() {
        val remark = viewBinding.etRemark.text.toString()
        if (remark.isEmpty()) {
            toast(R.string.remark_empty_message)
            return
        }
        val request = WorkOrderWBConfirmRequest(workOrderId,
            result,
            remark,
            photoAdapter.getDataSource())
        WebClient.request(EquipmentApi::class.java)
            .pdaEmsWBConfirmPost(request)
            .bindToFragment()
            .subscribe({
                toast("已确认")
                navBackListener.invoke(requireView())
            }, {})
    }

    private fun setupPhotoAdapter() {
        viewBinding.rvPicList.adapter = EquipmentInputPhotoAdapter()
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

    @SuppressLint("NotifyDataSetChanged")
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
        const val WORKORDERID = "workOrderId"
        const val FACILITYTYPE = "facilityType"
        //设备名称
        const val FACILITYDESC = "facilityDesc"
        //设备型号代号
        const val FACILITYMODEL = "facilityModel"
    }
}