package com.panda.pda.app.common.data

import com.panda.pda.app.base.retrofit.BaseResponse
import com.panda.pda.app.base.retrofit.DataListNode
import com.panda.pda.app.common.data.model.DataParamModel
import com.panda.pda.app.common.data.model.FileInfoModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import retrofit2.http.*
import java.io.File


/**
 * created by AnJiwei 2021/9/1
 */
interface CommonApi {
    /**
     * 文件上传
     *
     * @param file (required)
     */

    @Headers(
        "Content-Type:  multipart/form-data"
    )
    @retrofit2.http.Multipart
    @POST("pda/common/upload-file")
    fun pdaCommonUploadFilePost(
        @retrofit2.http.Part("file\"; filename=\"file") file: File,
    ): Single<BaseResponse<FileInfoModel>>

    /**
     * 查询数据字典
     *
     * The endpoint is owned by docs service owner
     */
    @GET("pda/config/sys-param/list-by-param")
    fun pdaConfigSysParamListByParamGet(
    ): Single<BaseResponse<DataListNode<DataParamModel>>>

//    var file: File = File(imageUri.getPath())
//
//    var fbody: RequestBody = create(MediaType.parse("image/*"),
//        file)
//
//    var name: RequestBody = create(MediaType.parse("text/plain"),
//        firstNameField.getText()
//            .toString())
//
//    var id: RequestBody = create(MediaType.parse("text/plain"),
//        AZUtils.getUserId(this))
//
//    var call: Call<com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion.User> =
//        org.gradle.internal.impldep.org.bouncycastle.crypto.tls.ConnectionEnd.client.editUser(
//            AZUtils.getToken(this),
//            fbody,
//            name,
//            id)

    @Streaming
    @GET
    fun downloadFile(@Url fileUrl: String): Observable<retrofit2.Response<ResponseBody>>
}