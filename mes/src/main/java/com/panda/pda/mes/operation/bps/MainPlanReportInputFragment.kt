package com.panda.pda.mes.operation.bps

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import by.kirich1409.viewbindingdelegate.viewBinding
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.extension.getGenericObjectString
import com.panda.pda.mes.base.extension.getStringObject
import com.panda.pda.mes.base.extension.putGenericObjectString
import com.panda.pda.mes.base.extension.toast
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.base.retrofit.onMainThread
import com.panda.pda.mes.common.CoilEngine
import com.panda.pda.mes.common.PersonSelectFragment
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.common.data.CommonApi
import com.panda.pda.mes.common.data.CommonParameters
import com.panda.pda.mes.common.data.DataParamType
import com.panda.pda.mes.common.data.model.PersonModel
import com.panda.pda.mes.databinding.FragmentMainPlanReportInputBinding
import com.panda.pda.mes.databinding.ItemMainPlanOperatorListBinding
import com.panda.pda.mes.operation.bps.data.MainPlanApi
import com.panda.pda.mes.operation.bps.data.model.EquipmentModel
import com.panda.pda.mes.operation.bps.data.model.MainPlanDetailModel
import com.panda.pda.mes.operation.bps.data.model.MainPlanReportItem
import com.panda.pda.mes.operation.bps.data.model.MainPlanReportRequest
import com.panda.pda.mes.operation.fms.mission.TaskReportInputPhotoAdapter
import com.panda.pda.mes.user.UserViewModel
import com.squareup.moshi.Types
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File

/**
 * created by AnJiwei 2022/8/8
 */
class MainPlanReportInputFragment : BaseFragment(R.layout.fragment_main_plan_report_input) {

    private var equipmentSelectPosition: Int = -1
    private var operatorSelectPosition: Int = -1
    private var photoAdapter: TaskReportInputPhotoAdapter? = null
    private var operatorAdapter: CommonViewBindingAdapter<ItemMainPlanOperatorListBinding, MainPlanReportItem>? =
        null
    private val viewBinding by viewBinding<FragmentMainPlanReportInputBinding>()
    private val userViewModel by activityViewModels<UserViewModel>()
    private var mainPlanDetail: MainPlanDetailModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPhotoAdapter()
        setupOperatorAdapter()
        setupOperatorSelectResult()
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }

        viewBinding.btnAddOperator.setOnClickListener {
            val item: MainPlanReportItem
            if (operatorAdapter!!.dataSource.isEmpty()) {
                val currentUser =
                    userViewModel.loginData.value?.userInfo ?: return@setOnClickListener
                val newOperators = listOf(PersonModel(currentUser.id,
                    -1,
                    listOf(),
                    -1,
                    -1,
                    "",
                    "",
                    currentUser.userName,
                    ""))

                item = MainPlanReportItem(null, null, null, listOf(), listOf()).also {
                    it.selectedPerson = newOperators
                }
            } else {
                item = MainPlanReportItem(null, null, null, listOf(), listOf()).also {
                    it.selectedPerson = listOf()
                }
            }
            addOperators(item)

        }
        val detail = arguments?.getStringObject<MainPlanDetailModel>() ?: return
        mainPlanDetail = detail
        viewBinding.apply {
            tvMainPlanCode.text = detail.planNo
            tvProductCode.text = detail.productCode
            tvProductDesc.text = detail.productName
            tvProductModel.text = detail.productModel
            tvMainPlanStatus.text =
                CommonParameters.getDesc(DataParamType.BPS_PLAN_STATUS, detail.planStatus)
            tvMainPlanNum.text = detail.planNumber.toString()
            tvWorkOrderCode.text = detail.workNo
        }

        viewBinding.btnConfirm.setOnClickListener {
            report()
        }

    }

    private fun report() {
        try {
            val detail = mainPlanDetail ?: throw Exception("主计划详情异常")
            val photos = photoAdapter?.getDataSource() ?: return
            val resources = operatorAdapter?.dataSource ?: throw Exception("请添加资源")
            if (resources.isEmpty()) {
                throw Exception("请添加资源")
            }
            resources.forEach {
                if (it.reportNumber == null || it.reportNumber == 0) {
                    throw Exception("工序报工数量不能为0")
                }
                if (it.reportNumber !in 1..detail.planNumber) {
                    throw Exception("工序报工数量不能大于主计划数量")
                }
                it.jockeyList = it.selectedPerson.map { user -> user.id }
                it.equipmentList = it.selectedEquipment.map { eqp -> eqp.id }
            }
            val request = MainPlanReportRequest(
                detail.id,
                viewBinding.etRemark.text.toString(),
                photos,
                resources
            )

            WebClient.request(MainPlanApi::class.java)
                .mainPlanReportConfirmPost(request)
                .bindToFragment()
                .subscribe({
                    toast(getString(R.string.main_plan_report_success_message))
                    navController.popBackStack()
                }, {})
        } catch (err: Throwable) {
            toast(err.message ?: getString(R.string.net_work_error))
        }

    }

    private fun setupOperatorAdapter() {
        if (operatorAdapter == null) {
            operatorAdapter = object :
                CommonViewBindingAdapter<ItemMainPlanOperatorListBinding, MainPlanReportItem>() {
                override fun createBinding(parent: ViewGroup): ItemMainPlanOperatorListBinding {
                    return ItemMainPlanOperatorListBinding.inflate(LayoutInflater.from(parent.context),
                        parent,
                        false)
                }

                override fun onBindViewHolderWithData(
                    holder: ViewBindingHolder,
                    data: MainPlanReportItem,
                    position: Int,
                ) {
                    holder.setIsRecyclable(false)
                    holder.itemViewBinding.apply {

                        tvSelectedOperator.text = data.selectedPerson.joinToString { it.userName }
                        tvSelectedEquipment.text = data.selectedEquipment.joinToString { it.equipmentDesc }

                        etReportNum.setText(data.reportNumber?.toString() ?: "")
                        etOperatorReportTime.setText(data.reportTime?.toString() ?: "")
                        etEquipmentReportTime.setText(data.equipmentTime?.toString() ?: "")

                        etReportNum.doAfterTextChanged {
                            data.reportNumber = it.toString().toIntOrNull()
                        }
                        etOperatorReportTime.doAfterTextChanged {
                            data.reportTime = it.toString().toIntOrNull()
                        }
                        etEquipmentReportTime.doAfterTextChanged {
                            data.equipmentTime = it.toString().toIntOrNull()
                        }
                        llOperator.setOnClickListener {
                            navToPersonSelect(data.selectedPerson, holder.bindingAdapterPosition)
                        }

                        llEquipment.setOnClickListener{
                            navToEquipmentSelect(data.selectedEquipment, holder.bindingAdapterPosition)
                        }
                        btnAction.setOnClickListener {
                            removeOperatorItem(data)
                        }
                    }
                }
            }
        }

        viewBinding.rvOperatorList.adapter = operatorAdapter
    }

    private fun removeOperatorItem(data: MainPlanReportItem) {
        val index = operatorAdapter!!.dataSource.indexOf(data)
        operatorAdapter!!.dataSource.removeAt(index)
        Timber.e(index.toString())
        operatorAdapter!!.notifyDataSetChanged()
    }

    private fun addOperators(data: MainPlanReportItem) {
        operatorAdapter!!.dataSource.add(data)
        operatorAdapter!!.notifyDataSetChanged()
    }

    private fun navToPersonSelect(operators: List<PersonModel>, bindingAdapterPosition: Int) {
        operatorSelectPosition = bindingAdapterPosition
        navController.navigate(R.id.personSelectFragment,
            Bundle().apply {
                putGenericObjectString(operators, Types.newParameterizedType(
                    List::class.java,
                    PersonModel::class.java
                ))
            })
    }

    private fun navToEquipmentSelect(
        selectedEquipment: List<EquipmentModel>,
        bindingAdapterPosition: Int,
    ) {
        equipmentSelectPosition = bindingAdapterPosition
        navController.navigate(R.id.equipmentSelectFragment,
            Bundle().apply {
                putGenericObjectString(selectedEquipment, Types.newParameterizedType(
                    List::class.java,
                    EquipmentModel::class.java
                ))
            })
    }

    private fun setupPhotoAdapter() {
        if (photoAdapter == null) {
            photoAdapter = TaskReportInputPhotoAdapter().also {
                it.onTakePhotoAction = { takePhoto() }
            }
        }
        viewBinding.rvPhotoList.adapter = photoAdapter
    }

    private fun takePhoto() {

        PictureSelector.create(this)
            .openGallery(SelectMimeType.ofImage())
            .setImageEngine(CoilEngine())
            .setMaxSelectNum(3 - photoAdapter!!.getDataSource().size)
            .forResult(object : OnResultCallbackListener<LocalMedia?> {
                override fun onResult(result: ArrayList<LocalMedia?>?) {
                    updatePhoto(result)
                }

                override fun onCancel() {}
            })
    }

    private fun updatePhoto(media: java.util.ArrayList<LocalMedia?>?) {
        media?.forEach {
            if (it != null) {
                updatePhoto(it)
            }
        }
    }

    private fun updatePhoto(media: LocalMedia) {

        val photo = File(media.realPath)
        WebClient.request(CommonApi::class.java)
            .pdaCommonUploadFilePost(MultipartBody.Part.createFormData(
                "file",
                media.fileName,
                photo.asRequestBody("image/${IMAGE_TYPE}".toMediaType())))
            .onMainThread()
            .catchError()
            .subscribe({
                it.fileLocalUri = Uri.fromFile(photo)
                photoAdapter!!.getDataSource().add(it)
                photoAdapter!!.notifyDataSetChanged()
                Timber.d("url: ${it.fileUrl}")
            }, {})
    }

    private fun setupOperatorSelectResult() {
        setFragmentResultListener(PersonSelectFragment.PERSON_SELECTED) { result, bundle ->
            val newSelectedPerson =
                bundle.getGenericObjectString<List<PersonModel>>(
                    Types.newParameterizedType(
                        List::class.java,
                        PersonModel::class.java
                    )) ?: return@setFragmentResultListener

            val item = operatorAdapter!!.dataSource.getOrNull(operatorSelectPosition)
                ?: return@setFragmentResultListener
            item.selectedPerson = newSelectedPerson
            operatorAdapter!!.notifyItemChanged(operatorSelectPosition)
        }

        setFragmentResultListener(EquipmentSelectFragment.EQUIPMENT_SELECTED) { result, bundle ->
            val newSelectedEquipment =
                bundle.getGenericObjectString<List<EquipmentModel>>(
                    Types.newParameterizedType(
                        List::class.java,
                        EquipmentModel::class.java
                    )) ?: return@setFragmentResultListener

            val item = operatorAdapter!!.dataSource.getOrNull(equipmentSelectPosition)
                ?: return@setFragmentResultListener
            item.selectedEquipment = newSelectedEquipment
            operatorAdapter!!.notifyItemChanged(operatorSelectPosition)
        }
    }

    companion object {
        const val IMAGE_TYPE = "jpg"
    }
}