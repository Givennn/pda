package com.panda.pda.mes.user.data

import com.panda.pda.mes.base.retrofit.DataListNode
import com.panda.pda.mes.common.data.model.CommonOperationRecordModel
import com.panda.pda.mes.operation.fms.data.model.GuideListModel
import com.panda.pda.mes.user.data.model.*
import io.reactivex.rxjava3.core.Single
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * created by AnJiwei 2021/8/17
 */
interface UserApi {
//    /**
//     * 获取用户信息
//     *
//     * The endpoint is owned by docs service owner
//     * @param raw raw paramter (optional)
//     */
//    @GET("pda/admin/user/info")
//    fun pdaAdminUserInfoGet(
//        @retrofit2.http.Body raw: List<Byte>?
//    ): Single<BaseResponse<LoginDataModel>>
    /**
     * 登出
     *
     * The endpoint is owned by docs service owner
     */
    @POST("pda/admin/user/logout")
    fun pdaAdminUserLogoutPost(
    ): Single<Any>

    /**
     * 登陆
     *
     * The endpoint is owned by docs service owner
     * @param root (optional)
     */
    @POST("pda/admin/user/name-login")
    fun userNameLoginPost(
        @retrofit2.http.Body request: LoginRequest,
    ): Single<LoginDataModel>

    /**
     * 修改密码/校验
     *
     * The endpoint is owned by docs service owner
     * @param root (optional)
     */
    @POST("pda/admin/user/password/check")
    fun pdaAdminUserPasswordCheckPost(
        @retrofit2.http.Body request: PwdCheckRequest,
    ): Single<Any>

    /**
     * 修改密码/修改
     *
     * The endpoint is owned by docs service owner
     * @param root (optional)
     */
    @POST("pda/admin/user/password/modify")
    fun pdaAdminUserPasswordModifyPost(
        @retrofit2.http.Body request: PwdModifyRequest,
    ): Single<Any>

    @Headers("Content-Type: application/json")
    @POST("pda/admin/user/code-login")
    fun qrCodeLoginPost(@retrofit2.http.Body request: String): Single<LoginDataModel>

    /**
     * 查询系统日志
     */
    @GET("pda/admin/user/system-log")
    fun pdaSystemLogGet(
        @retrofit2.http.Query("operateStartTime") operateStartTime: String?,
        @retrofit2.http.Query("operateEndTime") operateEndTime: String?,
    ): Single<DataListNode<SystemLogModel>>

    /**
     * 查询个人绩效列表
     */
    @GET("pda/staff/performance/statistics/self/list-by-page")
    fun pdaPersonalPerformanceGet(
        @retrofit2.http.Query("statisticsTime") statisticsTime: String?,
        @retrofit2.http.Query("page") page: Int? = 1,
        @retrofit2.http.Query("rows") rows: Int? = 10,
    ): Single<DataListNode<PersonalPerformanceModel>>

    @GET("pda/staff/performance/statistics/detail-skill-item")
    fun pdaSkillItemListGet(@retrofit2.http.Query("id") performanceId: Int): Single<DataListNode<SkillItemModel>>

    @GET("pda/staff/performance/statistics/detail-work-hour")
    fun pdaWorkHourListGet(@retrofit2.http.Query("id") performanceId: Int): Single<DataListNode<WorkHourModel>>

    @GET("pda/staff/performance/statistics/detail")
    fun pdaPerformanceDetailGet(@retrofit2.http.Query("id") performanceId: Int): Single<PerformanceDetailInfoModel>

    @GET("pda/staff/performance/operate-list")
    fun pdaPerformanceOperateRecord(@retrofit2.http.Query("id") performanceId: Int): Single<DataListNode<CommonOperationRecordModel>>
}