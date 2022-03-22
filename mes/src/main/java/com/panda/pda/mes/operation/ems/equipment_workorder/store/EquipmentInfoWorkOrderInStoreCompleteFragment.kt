package com.panda.pda.mes.operation.ems.equipment_workorder.store

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
import com.panda.pda.mes.base.extension.getStringObject
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
import com.panda.pda.mes.operation.ems.data.model.WorkOrderInStoreConfirmRequest
import com.panda.pda.mes.operation.ems.data.model.WorkOrderInStoreSubmitRequest
import com.panda.pda.mes.operation.qms.QualityViewModel
import com.panda.pda.mes.operation.qms.data.model.QualityProblemRecordDetailModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File

/**
 * 待入库  -->确认入库
 */
class EquipmentInfoWorkOrderInStoreCompleteFragment :
    BaseFragment(R.layout.fragment_equipment_workorder_instore_complete) {

    private val viewBinding by viewBinding<FragmentEquipmentWorkorderInstoreCompleteBinding>()
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

    //是否完好
    var isIntact: Boolean = true

    //工单id
    var workOrderId: String = ""
    //设备type 1设备  2模具
    var facilityType: String = ""

    //设备名称
    var facilityDesc: String = ""

    //设备型号代号
    var facilityModel: String = ""
    var locationName: String = ""
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        facilityType = arguments?.getString(FACILITYTYPE).toString()
        facilityDesc = arguments?.getString(FACILITYDESC).toString()
        facilityModel = arguments?.getString(FACILITYMODEL).toString()
        locationName = arguments?.getString(LOCATIONNAME).toString()
        viewBinding.tvDeviceTitle.text = "${
            if (facilityType == "1") {
                "设备"
            } else {
                "模具"
            }
        }-${facilityDesc}-${facilityModel}"
        workOrderId = arguments?.getString(WORKORDERID).toString()
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }
        setupPhotoAdapter()
        viewBinding.apply {
            tvProductLocation.text=locationName
            llStatusGood.setOnClickListener {
                //完好点击
                ivStatusGood.setImageResource(R.drawable.icon_equipment_check_selected)
                ivStatusBad.setImageResource(R.drawable.icon_equipment_check_unselect)
                isIntact = true
            }
            llStatusBad.setOnClickListener {
                //不完好点击
                ivStatusGood.setImageResource(R.drawable.icon_equipment_check_unselect)
                ivStatusBad.setImageResource(R.drawable.icon_equipment_check_selected)
                isIntact = false
            }
            //初始化状态，默认是完好
            if (isIntact) {
                ivStatusGood.setImageResource(R.drawable.icon_equipment_check_selected)
                ivStatusBad.setImageResource(R.drawable.icon_equipment_check_unselect)
            } else {
                ivStatusGood.setImageResource(R.drawable.icon_equipment_check_unselect)
                ivStatusBad.setImageResource(R.drawable.icon_equipment_check_selected)
            }
            btnConfirm.setOnClickListener{
                submit()
            }
        }
    }

    /**
     *工单提交
     */
    private fun submit() {
        val remark = viewBinding.etRemark.text.toString()
        if (viewBinding.etProductPoint.text.toString().trim().isEmpty()) {
            toast("请输入坐标")
            return
        }
        if (remark.isEmpty()) {
            toast(R.string.remark_empty_message)
            return
        }
        val request = WorkOrderInStoreConfirmRequest(workOrderId,
            if (isIntact) {
                1
            } else {
                2
            },
            viewBinding.etProductPoint.text.toString().trim(),
            remark,
            photoAdapter.getDataSource())
        WebClient.request(EquipmentApi::class.java)
            .pdaEmsInSotreConfirmPost(request)
            .bindToFragment()
            .subscribe({
                toast("提交入库成功")
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
        const val LOCATIONNAME = "locationName"
    }
}