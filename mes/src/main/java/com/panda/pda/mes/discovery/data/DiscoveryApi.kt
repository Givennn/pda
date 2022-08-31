package com.panda.pda.mes.discovery.data

import com.panda.pda.mes.base.retrofit.DataListNode
import com.panda.pda.mes.common.data.model.CommonOperationRecordModel
import com.panda.pda.mes.operation.bps.data.model.MainPlanModel
import com.panda.pda.mes.operation.fms.data.model.DispatchOrderModel
import com.panda.pda.mes.operation.fms.data.model.TaskDetailModel
import com.panda.pda.mes.operation.fms.data.model.TaskRecordModel
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

/**
 * created by AnJiwei 2021/9/1
 */
interface DiscoveryApi {
    /**
     * 未取消任务列表
     * keywords：任务编号/任务描述
     * The endpoint is owned by docs service owner
     * @param keywords  (required)
     */
    @GET("pda/fms/task/list-all")
    fun pdaFmsTaskListAllGet(
        @retrofit2.http.Query("keywords") keywords: String?,
    ): Single<DataListNode<DispatchOrderModel>>

    /**
     * 全部报工列表
     * keywords：任务编号/任务描述
     * The endpoint is owned by docs service owner
     * @param keywords  (required)
     * @param raw raw paramter (optional)
     */
    @GET("pda/fms/report/list-all")
    fun pdaFmsReportListAllGet(
        @retrofit2.http.Query("keywords") keywords: String?,
    ): Single<DataListNode<TaskReportModel>>

    /**
     * 查询报工详情
     *
     * The endpoint is owned by docs service owner
     * @param id  (required)
     * @param raw raw paramter (optional)
     */
    @GET("pda/fms/report/get-by-id")
    fun pdaFmsReportGetByIdGet(
        @retrofit2.http.Query("id") id: Int,
    ): Single<TaskReportDetailModel>

    /**
     * 主计划发现列表
     */
    @GET("pda/bps/main-plan/discovery/list")
    fun mainPlanDiscoveryListGet(@retrofit2.http.Query("keywords") keywords: String?,
    ): Single<DataListNode<MainPlanModel>>

    /**
     * 任务详情
     *
     * The endpoint is owned by docs service owner
     * @param id  (required)
     * @param raw raw paramter (optional)
     */
    @GET("pda/fms/task/get-by-id")
    fun taskGetByIdGet(
        @retrofit2.http.Query("id") id: Int,
    ): Single<DispatchOrderDetailDiscoveryModel>

    /**
     * 任务操作记录
     *
     * The endpoint is owned by docs service owner
     * @param id  (required)
     */
    @GET("pda/fms/task/operation-record")
    fun taskOperationRecordGet(
        @retrofit2.http.Query("id") id: Int,
    ): Single<DataListNode<CommonOperationRecordModel>>
}