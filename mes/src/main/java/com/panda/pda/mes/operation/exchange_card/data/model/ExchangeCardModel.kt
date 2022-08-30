package com.panda.pda.mes.operation.exchange_card.data.model

import com.panda.pda.mes.common.data.model.TaskMessageCountModel
import com.panda.pda.mes.operation.fms.data.model.DispatchOrderModel

/**
 * created by AnJiwei 2022/8/24
 */
data class ExchangeCardModel(
    val cardType: Int,
    val workOrder: WorkOrderModel,
    val dispatchOrder: DispatchOrderModel,
    val msgCount: List<TaskMessageCountModel>
)
