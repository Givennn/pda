package com.panda.pda.app.task.data

import com.panda.pda.app.base.retrofit.BaseResponse
import com.panda.pda.app.base.retrofit.DataListNode
import com.panda.pda.app.task.data.model.*
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

/**
 * created by AnJiwei 2021/8/24
 */
interface TaskApi {

//    /**
//     * 查询报工详情
//     *
//     * The endpoint is owned by docs service owner
//     * @param id  (required)
//     * @param raw raw paramter (optional)
//     */
//    @Headers(
//        "Content-Type: text/plain"
//    )
//    @GET("pda/fms/report/get-by-id")
//    fun pdaFmsReportGetByIdGet(
//        @retrofit2.http.Query("id") id: String,
//        @retrofit2.http.Body raw: List<Byte>?
//    ): Single<PdaFmsReportGetByIdGetResponse>
//    /**
//     * 全部报工列表
//     * keywords：任务编号/任务描述
//     * The endpoint is owned by docs service owner
//     * @param keywords  (required)
//     * @param raw raw paramter (optional)
//     */
//    @Headers(
//        "Content-Type: text/plain"
//    )
//    @GET("pda/fms/report/list-all")
//    fun pdaFmsReportListAllGet(
//        @retrofit2.http.Query("keywords") keywords: String,
//        @retrofit2.http.Body raw: List<Byte>?
//    ): Single<PdaFmsReportListAllGetResponse>
    /**
     * 任务完工
     *
     * The endpoint is owned by docs service owner
     * @param root (optional)
     */

    @POST("pda/fms/task/complete/confirm")
    fun taskCompleteConfirmPost(
        @retrofit2.http.Body request: TaskIdRequest,
    ): Single<BaseResponse<Any>>

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
    ): Single<BaseResponse<DataListNode<TaskModel>>>

    /**
     * 执行任务
     *
     * The endpoint is owned by docs service owner
     * @param root (optional)
     */
    @POST("pda/fms/task/execution/confirm")
    fun taskExecutionConfirmPost(
        @retrofit2.http.Body request: TaskIdRequest,
    ): Single<BaseResponse<Any>>

    /**
     * 任务执行列表
     * keywords：任务编号  产品编码或者描述  派工人工号或者姓名
     * The endpoint is owned by docs service owner
     * @param keywords  (optional)
     */

    @GET("pda/fms/task/execution/list-by-page")
    fun taskExecutionListByPageGet(
        @retrofit2.http.Query("keywords") keywords: String?,
    ): Single<BaseResponse<DataListNode<TaskModel>>>

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
    ): Single<BaseResponse<TaskDetailModel>>
//    /**
//     * 未取消任务列表
//     * keywords：任务编号/任务描述
//     * The endpoint is owned by docs service owner
//     * @param keywords  (required)
//     * @param raw raw paramter (optional)
//     */
//    @Headers(
//        "Content-Type: text/plain"
//    )
//    @GET("pda/fms/task/list-all")
//    fun pdaFmsTaskListAllGet(
//        @retrofit2.http.Query("keywords") keywords: String,
//        @retrofit2.http.Body raw: List<Byte>?
//    ): Single<PdaFmsTaskListAllGetResponse>
//    /**
//     * 产品物料替换绑定物料
//     *
//     * The endpoint is owned by docs service owner
//     * @param root (optional)
//     */
//    @Headers(
//        "Content-Type: application/json"
//    )
//    @POST("pda/fms/task/material/bind-change")
//    fun pdaFmsTaskMaterialBindChangePost(
//        @retrofit2.http.Body root: EmptyObject
//    ): Single<EmptyObject4>
//    /**
//     * 绑定产品物料
//     *
//     * The endpoint is owned by docs service owner
//     * @param root (optional)
//     */
//    @Headers(
//        "Content-Type: application/json"
//    )
//    @POST("pda/fms/task/material/bind")
//    fun pdaFmsTaskMaterialBindPost(
//        @retrofit2.http.Body root: EmptyObject
//    ): Single<EmptyObject4>
//    /**
//     * 任务列表
//     * keywords：任务编号/产品编码/产品描述/派工人
//     * The endpoint is owned by docs service owner
//     * @param keywords  (required)
//     * @param raw raw paramter (optional)
//     */
//    @Headers(
//        "Content-Type: text/plain"
//    )
//    @GET("pda/fms/task/material/task/list-by-page")
//    fun pdaFmsTaskMaterialTaskListByPageGet(
//        @retrofit2.http.Query("keywords") keywords: String,
//        @retrofit2.http.Body raw: List<Byte>?
//    ): Single<PdaFmsTaskMaterialTaskListByPageGetResponse>
//    /**
//     * 查询已绑定的物料
//     * keywords：任务编号/产品编码/产品描述/派工人
//     * The endpoint is owned by docs service owner
//     * @param taskId  (required)
//     * @param productBarCode  (required)
//     * @param raw raw paramter (optional)
//     */
//    @Headers(
//        "Content-Type: text/plain"
//    )
//    @GET("pda/fms/task/material/task/query-bind-by-product")
//    fun pdaFmsTaskMaterialTaskQueryBindByProductGet(
//        @retrofit2.http.Query("taskId") taskId: String,
//        @retrofit2.http.Query("productBarCode") productBarCode: String,
//        @retrofit2.http.Body raw: List<Byte>?
//    ): Single<PdaFmsTaskMaterialTaskQueryBindByProductGetResponse>
//    /**
//     * 绑定/查询物料
//     *
//     * The endpoint is owned by docs service owner
//     * @param materielSerialCode  (required)
//     * @param raw raw paramter (optional)
//     */
//    @Headers(
//        "Content-Type: text/plain"
//    )
//    @GET("pda/fms/task/material/task/query-material")
//    fun pdaFmsTaskMaterialTaskQueryMaterialGet(
//        @retrofit2.http.Query("materielSerialCode") materielSerialCode: String,
//        @retrofit2.http.Body raw: List<Byte>?
//    ): Single<PdaFmsTaskMaterialTaskQueryMaterialGetResponse>
//    /**
//     * 产品物料解绑
//     *
//     * The endpoint is owned by docs service owner
//     * @param root (optional)
//     */
//    @Headers(
//        "Content-Type: application/json"
//    )
//    @POST("pda/fms/task/material/unbind")
//    fun pdaFmsTaskMaterialUnbindPost(
//        @retrofit2.http.Body root: EmptyObject
//    ): Single<EmptyObject4>
    /**
     * 任务操作记录
     *
     * The endpoint is owned by docs service owner
     * @param id  (required)
     */
    @GET("pda/fms/task/operation-record")
    fun taskOperationRecordGet(
        @retrofit2.http.Query("id") id: Int,
    ): Single<BaseResponse<DataListNode<TaskRecordModel>>>

    /**
     * 接收任务
     *
     * The endpoint is owned by docs service owner
     * @param root (optional)
     */
    @POST("pda/fms/task/receive/confirm")
    fun taskReceiveConfirmPost(
        @retrofit2.http.Body request: TaskIdRequest,
    ): Single<BaseResponse<Any>>

    /**
     * 任务接收列表
     * keywords：任务编号/产品编码/产品描述/派工人
     * The endpoint is owned by docs service owner
     * @param keywords  (optional)
     */

    @GET("pda/fms/task/receive/list-by-page")
    fun taskReceiveListByPageGet(
        @retrofit2.http.Query("keywords") keywords: String?,
    ): Single<BaseResponse<DataListNode<TaskModel>>>
//    /**
//     * 任务报工
//     *
//     * The endpoint is owned by docs service owner
//     * @param root (optional)
//     */
//    @Headers(
//        "Content-Type: application/json"
//    )
//    @POST("pda/fms/task/report/confirm")
//    fun pdaFmsTaskReportConfirmPost(
//        @retrofit2.http.Body root: EmptyObject
//    ): Single<EmptyObject8>
//    /**
//     * 任务报工列表
//     * keywords：任务编号  产品编码或者描述  派工人工号或者姓名
//     * The endpoint is owned by docs service owner
//     * @param keywords  (optional)
//     * @param raw raw paramter (optional)
//     */
//    @Headers(
//        "Content-Type: text/plain"
//    )
//    @GET("pda/fms/task/report/list-by-page")
//    fun pdaFmsTaskReportListByPageGet(
//        @retrofit2.http.Query("keywords") keywords: String?,
//        @retrofit2.http.Body raw: List<Byte>?
//    ): Single<PdaFmsTaskReportListByPageGetResponse>
//    /**
//     * 作业指导书列表
//     *
//     * The endpoint is owned by docs service owner
//     * @param keywords  (required)
//     * @param page  (required)
//     * @param rows  (required)
//     * @param raw raw paramter (optional)
//     */
//    @Headers(
//        "Content-Type: text/plain"
//    )
//    @GET("pda/fms/work-guide/list")
//    fun pdaFmsWorkGuideListGet(
//        @retrofit2.http.Query("keywords") keywords: String,
//        @retrofit2.http.Query("page") page: String,
//        @retrofit2.http.Query("rows") rows: String,
//        @retrofit2.http.Body raw: List<Byte>?
//    ): Single<PdaFmsWorkGuideListGetResponse>
//    /**
//     * 任务/查询任务数量
//     *
//     * The endpoint is owned by docs service owner
//     * @param raw raw paramter (optional)
//     */
//    @Headers(
//        "Content-Type: text/plain"
//    )
//    @GET("pda/task/msg-count")
//    fun pdaTaskMsgCountGet(
//        @retrofit2.http.Body raw: List<Byte>?
//    ): Single<PdaTaskMsgCountGetResponse>
}