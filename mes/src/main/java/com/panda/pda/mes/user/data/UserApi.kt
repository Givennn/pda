package com.panda.pda.mes.user.data

import com.panda.pda.mes.user.data.model.LoginDataModel
import com.panda.pda.mes.user.data.model.LoginRequest
import com.panda.pda.mes.user.data.model.PwdCheckRequest
import com.panda.pda.mes.user.data.model.PwdModifyRequest
import io.reactivex.rxjava3.core.Single
import okhttp3.RequestBody
import org.json.JSONObject
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
    ): Single<Any>
    /**
     * 修改密码/修改
     *
     * The endpoint is owned by docs service owner
     * @param root (optional)
     */
    @POST("pda/admin/user/password/modify")
    fun pdaAdminUserPasswordModifyPost(
        @retrofit2.http.Body request: PwdModifyRequest
    ): Single<Any>

    @Headers("Content-Type: application/json")
    @POST("pda/admin/user/code-login")
    fun qrCodeLoginPost(@retrofit2.http.Body request: String): Single<LoginDataModel>
}