package com.panda.pda.app.user.data

import com.panda.pda.app.base.retrofit.BaseResponse
import com.panda.pda.app.user.data.model.LoginDataModel
import com.panda.pda.app.user.data.model.LoginRequest
import com.panda.pda.app.user.data.model.PwdCheckRequest
import com.panda.pda.app.user.data.model.PwdModifyRequest
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

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
    ): Completable
    /**
     * 登陆
     *
     * The endpoint is owned by docs service owner
     * @param root (optional)
     */
    @POST("pda/admin/user/name-login")
    fun userNameLoginPost(
        @retrofit2.http.Body request: LoginRequest
    ): Single<LoginDataModel>
    /**
     * 修改密码/校验
     *
     * The endpoint is owned by docs service owner
     * @param root (optional)
     */
    @POST("pda/admin/user/password/check")
    fun pdaAdminUserPasswordCheckPost(
        @retrofit2.http.Body request: PwdCheckRequest
    ): Completable
    /**
     * 修改密码/修改
     *
     * The endpoint is owned by docs service owner
     * @param root (optional)
     */
    @POST("pda/admin/user/password/modify")
    fun pdaAdminUserPasswordModifyPost(
        @retrofit2.http.Body request: PwdModifyRequest
    ): Completable
}