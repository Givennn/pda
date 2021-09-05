package com.panda.pda.app.operation.data

import com.panda.pda.app.base.retrofit.BaseResponse
import com.panda.pda.app.base.retrofit.DataListNode
import com.panda.pda.app.operation.data.model.*
import com.panda.pda.app.task.data.model.TaskModel
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * created by AnJiwei 2021/9/1
 */
interface MaterialApi {

    /**
     * 产品物料替换绑定物料
     *
     * The endpoint is owned by docs service owner
     * @param root (optional)
     */
    @POST("pda/fms/task/material/bind-change")
    fun pdaFmsTaskMaterialBindChangePost(
        @retrofit2.http.Body request: MaterialReplaceBindRequest
    ): Single<BaseResponse<Any>>
    /**
     * 绑定产品物料
     *
     * The endpoint is owned by docs service owner
     * @param root (optional)
     */

    @POST("pda/fms/task/material/bind")
    fun taskMaterialBindPost(
        @retrofit2.http.Body request: MaterialBindRequest
    ): Single<BaseResponse<Any>>
    /**
     * 任务列表
     * keywords：任务编号/产品编码/产品描述/派工人
     * The endpoint is owned by docs service owner
     * @param keywords  (required)
     * @param raw raw paramter (optional)
     */

    @GET("pda/fms/task/material/task/list-by-page")
    fun materialTaskListByPageGet(
        @retrofit2.http.Query("keywords") keywords: String?,
    ): Single<BaseResponse<DataListNode<TaskModel>>>
    /**
     * 查询已绑定的物料
     * keywords：任务编号/产品编码/产品描述/派工人
     * The endpoint is owned by docs service owner
     * @param taskId  (required)
     * @param productBarCode  (required)
     * @param raw raw paramter (optional)
     */

    @GET("pda/fms/task/material/task/query-bind-by-product")
    fun materialTaskQueryBindByProductGet(
        @retrofit2.http.Query("taskId") taskId: Int,
        @retrofit2.http.Query("productBarCode") productBarCode: String,
    ): Single<BaseResponse<TaskBandedMaterialModel>>
    /**
     * 绑定/查询物料
     *
     * The endpoint is owned by docs service owner
     * @param materielSerialCode  (required)
     */
    @GET("pda/fms/task/material/task/query-material")
    fun materialTaskQueryMaterialGet(
        @retrofit2.http.Query("materielSerialCode") materielSerialCode: String,
    ): Single<BaseResponse<MaterialModel>>
    /**
     * 产品物料解绑
     *
     * The endpoint is owned by docs service owner
     * @param root (optional)
     */

    @POST("pda/fms/task/material/unbind")
    fun materialUnbindPost(
        @retrofit2.http.Body request: MaterialUnbindRequest
    ): Single<BaseResponse<Any>>
}