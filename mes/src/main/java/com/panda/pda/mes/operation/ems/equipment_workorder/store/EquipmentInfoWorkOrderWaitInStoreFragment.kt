package com.panda.pda.mes.operation.ems.equipment_workorder.store

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.mes.BuildConfig
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.extension.putGenericObjectString
import com.panda.pda.mes.base.extension.toast
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.data.CommonApi
import com.panda.pda.mes.common.data.MesStringUtils
import com.panda.pda.mes.databinding.FragmentEquipmentWorkorderWaitinstoreBinding
import com.panda.pda.mes.operation.ems.adapter.EquipmentInputPhotoAdapter
import com.panda.pda.mes.operation.ems.data.EquipmentApi
import com.panda.pda.mes.operation.ems.data.model.EquipmentProductChooseModel
import com.panda.pda.mes.operation.ems.data.model.WorkOrderInStoreSubmitRequest
import com.squareup.moshi.Types
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File

/**
 * 确认入库  -->入库完成
 */
class EquipmentInfoWorkOrderWaitInStoreFragment :
    BaseFragment(R.layout.fragment_equipment_workorder_waitinstore) {
    private val ngReasonAdapter by lazy { EquipmentProductChooseListFragment.getNgReasonAdapter() }
    private val viewBinding by viewBinding<FragmentEquipmentWorkorderWaitinstoreBinding>()
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


    //人员列表，已选也包含未选
    var selecteProductList: MutableList<EquipmentProductChooseModel> =
        mutableListOf()

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
        setupPhotoAdapter()
        facilityType = arguments?.getString(FACILITYTYPE).toString()
        facilityDesc = arguments?.getString(FACILITYDESC).toString()
        facilityModel = arguments?.getString(FACILITYMODEL).toString()
        workOrderId = arguments?.getString(WORKORDERID).toString()
        viewBinding.tvDeviceTitle.text = "${
            if (facilityType == "1") {
                "设备"
            } else {
                "模具"
            }
        }-${facilityDesc}-${facilityModel}"
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }
        viewBinding.apply {
            topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }
            llWorkorderProductChoose.setOnClickListener {
                //选择产品
                navToProductSelect()
            }
            btnConfirm.setOnClickListener {
                submit()
            }
        }
        setFragmentResultListener(EquipmentProductChooseListFragment.REQUEST_KEY) { requestKey, bundle ->
            if (requestKey == EquipmentProductChooseListFragment.REQUEST_KEY) {
                selecteProductList = ngReasonAdapter.fromJson(
                    bundle.getString(EquipmentProductChooseListFragment.NG_REASON_ARG_KEY, "")
                )?.toMutableList() ?: mutableListOf()
                if (selecteProductList.isNotEmpty()) {
                    viewBinding.tvProductChoose.text = selecteProductList.joinToString(";") {
                        it.productName
                    }
                }
            }
        }
    }

    /**
     *工单提交
     */
    private fun submit() {
        val remark = viewBinding.etRemark.text.toString()
        val reasons = selecteProductList.filter { it.isChecked }
        if (reasons.isEmpty()) {
            toast("请选择产品")
            return
        }
        var selecteProductIdList: MutableList<Int> =
            mutableListOf()
        reasons.forEach {
            selecteProductIdList.add(it.id)
        }
        if (viewBinding.etProductTimes.text.toString().trim().isEmpty()) {
            toast("请输入生产次数")
            return
        }
        if (remark.isEmpty()) {
            toast(R.string.remark_empty_message)
            return
        }
        val request = WorkOrderInStoreSubmitRequest(workOrderId,
            selecteProductIdList,
            MesStringUtils.stringToInt(viewBinding.etProductTimes.text.toString().trim()),
            remark,
            photoAdapter.getDataSource())
        WebClient.request(EquipmentApi::class.java)
            .pdaEmsInSotreSubmitPost(request)
            .bindToFragment()
            .subscribe({
                toast("提交入库成功")
                navBackListener.invoke(requireView())
            }, {})
    }

    //跳转至产品选择列表
    private fun navToProductSelect() {
//        WebClient.request(QualityApi::class.java)
//            .pdaQmsQualitySubTaskGetBadnessListGet(subTaskDetailModel.id)
//            .bindToFragment()
//            .subscribe({
//                if (it.dataList.isEmpty()) {
//                    toast("请配置不良原因。")
//                } else {
//                    val ngReasons = ngReasonAdapter.toJson(it.dataList)
//                    navController.navigate(
//                        R.id.ngReasonFragment,
//                        Bundle().apply { putString(NgReasonFragment.NG_REASON_ARG_KEY, ngReasons) }
//                    )
//                }
//            }, {})
        navController.navigate(
            R.id.equipmentProductChooseListFragment,
            Bundle().apply {
                putGenericObjectString(
                    selecteProductList,
                    Types.newParameterizedType(
                        List::class.java,
                        EquipmentProductChooseModel::class.java
                    ), EquipmentProductChooseListFragment.NG_REASON_ARG_KEY
                )
            }
        )

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