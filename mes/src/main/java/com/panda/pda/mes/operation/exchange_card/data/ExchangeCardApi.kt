package com.panda.pda.mes.operation.exchange_card.data

import com.panda.pda.mes.operation.exchange_card.data.model.ExchangeCardModel
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

/**
 * created by AnJiwei 2022/8/24
 */
interface ExchangeCardApi {

    /**
     * 流转卡扫描
     */
    @GET("pda/task/exchange-card/scan")
    fun exchangeCardScantGet(@retrofit2.http.Query("code") code: String): Single<ExchangeCardModel>
}