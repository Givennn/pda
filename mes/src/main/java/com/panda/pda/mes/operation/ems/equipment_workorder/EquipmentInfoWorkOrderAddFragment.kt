package com.panda.pda.mes.operation.ems.equipment_workorder

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.mes.BuildConfig
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.extension.getStringObject
import com.panda.pda.mes.base.extension.toast
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.WheelPickerDialogFragment
import com.panda.pda.mes.common.data.CommonApi
import com.panda.pda.mes.common.data.CommonParameters
import com.panda.pda.mes.common.data.DataParamType
import com.panda.pda.mes.common.data.MesStringUtils
import com.panda.pda.mes.databinding.FragmentEquipmentWorkorderAddBinding
import com.panda.pda.mes.operation.ems.adapter.EquipmentInputPhotoAdapter
import com.panda.pda.mes.operation.ems.data.EquipmentApi
import com.panda.pda.mes.operation.ems.data.model.EquipmentFunctionTypeChooseModel
import com.panda.pda.mes.operation.ems.data.model.EquipmentInfoDeviceModel
import com.panda.pda.mes.operation.ems.data.model.EquipmentSupplierChooseModel
import com.panda.pda.mes.operation.ems.data.model.WorkOrderAddRequest
import com.panda.pda.mes.operation.ems.equipment_workorder.store.EquipmentDeviceChooseListFragment
import com.panda.pda.mes.operation.ems.equipment_workorder.store.EquipmentSupplierChooseListFragment
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File

/**
 * 工单填报（维保-->分配、出库--->待出库提交）
 */
class EquipmentInfoWorkOrderAddFragment : BaseFragment(R.layout.fragment_equipment_workorder_add) {
    private val viewBinding by viewBinding<FragmentEquipmentWorkorderAddBinding>()
    private lateinit var photoAdapter: EquipmentInputPhotoAdapter
    private lateinit var tmpFile: File
    private var latestTmpUri: Uri? = null

    //图片列表
    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    updatePhoto(uri)
                }
            }
        }

    //功能类型选择列表，
    private var typeChooseList = mutableListOf<EquipmentFunctionTypeChooseModel>()

    //功能类型列表对应的名称列表
    private var typeChooseNameList = mutableListOf<String>()

    //功能类型列表弹窗
    private val typeChooseDialog by lazy {
        WheelPickerDialogFragment().also {
            it.pickerData = typeChooseNameList
        }
    }

    //根布局
    var rootView: View? = null

    //当前选中类型信息
    var currentFunctionType: EquipmentFunctionTypeChooseModel? = null

    //当前选中设备信息
    var currentDeviceChoose: EquipmentInfoDeviceModel? = null

    //当前选中厂商信息
    var currentSupplierChoose: EquipmentSupplierChooseModel? = null

    //设备id
    var facilityId = ""
    var facilityType = ""
    var deviceModel: EquipmentInfoDeviceModel? = null

    //保养权限
    var maintainAddPermission = false

    //维修权限
    var repairAddPermission = false

    //出库权限
    var stockAddPerssion = false

    //保存状态
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        if (rootView == null) {
            rootView = super.onCreateView(inflater, container, savedInstanceState)
        }
        deviceModel = arguments?.getStringObject<EquipmentInfoDeviceModel>()
        deviceModel?.let {
            facilityId = deviceModel!!.facilityId.toString()
            facilityType = deviceModel!!.facilityType
            maintainAddPermission = deviceModel!!.maintainAddPermission
            repairAddPermission = deviceModel!!.repairAddPermission
            stockAddPerssion = deviceModel!!.stockAddPerssion
            getFunctionTypeList()
        }

        return rootView
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //设置图片适配器
        setupPhotoAdapter()
        //返回按钮点击事件
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }
        viewBinding.btnConfirm.setOnClickListener {
            //点击提交工单
            if (facilityId != null) {
                submit(facilityId, facilityType)
            }
        }
        viewBinding.tvDeviceTitle.text = "${
            if (deviceModel!!.facilityType == "1") {
                "设备"
            } else {
                "模具"
            }
        }-${deviceModel!!.facilityDesc}-${deviceModel!!.facilityModel}"
        viewBinding.llWorkorderSupplier.setOnClickListener {
            navController.navigate(
                R.id.equipmentSupplierChooseListFragment,
            )
        }
        viewBinding.llWorkorderDevice.setOnClickListener {
//            navController.navigate(
//                R.id.equipmentDeviceChooseListFragment,
//            )
            navController.navigate(
                R.id.equipmentDeviceChooseListFragment,
            )
        }
        viewBinding.llWorkorderType.setOnClickListener {
            if (typeChooseNameList.isNotEmpty()) {
                showTypeSelectDialog(viewBinding.tvType, typeChooseDialog)
            } else {
                toast("暂无可填报类型")
            }
        }
        setFragmentResultListener(EquipmentSupplierChooseListFragment.REQUEST_KEY) { requestKey, bundle ->
            if (requestKey == EquipmentSupplierChooseListFragment.REQUEST_KEY) {
                val supplierData =
                    bundle.getStringObject<EquipmentSupplierChooseModel>(
                        EquipmentSupplierChooseListFragment.NG_REASON_ARG_KEY)
                if (supplierData != null) {
                    viewBinding.tvSupplier.text = supplierData.supplierDesc
                    currentSupplierChoose = supplierData
                }
            }
        }
        setFragmentResultListener(EquipmentDeviceChooseListFragment.REQUEST_KEY) { requestKey, bundle ->
            if (requestKey == EquipmentDeviceChooseListFragment.REQUEST_KEY) {
                val supplierData =
                    bundle.getStringObject<EquipmentInfoDeviceModel>(
                        EquipmentDeviceChooseListFragment.NG_REASON_ARG_KEY)
                if (supplierData != null) {
                    viewBinding.tvDevice.text = supplierData.facilityDesc
                    currentDeviceChoose = supplierData
                }
            }
        }
    }

    /**
     *工单提交
     */
    private fun submit(facilityId: String, facilityType: String) {
        if (currentFunctionType == null) {
            toast("请选择类型")
            return
        }
        var functionType = currentFunctionType!!.functionType
        var functionId = currentFunctionType!!.id
        var relatedEquipId = ""
        var relateSupplierId = ""
        if (currentFunctionType!!.functionRealAttr == "1") {
            if (currentDeviceChoose == null) {
                toast("请选择设备")
                return
            }
            relatedEquipId = currentDeviceChoose!!.facilityId.toString()
        } else if (currentFunctionType!!.functionRealAttr == "2") {
            if (currentSupplierChoose == null) {
                toast("请选择厂商")
                return
            }
            relateSupplierId = currentSupplierChoose!!.supplierCode
        }
        val remark = viewBinding.etRemark.text.toString()
        if (remark.isEmpty()) {
            toast(R.string.remark_empty_message)
            return
        }
        val request = WorkOrderAddRequest(MesStringUtils.stringToInt(facilityId), facilityType,
            functionType,
            functionId,
            relatedEquipId,
            relateSupplierId,
            remark,
            photoAdapter.getDataSource())
        WebClient.request(EquipmentApi::class.java)
            .pdaEmsTaskSubmitConfirmPost(request)
            .bindToFragment()
            .subscribe({
                toast("工单填报成功")
                navBackListener.invoke(requireView())
            }, {})
    }

    /**
     *工单提交
     */
    private fun submitTest(facilityId: String, facilityType: String) {
        currentFunctionType = EquipmentFunctionTypeChooseModel("4", "生产出库", "3", "2", "1")
        if (currentFunctionType == null) {
            toast("请选择类型")
            return
        }
        var functionType = currentFunctionType!!.functionType
        var functionId = currentFunctionType!!.id
        var relatedEquipId = ""
        var relateSupplierId = ""
        if (currentFunctionType!!.functionRealAttr == "1") {
            if (currentDeviceChoose == null) {
                toast("请选择设备")
                return
            }
            relatedEquipId = currentDeviceChoose!!.facilityId.toString()
        } else if (currentFunctionType!!.functionRealAttr == "2") {
            if (currentSupplierChoose == null) {
                toast("请选择厂商")
                return
            }
            relateSupplierId = currentSupplierChoose!!.supplierCode
        }
        val remark = viewBinding.etRemark.text.toString()
        if (remark.isEmpty()) {
            toast(R.string.remark_empty_message)
            return
        }
        val request = WorkOrderAddRequest(MesStringUtils.stringToInt(facilityId), facilityType,
            functionType,
            functionId,
            relatedEquipId,
            relateSupplierId,
            remark,
            photoAdapter.getDataSource())
        WebClient.request(EquipmentApi::class.java)
            .pdaEmsTaskSubmitConfirmPost(request)
            .bindToFragment()
            .subscribe({
                toast("工单填报成功")
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

    //筛选类型列表，将有权限的，且启用状态下的数据留下来

    private fun showTypeSelectDialog(
        tvSelectValue: TextView,
        dialog: WheelPickerDialogFragment,
    ) {
        dialog.setConfirmButton { result ->
            result!!.first
            tvSelectValue.text = result.first
            currentSupplierChoose = null
            currentDeviceChoose = null
            currentFunctionType = null
            viewBinding.tvDevice.text = ""
            viewBinding.tvSupplier.text = ""
            //列表索引
            var position = result.second
            //当前选择的类型
            currentFunctionType = typeChooseList[position]
            if (currentFunctionType!!.functionRealAttr == "1") {
                //关联设备选择展示
                viewBinding.llWorkorderDevice.visibility = View.VISIBLE
                viewBinding.llWorkorderSupplier.visibility = View.GONE
            } else if (currentFunctionType!!.functionRealAttr == "2") {
                //关联厂商选择展示
                viewBinding.llWorkorderSupplier.visibility = View.VISIBLE
                viewBinding.llWorkorderDevice.visibility = View.GONE
            } else {
                viewBinding.llWorkorderDevice.visibility = View.GONE
                viewBinding.llWorkorderSupplier.visibility = View.GONE
            }


        }.setCancelButton { dialogInterface, i ->
            Timber.e("dialog关闭")
        }.show(parentFragmentManager, TAG)
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

    //获取类型选择列表,根据上游页面权限进行筛选
    private fun getFunctionTypeList() {
        WebClient.request(EquipmentApi::class.java)
            .pdaFunctionTypeListGet()
            .subscribe({
                //顶部列表
                var tempList = it.dataList.toMutableList()
                //筛选维修保养权限type
                //根据维修保养出库各自权限去过滤可以选择的类型
                typeChooseList =
                    tempList.filter { it.status == "1" && ((maintainAddPermission && it.functionType == "2") || (repairAddPermission && it.functionType == "1") || (stockAddPerssion && it.functionType == "3")) }
                        .toMutableList()
                typeChooseNameList.clear()
                typeChooseList.forEach { item ->
                    var functionTypeName = CommonParameters.getDesc(DataParamType.FUNCTION_TYPE,
                        MesStringUtils.stringToInt(item.functionType))
                    typeChooseNameList.add("${functionTypeName}-${item.functionName}")
                }
            }, {
            })

    }

    companion object {
        const val IMAGE_TYPE = "jpg"
        const val FACILITYID = "facilityId"

        //维修权限
        const val MAINTAINADDPERMISSION = "maintainAddPermission"

        //保养权限
        const val REPAIRADDPERMISSION = "repairAddPermission"

        //出库权限
        const val STOCKADDPERSSION = "stockAddPerssion"
    }
}