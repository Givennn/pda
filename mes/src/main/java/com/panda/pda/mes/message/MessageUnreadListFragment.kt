package com.panda.pda.mes.message

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Looper
import android.os.MessageQueue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.library.android.controls.EndlessRecyclerViewScrollListener
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.retrofit.DataListNode
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.WheelPickerDialogFragment
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.common.data.CommonParameters
import com.panda.pda.mes.common.data.DataParamType
import com.panda.pda.mes.common.data.MesStringUtils
import com.panda.pda.mes.databinding.FragmentMessageListBinding
import com.panda.pda.mes.databinding.FrameEmptyViewBinding
import com.panda.pda.mes.databinding.ItemMessageBinding
import com.panda.pda.mes.message.model.MessageItemModel
import com.panda.pda.mes.operation.ems.data.EquipmentApi
import com.panda.pda.mes.operation.ems.equipment_workorder.maintenance.EquipmentPersonChooseListFragment
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import io.reactivex.rxjava3.core.Single
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Author:yinzhilin
 * Date: 2023/1/10
 * Desc:消息列表，子tab，已读未读列表公用
 */
class MessageUnreadListFragment(
    private var msgRead: Int,
) :
    BaseFragment(R.layout.fragment_message_list) {
    /**
     * 类型列表
     */
    private var headTypeData: MutableList<String> = mutableListOf()
    private var msgLevel: String = "-1";
    private var isFirlst = true;
    private val bindingAdapter by lazy { createRecordAdapter() }
    private val viewBinding by viewBinding<FragmentMessageListBinding>()
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    //类型弹窗
    private val headTypeDialog by lazy {
        WheelPickerDialogFragment().also {
            it.pickerData = headTypeData
        }
    }

    //出入库记录列表
    private var dataList = mutableListOf<MessageItemModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Timber.e("onCreateView")
        headTypeData.clear()
        headTypeData.add("全部")
        headTypeData.addAll(CommonParameters.getParameters(DataParamType.MESSAGE_LEVEL)
            .sortedBy { it.paramValue }.map { it.paramDesc }.toMutableList()
        )
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.e("onViewCreated")
        val layoutManager = viewBinding.rvMessage.layoutManager as? LinearLayoutManager ?: return
        scrollListener = object :
            EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                //设备的维修列表
                WebClient.request(EquipmentApi::class.java)
                    .pdaMessageListGet(
                        msgRead, MesStringUtils.stringToInt(msgLevel),
                        10,
                        page
                    )
                    .bindToFragment()
                    .subscribe({
                        bindingAdapter.addData(it.dataList)
                    }, { })
            }
        }
        viewBinding.rvMessage.addOnScrollListener(scrollListener)
        viewBinding.rvMessage.adapter = bindingAdapter
        viewBinding.swipe.setOnRefreshListener { refreshData() }
        viewBinding.llHeadType.setOnClickListener {
            if (headTypeData.isNotEmpty()) {
                showHeadTypeSelectDialog(viewBinding.tvHeadType, headTypeDialog)
            }
        }
    }

    @SuppressLint("BinaryOperationInTimber")
    override fun onResume() {
        super.onResume()
        if (isFirlst) {
            refreshData()
            isFirlst = false
        }
//        val start: Long = System.currentTimeMillis();
//        Looper.myQueue().addIdleHandler(MessageQueue.IdleHandler {
//            Timber.e("time is"+(System.currentTimeMillis()-start))
//             false
//        })
    }

    @SuppressLint("SetTextI18n")
    private fun showHeadTypeSelectDialog(
        tvSelectValue: TextView,
        dialog: WheelPickerDialogFragment,
    ) {
        dialog.setConfirmButton { result ->
            result!!.first
            viewBinding.tvHeadType.text = result.first
//            viewBinding.tvHeadType.setTextColor(Color.rgb(34, 34, 34));
            viewBinding.tvHeadType.setTextColor(Color.parseColor("#222222"));
            msgLevel = if (result.first == "全部") {
                "-1"
            } else
                CommonParameters.getValue(DataParamType.MESSAGE_LEVEL, result.first)
                    .toString()
//            navController.navigate(R.id.equipmentInfoWorkOrderAddFragment)
            //筛选后刷新列表
            refreshData()
        }.setCancelButton { dialogInterface, i ->
        }.show(parentFragmentManager, TAG)
    }

    fun refreshData() {
        api(msgRead, MesStringUtils.stringToInt(msgLevel))
            .bindToFragment()
            .doFinally { viewBinding.swipe.isRefreshing = false }
            .subscribe({ data ->
                bindingAdapter.refreshData(data.dataList)
                dataList = data.dataList as MutableList<MessageItemModel>
            }, { })
    }

    fun api(
        msgRead: Int,
        msgLevel: Int,
    ): Single<DataListNode<MessageItemModel>> {
        //设备查询维修记录
        return WebClient.request(EquipmentApi::class.java)
            .pdaMessageListGet(msgRead, msgLevel)
            .doFinally { scrollListener.resetState() }
    }

    private fun createRecordAdapter(): CommonViewBindingAdapter<ItemMessageBinding, MessageItemModel> {
        return object :
            CommonViewBindingAdapter<ItemMessageBinding, MessageItemModel>() {
            override fun createBinding(parent: ViewGroup): ItemMessageBinding {
                return ItemMessageBinding.inflate(LayoutInflater.from(parent.context))
            }

            override fun createEmptyViewBinding(parent: ViewGroup): ViewBinding {
                return FrameEmptyViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            }

            @SuppressLint("SetTextI18n")
            override fun onBindViewHolderWithData(
                holder: ViewBindingHolder,
                data: MessageItemModel,
                position: Int,
            ) {
                holder.itemViewBinding.apply {
                    tvMessageTitle.text =
                        "【" + data.messageTypeName + "】" + data.messageTemplateName
                    tvMessageContent.text = data.message
                    when (data.priority) {
                        0 -> tvMessageLevel.setBackgroundResource(R.drawable.bg_item_message_level0)
                        1 -> tvMessageLevel.setBackgroundResource(R.drawable.bg_item_message_level1)
                        2 -> tvMessageLevel.setBackgroundResource(R.drawable.bg_item_message_level2)
                    }
                    tvMessageLevel.text =
                        CommonParameters.getDesc(
                            DataParamType.MESSAGE_LEVEL,
                            data.priority
                        )
                    llRoot.setOnClickListener {
                        if (msgRead == 0) {
                            isFirlst = true
                        }
                        navController.navigate(R.id.messageDetailFragment,
                            Bundle().apply {
                                //带入详情页的数据
                                putString(
                                    MESSAGE_RESULT_KEY,
                                    getProblemRecordJsonAdapter().toJson(data)
                                )
                                putInt(MESSAGE_REFRESH_KEY, msgRead)
                            })
                    }
//                    root.clicks()
//                        .throttleFirst(500, TimeUnit.MILLISECONDS)
//                        .bindToLifecycle(holder.itemView)
//                        .subscribe {
//                            if (msgRead == 0) {
//                                isFirlst = true
//                            }
//                            navController.navigate(R.id.messageDetailFragment,
//                                Bundle().apply {
//                                    //带入详情页的数据
//                                    putString(
//                                        MESSAGE_RESULT_KEY,
//                                        getProblemRecordJsonAdapter().toJson(data)
//                                    )
//                                    putInt(MESSAGE_REFRESH_KEY, msgRead)
//
//                                })
//                        }
                }
            }
        }
    }

    companion object {
        const val MESSAGE_RESULT_KEY = "MESSAGE_RESULT_key"
        const val MESSAGE_REFRESH_KEY = "MESSAGE_REFRESH_KEY"
        fun getProblemRecordJsonAdapter(): JsonAdapter<MessageItemModel> {
            return Moshi.Builder().build().adapter(MessageItemModel::class.java)
        }
    }
}