package com.panda.pda.mes.operation.exchange_card

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.extension.getStringObject
import com.panda.pda.mes.base.extension.toast
import com.panda.pda.mes.common.ModelPropertyCreator
import com.panda.pda.mes.databinding.FragmentExchangeCardOperateBinding
import com.panda.pda.mes.operation.exchange_card.data.model.ExchangeCardModel
import com.panda.pda.mes.operation.exchange_card.data.model.WorkOrderModel
import com.panda.pda.mes.operation.fms.data.model.DispatchOrderModel
import com.panda.pda.mes.task.TaskMessageNavigationAdapter

/**
 * created by AnJiwei 2022/8/19
 */
class ExchangeCardOperateFragment : BaseFragment(R.layout.fragment_exchange_card_operate) {

    private val viewBinding by viewBinding<FragmentExchangeCardOperateBinding>()

    private var exchangeCard: ExchangeCardModel? = null

    private lateinit var exchangeCardCode: String

    private lateinit var taskMessageNavigationAdapter: TaskMessageNavigationAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }
        exchangeCard = arguments?.getStringObject<ExchangeCardModel>()
        if (exchangeCard == null) {
            toast("流转卡数据异常")
            navBackListener.invoke(view)
        }
        val card = exchangeCard!!
        if (card.cardType == 1) {
            val modelProperty = ModelPropertyCreator(
                WorkOrderModel::class.java,
                viewBinding.llPropertyInfo,
            )
            modelProperty.setData(card.workOrder)
            exchangeCardCode = card.workOrder.workOrderCode
            viewBinding.tvOperateTitle.text = getString(R.string.work_order_operate)
//            viewBinding.topAppBar.title =
        } else {
            val modelProperty = ModelPropertyCreator(
                DispatchOrderModel::class.java,
                viewBinding.llPropertyInfo,
            )
            modelProperty.setData(card.dispatchOrder)
            exchangeCardCode = card.dispatchOrder.workOrderCode
            viewBinding.tvOperateTitle.text = getString(R.string.dispatch_order_operate)
        }

        taskMessageNavigationAdapter = TaskMessageNavigationAdapter(
            R.menu.exchange_card_nav_menu,
            requireContext()
        ) { true }
            .also {
                it.navAction = { navId ->
                    navController.navigate(navId, Bundle().apply {
                        putString(WORK_ORDER_CODE, exchangeCardCode)
                    })
                }
            }
        viewBinding.rvModuleArea.adapter = taskMessageNavigationAdapter
        taskMessageNavigationAdapter.updateBadge(card.msgCount)

    }

    companion object {
        const val WORK_ORDER_CODE = "workOrderCode"
    }
}