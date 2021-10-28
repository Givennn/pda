package com.panda.pda.app.operation.fms.data

import com.panda.pda.app.operation.fms.data.model.AlarmDetailRequest
import com.panda.pda.app.operation.fms.data.model.AlarmHistoryListModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * created by AnJiwei 2021/9/1
 */
interface AlarmApi {
    /**
     * 新增警报
     * status  状态 1未关闭 2关闭
     * The endpoint is owned by docs service owner
     * @param root (optional)
     */
    @POST("pda/fms/alarm/add")
    fun pdaFmsAlarmAddPost(
        @retrofit2.http.Body request: AlarmDetailRequest
    ): Single<Any>
    /**
     * 警报历史查询
     * status  状态 1未关闭 2关闭
     * The endpoint is owned by docs service owner
     * @param page  (required)
     * @param rows  (required)
     * @param raw raw paramter (optional)
     */
    @GET("pda/fms/alarm/my-history")
    fun pdaFmsAlarmMyHistoryGet(
        @retrofit2.http.Query("page") page: Int? = null,
        @retrofit2.http.Query("rows") rows: Int? = null,
    ): Single<AlarmHistoryListModel>
}