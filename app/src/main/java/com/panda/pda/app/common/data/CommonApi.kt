package com.panda.pda.app.common.data

import com.panda.pda.app.BuildConfig
import com.panda.pda.app.base.retrofit.DataListNode
import com.panda.pda.app.common.data.model.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*


/**
 * created by AnJiwei 2021/9/1
 */
interface CommonApi {
    /**
     * 文件上传
     *
     * @param file (required)
     */

    @Multipart
    @POST("pda/common/upload-file")
    fun pdaCommonUploadFilePost(
        @Part file: MultipartBody.Part
    ): Single<FileInfoModel>

    /**
     * 查询数据字典
     *
     * The endpoint is owned by docs service owner
     */
    @GET("pda/config/sys-param/list-all")
    fun pdaConfigSysParamListByParamGet(
    ): Single<DataParameterModel>


    @Streaming
    @GET(DOWNLOAD_PATH)
    fun downloadFile(
        @retrofit2.http.Query("fileUrl") fileUrl: String,
        @retrofit2.http.Query("fileName") fileName: String,
    ): Observable<retrofit2.Response<ResponseBody>>

    companion object {
        const val DOWNLOAD_PATH = "pda/common/get-file"

        fun getFIleUrl(fileUrl: String, fileName: String): String {
            return "${BuildConfig.GRADLE_API_BASE_URL}$DOWNLOAD_PATH?fileUrl=$fileUrl&fileName=$fileName"
        }
    }

    @GET("pda/admin/menu/get-base")
    fun getAuthorityTree(): Single<DataListNode<AuthorityModel>>

    /**
     * 任务/查询任务数量
     *
     * The endpoint is owned by docs service owner
     * @param raw raw paramter (optional)
     */
    @GET("pda/task/msg-count")
    fun pdaTaskMsgCountGet(
//    ): Single<BaseResponse<TaskMessageCountModel>>
    ): Single<List<TaskMessageCountModel>>

    /**
     * 角色管理/根据部门Id查询子部门及人员信息
     */

    @GET("pda/admin/user/list-org-node")
    fun userListOrgNodeGet(@retrofit2.http.Query("parentId") id: String):Single<DataListNode<OrgNodeModel>>
}