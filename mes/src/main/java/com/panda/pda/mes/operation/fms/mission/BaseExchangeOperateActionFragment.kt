package com.panda.pda.mes.operation.fms.mission

import android.os.Bundle
import android.view.View
import com.panda.pda.mes.common.CommonSearchListFragment
import com.panda.pda.mes.operation.exchange_card.ExchangeCardOperateFragment
import com.panda.pda.mes.operation.fms.data.model.IExchangeCardOperateItem

/**
 * created by AnJiwei 2022/8/29
 */
abstract class BaseExchangeOperateActionFragment<TSource : IExchangeCardOperateItem> :
    CommonSearchListFragment<TSource>() {

    protected var workOrderCodeFilter: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        workOrderCodeFilter = arguments?.getString(ExchangeCardOperateFragment.WORK_ORDER_CODE)
    }

    override fun refreshData() {
        api(viewBinding.etSearchBar.text?.toString())
            .bindToFragment()
            .doFinally { viewBinding.swipe.isRefreshing = false }
            .subscribe({ data ->
                itemListAdapter.refreshData(if (workOrderCodeFilter == null) data.dataList else data.dataList.toMutableList()
                    .filter { item ->
                        item.getWorkOrder() == workOrderCodeFilter
                    })
            }, { })
    }
}