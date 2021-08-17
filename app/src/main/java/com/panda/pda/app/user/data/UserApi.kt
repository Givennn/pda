package com.panda.pda.app.user.data

import com.panda.android.client.models.*
import com.panda.pda.app.retrofit.BaseResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import java.util.*

/**
 * created by AnJiwei 2021/8/17
 */
interface UserApi {
    /**
     * 获取用户信息
     *
     * The endpoint is owned by docs service owner
     * @param raw raw paramter (optional)
     */
    @GET("pda/admin/user/info")
    fun pdaAdminUserInfoGet(
        @retrofit2.http.Body raw: List<Byte>?
    ): Single<PdaAdminUserInfoGetResponse>
    /**
     * 登出
     *
     * The endpoint is owned by docs service owner
     * @param raw raw paramter (optional)
     */
    @POST("pda/admin/user/logout")
    fun pdaAdminUserLogoutPost(
        @retrofit2.http.Body raw: List<Byte>?
    ): Single<BaseResponse<Any>>
    /**
     * 登陆
     *
     * The endpoint is owned by docs service owner
     * @param root (optional)
     */
    @POST("pda/admin/user/name-login")
    fun pdaAdminUserNameLoginPost(
        @retrofit2.http.Body root: LoginRequest
    ): Single<PdaAdminUserNameLoginPostResponse>
    /**
     * 修改密码/校验
     *
     * The endpoint is owned by docs service owner
     * @param root (optional)
     */
    @POST("pda/admin/user/password/check")
    fun pdaAdminUserPasswordCheckPost(
        @retrofit2.http.Body root: EmptyObject
    ): Single<InlineResponse2001>
    /**
     * 修改密码/修改
     *
     * The endpoint is owned by docs service owner
     * @param root (optional)
     */
    @POST("pda/admin/user/password/modify")
    fun pdaAdminUserPasswordModifyPost(
        @retrofit2.http.Body root: EmptyObject
    ): Single<InlineResponse2001>
}