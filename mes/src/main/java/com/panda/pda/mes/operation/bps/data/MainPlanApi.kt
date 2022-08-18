package com.panda.pda.mes.operation.bps.data

import com.panda.pda.mes.base.retrofit.DataListNode
import com.panda.pda.mes.common.data.model.CommonOperationRecordModel
import com.panda.pda.mes.common.data.model.IdRequest
import com.panda.pda.mes.operation.bps.data.model.MainPlanDetailModel
import com.panda.pda.mes.operation.bps.data.model.MainPlanModel
import com.panda.pda.mes.operation.bps.data.model.MainPlanReportRequest
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * created by AnJiwei 2022/8/8
 */
interface MainPlanApi {

    /**
     * 主计划操作记录
     *
     * The endpoint is owned by docs service owner
     * @param id  (required)
     */
    @GET("pda/bps/main-plan/operation-record")
    fun mainPlanOperationRecordGet(
        @retrofit2.http.Query("id") id: Int,
    ): Single<DataListNode<CommonOperationRecordModel>>

    /**
     * 查看主计划详情
     */
    @GET("pda/bps/main-plan/detail")
    fun mainPlanDetailGet(@retrofit2.http.Query("id") id: Int): Single<MainPlanDetailModel>

    /**
     * 主计划报工列表
     */
    @GET("pda/bps/main-plan/report/list")
    fun mainPlanReportListGet(@retrofit2.http.Query("keywords") keywords: String?,
    ): Single<DataListNode<MainPlanModel>>

    /**
     * 主计划报工
     */
    @POST("pda/bps/main-plan/report/confirm")
    fun mainPlanReportConfirmPost(@retrofit2.http.Body request: MainPlanReportRequest): Single<Any>
    /**
     * 主计划完工列表
     */
    @GET("pda/bps/main-plan/finish/list")
    fun mainPlanFinishListGet(@retrofit2.http.Query("keywords") keywords: String?,
    ): Single<DataListNode<MainPlanModel>>

    /**
     * 主计划完工
     */
    @POST("pda/bps/main-plan/finish/confirm")
    fun mainPlanFinishConfirmPost(@retrofit2.http.Body request: IdRequest): Single<Any>
}