package com.panda.pda.app.operation.data

import com.panda.pda.app.base.retrofit.BaseResponse
import com.panda.pda.app.operation.data.model.GuideListModel
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

/**
 * created by AnJiwei 2021/9/1
 */
interface GuideApi {
        /**
     * 作业指导书列表
     *
     * The endpoint is owned by docs service owner
     * @param keywords  (required)
     * @param page  (required)
     * @param rows  (required)
     */
    @GET("pda/fms/work-guide/list")
    fun pdaFmsWorkGuideListGet(
        @retrofit2.http.Query("keywords") keywords: String?,
        @retrofit2.http.Query("page") page: String? = null,
        @retrofit2.http.Query("rows") rows: String? = null,
    ): Single<BaseResponse<GuideListModel>>
}