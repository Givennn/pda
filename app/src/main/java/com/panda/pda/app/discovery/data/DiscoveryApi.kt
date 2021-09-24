package com.panda.pda.app.discovery.data

import com.panda.pda.app.base.retrofit.BaseResponse
import com.panda.pda.app.base.retrofit.DataListNode
import com.panda.pda.app.operation.fms.mission.data.model.TaskModel
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
    ): Single<BaseResponse<DataListNode<TaskModel>>>

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
    ): Single<BaseResponse<DataListNode<TaskReportModel>>>

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
    ): Single<BaseResponse<TaskReportDetailModel>>
}