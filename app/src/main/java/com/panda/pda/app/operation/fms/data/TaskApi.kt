package com.panda.pda.app.operation.fms.data

import com.panda.pda.app.base.retrofit.DataListNode
import com.panda.pda.app.common.data.model.IdRequest
import com.panda.pda.app.operation.fms.data.model.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

/**
 * created by AnJiwei 2021/8/24
 */
interface TaskApi {
    /**
     * 任务完工
     *
     * The endpoint is owned by docs service owner
     * @param root (optional)
     */

    @POST("pda/fms/task/complete/confirm")
    fun taskCompleteConfirmPost(
        @retrofit2.http.Body request: IdRequest,
    ): Completable

    /**
     * 任务完工列表
     * keywords：任务编号  产品编码或者描述  派工人工号或者姓名
     * The endpoint is owned by docs service owner
     * @param keywords  (optional)
     * @param raw raw paramter (optional)
     */

    @GET("pda/fms/task/complete/list-by-page")
    fun taskCompleteListGet(
        @retrofit2.http.Query("keywords") keywords: String? = null,
    ): Single<DataListNode<TaskModel>>

    /**
     * 执行任务
     *
     * The endpoint is owned by docs service owner
     * @param root (optional)
     */
    @POST("pda/fms/task/execution/confirm")
    fun taskExecutionConfirmPost(
        @retrofit2.http.Body request: IdRequest,
    ): Completable

    /**
     * 任务执行列表
     * keywords：任务编号  产品编码或者描述  派工人工号或者姓名
     * The endpoint is owned by docs service owner
     * @param keywords  (optional)
     */

    @GET("pda/fms/task/execution/list-by-page")
    fun taskExecutionListByPageGet(
        @retrofit2.http.Query("keywords") keywords: String?,
    ): Single<DataListNode<TaskModel>>

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
    ): Single<TaskDetailModel>

    /**
     * 任务操作记录
     *
     * The endpoint is owned by docs service owner
     * @param id  (required)
     */
    @GET("pda/fms/task/operation-record")
    fun taskOperationRecordGet(
        @retrofit2.http.Query("id") id: Int,
    ): Single<DataListNode<TaskRecordModel>>

    /**
     * 接收任务
     *
     * The endpoint is owned by docs service owner
     * @param root (optional)
     */
    @POST("pda/fms/task/receive/confirm")
    fun taskReceiveConfirmPost(
        @retrofit2.http.Body request: IdRequest,
    ): Completable

    /**
     * 任务接收列表
     * keywords：任务编号/产品编码/产品描述/派工人
     * The endpoint is owned by docs service owner
     * @param keywords  (optional)
     */

    @GET("pda/fms/task/receive/list-by-page")
    fun taskReceiveListByPageGet(
        @retrofit2.http.Query("keywords") keywords: String?,
    ): Single<DataListNode<TaskModel>>
    /**
     * 任务报工
     *
     * The endpoint is owned by docs service owner
     */
    @POST("pda/fms/task/report/confirm")
    fun pdaFmsTaskReportConfirmPost(
        @retrofit2.http.Body request: TaskReportRequest
    ): Completable
    /**
     * 任务报工列表
     * keywords：任务编号  产品编码或者描述  派工人工号或者姓名
     * The endpoint is owned by docs service owner
     * @param keywords  (optional)
     */
    @GET("pda/fms/task/report/list-by-page")
    fun pdaFmsTaskReportListByPageGet(
        @retrofit2.http.Query("keywords") keywords: String?,
    ): Single<DataListNode<TaskModel>>

}