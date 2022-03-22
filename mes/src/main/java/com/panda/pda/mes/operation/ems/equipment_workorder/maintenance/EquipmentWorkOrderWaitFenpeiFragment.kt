package com.panda.pda.mes.operation.ems.equipment_workorder.maintenance

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.extension.toast
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.data.MesStringUtils
import com.panda.pda.mes.databinding.FragmentEquipmentWorkorderWaitfenpeiBinding
import com.panda.pda.mes.operation.ems.data.EquipmentApi
import com.panda.pda.mes.operation.ems.data.model.EquipmentPersonChooseModel
import com.panda.pda.mes.operation.ems.data.model.WorkOrderFenpeiRequest
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

/**
 * 工单待分配->去分配
 */
class EquipmentWorkOrderWaitFenpeiFragment :
    BaseFragment(R.layout.fragment_equipment_workorder_waitfenpei) {
    private val viewBinding by viewBinding<FragmentEquipmentWorkorderWaitfenpeiBinding>()

    //是否提供样品
    var isSimple: Boolean = true
    var completeDate: Long = 0
    var completeTime: Long = 0
    private val datePicker by lazy {
        MaterialDatePicker.Builder.datePicker().setTitleText(R.string.time_pick).build()
//        MaterialDatePicker.Builder.datePicker().setTitleText(R.string.time_pick).build()
    }
    private val timePicker by lazy {
        MaterialTimePicker.Builder().setTitleText("时间选择").build()
//        MaterialTimePicker.Builder().setTitleText("时间选择").build()
    }

    //人员适配器
    private val ngReasonAdapter by lazy { EquipmentPersonChooseListFragment.getNgReasonAdapter() }

    //人员列表，已选也包含未选
    private var selectePersonList: MutableList<EquipmentPersonChooseModel> =
        mutableListOf()

    //工单id
    var workOrderId: String = ""
    var teamId: String = ""

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
        teamId = arguments?.getString(TEAMID).toString()
        viewBinding.apply {
            topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }
            llWorkorderCompleteDate.setOnClickListener {
                datePicker.show(parentFragmentManager, TAG)
            }
            llWorkorderCompleteTime.setOnClickListener {
                timePicker.show(parentFragmentManager, TAG);
            }
            llSampleYes.setOnClickListener {
                //提供样品点击
                ivSampleYes.setImageResource(R.drawable.icon_equipment_check_selected)
                ivSampleNo.setImageResource(R.drawable.icon_equipment_check_unselect)
                isSimple = true
            }
            llSampleNo.setOnClickListener {
                //不提供样品点击
                ivSampleYes.setImageResource(R.drawable.icon_equipment_check_unselect)
                ivSampleNo.setImageResource(R.drawable.icon_equipment_check_selected)
                isSimple = false
            }
            //初始化状态，默认是提供样品
            if (isSimple) {
                ivSampleYes.setImageResource(R.drawable.icon_equipment_check_selected)
                ivSampleNo.setImageResource(R.drawable.icon_equipment_check_unselect)
            } else {
                ivSampleYes.setImageResource(R.drawable.icon_equipment_check_unselect)
                ivSampleNo.setImageResource(R.drawable.icon_equipment_check_selected)
            }
            llWorkorderPerson.setOnClickListener {
                navToPersonSelect()
            }
            btnConfirm.setOnClickListener {
                submit()
            }

        }

        datePicker.addOnPositiveButtonClickListener {
            onTimePicked(it)
            //此处有日期的偏移量，需要注意相差8小时
            completeDate = it - 8 * 60 * 60 * 1000
        }
        timePicker.addOnPositiveButtonClickListener {
            viewBinding.tvCompleteTime.text = convertHMToTime(timePicker.hour, timePicker.minute)
            completeTime =
                (timePicker.hour * 60 * 60 * 1000 + timePicker.minute * 60 * 1000).toLong()
        }
        setFragmentResultListener(EquipmentPersonChooseListFragment.REQUEST_KEY) { requestKey, bundle ->
            if (requestKey == EquipmentPersonChooseListFragment.REQUEST_KEY) {
                val ngReasonsStr =
                    bundle.getString(EquipmentPersonChooseListFragment.NG_REASON_ARG_KEY, "")
                selectePersonList =
                    ngReasonAdapter.fromJson(ngReasonsStr)?.toMutableList() ?: mutableListOf()

                val reasons = selectePersonList.filter { it.isChecked }
                if (!reasons.isEmpty()) {
                    viewBinding.tvName.text = reasons?.joinToString(";") {
//                        it.realName
                        it.userName
                    }
                }
            }
        }

    }

    //跳转至人员选择列表
    private fun navToPersonSelect() {
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
            R.id.equipmentPersonChooseListFragment, Bundle().apply {
                putString(TEAMID,
                    teamId)
            }
        )

    }

    //日期转换并设置
    private fun onTimePicked(it: Long) {
        val time = convertLongToTime(it)
        viewBinding.tvCompleteDate.text = time
    }

    //时间转换日期
    private fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
        return format.format(date)
    }

    //时间转换日期
    private fun convertLongToCompleteTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return format.format(date)
    }

    //时分转化为整体时间
    private fun convertHMToTime(hour: Int, minute: Int): String {
        var hourStr = hour.toString()
        if (hourStr.length == 1) {
            hourStr = "0$hourStr"
        }
        var minuteStr = minute.toString()
        if (minuteStr.length == 1) {
            minuteStr = "0$minuteStr"
        }
        return "$hourStr:$minuteStr"
    }

    /**
     *工单分配
     */
    private fun submit() {
        var dateTime = completeDate + completeTime
        Timber.e("dateTime${convertLongToCompleteTime(dateTime)}")
        val remark = viewBinding.etRemark.text.toString().trim()
        val time = viewBinding.etTimecount.text.toString().trim()
        if (remark.isEmpty()) {
            toast(R.string.remark_empty_message)
            return
        }
        if (time.isEmpty()) {
            toast("请出入预计完成工时")
            return
        }
        val reasons = selectePersonList.filter { it.isChecked }
        if (reasons.isEmpty()) {
            toast("请选择分配人员")
            return
        }
        var selectePersonIdList: MutableList<Int> =
            mutableListOf()
        reasons.forEach {
            selectePersonIdList.add(it.id)
        }
        val request = WorkOrderFenpeiRequest(
            workOrderId,
            selectePersonIdList,
            convertLongToCompleteTime(dateTime),
            MesStringUtils.stringToInt(time),
            if (isSimple) {
                //有样品
                1
            } else {
                //无样品
                2
            },
            remark,
        )
        WebClient.request(EquipmentApi::class.java)
            .pdaEmsWBFenpeiPost(request)
            .bindToFragment()
            .subscribe({
                toast("工单填报成功")
                navBackListener.invoke(requireView())
            }, {})
    }

    companion object {
        //工单id
        const val WORKORDERID = "workOrderId"

        //小组id
        const val TEAMID = "teamId"
        const val FACILITYTYPE = "facilityType"

        //设备名称
        const val FACILITYDESC = "facilityDesc"

        //设备型号代号
        const val FACILITYMODEL = "facilityModel"
    }
}