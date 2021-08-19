package com.panda.pda.app.user.data

import com.panda.pda.app.base.retrofit.BaseResponse
import com.panda.pda.app.user.data.model.LoginDataModel
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.POST

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
    ): Single<BaseResponse<LoginDataModel>>
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
        @retrofit2.http.Body root: Any
    ): Single<BaseResponse<LoginDataModel>>
    /**
     * 修改密码/校验
     *
     * The endpoint is owned by docs service owner
     * @param root (optional)
     */
    @POST("pda/admin/user/password/check")
    fun pdaAdminUserPasswordCheckPost(
        @retrofit2.http.Body root: Any
    ): Single<BaseResponse<Any>>
    /**
     * 修改密码/修改
     *
     * The endpoint is owned by docs service owner
     * @param root (optional)
     */
    @POST("pda/admin/user/password/modify")
    fun pdaAdminUserPasswordModifyPost(
        @retrofit2.http.Body root: Any
    ): Single<BaseResponse<Any>>
}