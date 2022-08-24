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
import com.panda.pda.mes.operation.bps.data.model.MainPlanDetailModel
import com.panda.pda.mes.operation.bps.data.model.MainPlanReportItem
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

    private var operatorSelectPosition: Int = -1
    private lateinit var photoAdapter: TaskReportInputPhotoAdapter
    private lateinit var operatorAdapter: CommonViewBindingAdapter<ItemMainPlanOperatorListBinding, MainPlanReportItem>
    private val viewBinding by viewBinding<FragmentMainPlanReportInputBinding>()
    private val userViewModel by activityViewModels<UserViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPhotoAdapter()
        setupOperatorAdapter()
        setupOperatorSelectResult()
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }

        viewBinding.btnAddOperator.setOnClickListener {
            val currentUser = userViewModel.loginData.value?.userInfo ?: return@setOnClickListener
            val newOperators = listOf(PersonModel(currentUser.id,
                -1,
                listOf(),
                -1,
                -1,
                "",
                "",
                currentUser.userName,
                ""))

            addOperators(MainPlanReportItem(1, 1, listOf()).also {
                it.selectedPerson = newOperators
            })
        }
        val mainPlanDetail = arguments?.getStringObject<MainPlanDetailModel>() ?: return
        viewBinding.apply {
            tvMainPlanCode.text = mainPlanDetail.planNo
            tvProductCode.text = mainPlanDetail.productCode
            tvProductDesc.text = mainPlanDetail.productName
            tvProductModel.text = mainPlanDetail.productModel
            tvMainPlanStatus.text = CommonParameters.getDesc(DataParamType.BPS_PLAN_STATUS, mainPlanDetail.planStatus)
            tvMainPlanNum.text = mainPlanDetail.planNumber.toString()
            tvWorkOrderCode.text = mainPlanDetail.workOrderCode
        }

    }

    private fun setupOperatorAdapter() {
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
                    etReportNum.setText(data.reportNumber.toString())
                    etReportTime.setText(data.reportTime.toString())
                    etReportNum.doAfterTextChanged {
                        data.reportNumber = it.toString().toIntOrNull() ?: 0
                    }
                    etReportTime.doAfterTextChanged {
                        data.reportTime = it.toString().toIntOrNull() ?: 0
                    }
                    llOperator.setOnClickListener {
                        navToPersonSelect(data.selectedPerson, holder.bindingAdapterPosition)
                    }

                    btnAction.setOnClickListener {
                        removeOperatorItem(holder.bindingAdapterPosition)
                    }
                }
            }
        }
        viewBinding.rvOperatorList.adapter = operatorAdapter
    }

    override fun onResume() {
        super.onResume()
        operatorAdapter.notifyDataSetChanged()
    }

    private fun removeOperatorItem(position: Int) {
        operatorAdapter.dataSource.removeAt(position)
        operatorAdapter.notifyItemRemoved(position)
    }

    private fun addOperators(data: MainPlanReportItem) {
        operatorAdapter.dataSource.add(0, data)
        operatorAdapter.notifyItemInserted(0)
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

    private fun setupPhotoAdapter() {
        viewBinding.rvPhotoList.adapter = TaskReportInputPhotoAdapter()
            .also {
                it.onTakePhotoAction = { takePhoto() }
                photoAdapter = it
            }
    }

    private fun takePhoto() {

        PictureSelector.create(this)
            .openGallery(SelectMimeType.ofImage())
            .setImageEngine(CoilEngine())
            .setMaxSelectNum(3 - photoAdapter.getDataSource().size)
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
                photoAdapter.getDataSource().add(it)
                photoAdapter.notifyDataSetChanged()
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

            val item = operatorAdapter.dataSource.getOrNull(operatorSelectPosition)
                ?: return@setFragmentResultListener
            item.selectedPerson = newSelectedPerson
            operatorAdapter.notifyItemChanged(operatorSelectPosition)
        }

    }

    companion object {
        const val IMAGE_TYPE = "jpg"
    }
}