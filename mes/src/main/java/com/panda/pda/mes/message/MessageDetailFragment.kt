package com.panda.pda.mes.message

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import by.kirich1409.viewbindingdelegate.viewBinding
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.extension.getStringObject
import com.panda.pda.mes.base.extension.putObjectString
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.ModelPropertyCreator
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.common.data.CommonApi
import com.panda.pda.mes.common.data.CommonParameters
import com.panda.pda.mes.common.data.DataParamType
import com.panda.pda.mes.common.data.MesStringUtils
import com.panda.pda.mes.common.data.model.FileInfoModel
import com.panda.pda.mes.databinding.*
import com.panda.pda.mes.message.model.MessageItemModel
import com.panda.pda.mes.operation.ems.data.EquipmentApi
import com.panda.pda.mes.operation.ems.data.model.EquipmentInfoDeviceModel
import com.panda.pda.mes.operation.ems.data.model.EquipmentWorkOrderModel
import com.panda.pda.mes.operation.ems.data.model.MessageReadRequest
import com.panda.pda.mes.operation.ems.data.model.WorkOrderInStoreConfirmRequest
import com.panda.pda.mes.operation.ems.equipment_workorder.EquipmentProduceHistoryListFragment
import com.panda.pda.mes.operation.ems.equipment_workorder.maintenance.EquipmentInfoWorkOrderMaintenanceCompleteFragment
import com.panda.pda.mes.operation.ems.equipment_workorder.maintenance.EquipmentPersonChooseListFragment
import com.panda.pda.mes.operation.ems.equipment_workorder.maintenance.EquipmentWorkOrderWaitCompleteFragment
import com.panda.pda.mes.operation.ems.equipment_workorder.maintenance.EquipmentWorkOrderWaitFenpeiFragment
import com.panda.pda.mes.operation.ems.equipment_workorder.store.EquipmentDeviceChooseListFragment
import com.panda.pda.mes.operation.ems.equipment_workorder.store.EquipmentInfoWorkOrderConfirmOutStoreFragment
import com.panda.pda.mes.operation.ems.equipment_workorder.store.EquipmentInfoWorkOrderInStoreCompleteFragment
import com.panda.pda.mes.operation.ems.equipment_workorder.store.EquipmentInfoWorkOrderWaitInStoreFragment
import com.panda.pda.mes.operation.qms.NgReasonFragment
import com.panda.pda.mes.operation.qms.QualityViewModel
import com.panda.pda.mes.operation.qms.data.model.QualityProblemRecordDetailModel
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Author:yinzhilin
 * Date: 2023/1/10
 * Desc:消息详情
 */
class MessageDetailFragment :
    BaseFragment(R.layout.fragment_message_detail) {

    private val viewBinding by viewBinding<FragmentMessageDetailBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mesRead = arguments?.getInt(MESSAGE_REFRESH_KEY, -1)
        val messageJson = arguments?.getString(MESSAGE_RESULT_KEY)
        messageJson?.let {
            getProblemRecordJsonAdapter().fromJson(messageJson)?.let { it1 ->
                if (mesRead != null) {
                    showDetail(it1, mesRead)
                }

            }
        }
        viewBinding.topAppBar.setNavigationOnClickListener {
            navBackListener.invoke(it)
        }

    }

    private fun readMessage(data: MessageItemModel) {
        val listMsgId: MutableList<Int> = mutableListOf()
        listMsgId.add(data.id)
        val request = MessageReadRequest(listMsgId)
        //消息未读点击后需要标记为已读
        WebClient.request(EquipmentApi::class.java)
            .pdaMessageReadPost(request)
            .bindToFragment()
            .subscribe({
//                                        refreshData()
            }, {})
    }

    //详情
    @SuppressLint("SetTextI18n")
    private fun showDetail(data: MessageItemModel, msgRead: Int) {
        if (msgRead == 0) {
            readMessage(data)
        }
        viewBinding.apply {
            tvMessageTitle.text =
                "【" + data.messageTypeName + "】" + data.messageTemplateName
            tvMessageContent.text = data.message
            tvMessageAuthor.text = "操作人" + data.templateCreateName
            tvMessageResource.text = "来源：" + data.appName
            tvMessageTime.text = "操作时间：" + data.createTime
        }
    }


    companion object {
        const val MESSAGE_RESULT_KEY = "MESSAGE_RESULT_key"
        const val MESSAGE_REFRESH_KEY = "MESSAGE_REFRESH_KEY"
        const val MESSAGE_REFRESH_RESULT_KEY = "MESSAGE_REFRESH_RESULT_KEY"
        fun getProblemRecordJsonAdapter(): JsonAdapter<MessageItemModel> {
            return Moshi.Builder().build().adapter(MessageItemModel::class.java)
        }
    }
}