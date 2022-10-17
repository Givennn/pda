package com.panda.pda.mes.operation.qms.ng_product_deall

import com.panda.pda.mes.R
import com.panda.pda.mes.base.retrofit.DataListNode
import com.panda.pda.mes.common.CommonSearchListFragment
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.operation.qms.data.model.NGProductDealModel
import com.panda.pda.mes.operation.qms.data.model.QualityTaskModel
import io.reactivex.rxjava3.core.Single

/**
 * created by AnJiwei 2022/10/10
 */
class NGProductDealFragment : CommonSearchListFragment<QualityTaskModel>() {


    override fun createAdapter(): CommonViewBindingAdapter<*, QualityTaskModel> {
        TODO("Not yet implemented")
    }

    override fun api(key: String?): Single<DataListNode<QualityTaskModel>> {
        TODO("Not yet implemented")
    }

    override val titleResId: Int
        get() = R.string.ng_product_deal
}