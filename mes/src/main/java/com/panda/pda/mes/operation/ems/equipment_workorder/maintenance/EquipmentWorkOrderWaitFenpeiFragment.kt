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

    //完工日期
    var completeDate: Long = 0

    //完工时间，都是log型，提交前会转换成date型
    var completeTime: Long = 0

    //日期选择器
    private val datePicker by lazy {
        MaterialDatePicker.Builder.datePicker().setTitleText(R.string.time_pick).build()
    }
    var datePickerClickAble = true
    var timePickerClickAble = true


    //时间选择器
    private val timePicker by lazy {
        MaterialTimePicker.Builder().setTitleText("时间选择").build()
    }

    //人员适配器
    private val ngReasonAdapter by lazy { EquipmentPersonChooseListFragment.getNgReasonAdapter() }

    //人员列表，已选也包含未选
    private var selectePersonList: MutableList<EquipmentPersonChooseModel> =
        mutableListOf()

    //工单id
    var workOrderId: String = ""

    //小组id，用于查询人员列表使用
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
            //日期点击
            llWorkorderCompleteDate.setOnClickListener {
                if (datePickerClickAble) {
                    datePicker.show(childFragmentManager, TAG)
                    datePickerClickAble = false
                }

            }
            //时间点击
            llWorkorderCompleteTime.setOnClickListener {
                if (timePickerClickAble) {
                    timePicker.show(parentFragmentManager, TAG)
                    timePickerClickAble = false
                }
            }
            //是否提供样品：是点击
            llSampleYes.setOnClickListener {
                //提供样品点击
                ivSampleYes.setImageResource(R.drawable.icon_equipment_check_selected)
                ivSampleNo.setImageResource(R.drawable.icon_equipment_check_unselect)
                isSimple = true
            }
            //是否提供样品：否点击
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
            //人员选择点击
            llWorkorderPerson.setOnClickListener {
                //跳转人员选择列表
                navToPersonSelect()
            }
            //确认按钮
            btnConfirm.setOnClickListener {
                //工单提交
                submit()
            }

        }
        //日期选择器添加回调
        datePicker.addOnPositiveButtonClickListener {
            onTimePicked(it)
            //此处有日期的偏移量（系统的datepicker会有时区，跟东8区相差8小时），需要注意相差8小时
            completeDate = it - 8 * 60 * 60 * 1000
        }
        datePicker.addOnDismissListener {
            datePickerClickAble = true
        }
        //时间选择器添加回调
        timePicker.addOnPositiveButtonClickListener {
            viewBinding.tvCompleteTime.text = convertHMToTime(timePicker.hour, timePicker.minute)
            completeTime =
                (timePicker.hour * 60 * 60 * 1000 + timePicker.minute * 60 * 1000).toLong()
        }
        timePicker.addOnDismissListener {
            timePickerClickAble = true
        }
        //人员选择回调
        setFragmentResultListener(EquipmentPersonChooseListFragment.REQUEST_KEY) { requestKey, bundle ->
            if (requestKey == EquipmentPersonChooseListFragment.REQUEST_KEY) {
                val ngReasonsStr =
                    bundle.getString(EquipmentPersonChooseListFragment.NG_REASON_ARG_KEY, "")
                selectePersonList =
                    ngReasonAdapter.fromJson(ngReasonsStr)?.toMutableList() ?: mutableListOf()
                //人员列表过滤出已选的
                val reasons = selectePersonList.filter { it.isChecked }
                if (!reasons.isEmpty()) {
                    //展示人员名称
                    viewBinding.tvName.text = reasons?.joinToString(";") {
                        it.userName
                    }
                }
            }
        }

    }

    //跳转至人员选择列表
    private fun navToPersonSelect() {
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
        Timber.e("currentTime${System.currentTimeMillis()}")
        if(dateTime<=System.currentTimeMillis()){
            toast("选择的时间不得小于当前时间")
            return
        }

        val remark = viewBinding.etRemark.text.toString().trim()
        val time = viewBinding.etTimecount.text.toString().trim()
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
                toast("工单分配成功")
                navBackListener.invoke(requireView())
            }, {})
    }

    companion object {
        //工单id
        const val WORKORDERID = "workOrderId"

        //小组id
        const val TEAMID = "teamId"

        //设备类型
        const val FACILITYTYPE = "facilityType"

        //设备名称
        const val FACILITYDESC = "facilityDesc"

        //设备型号代号
        const val FACILITYMODEL = "facilityModel"
    }
}