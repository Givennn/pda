package com.panda.pda.app.common.data

import com.panda.pda.app.base.retrofit.BaseResponse
import com.panda.pda.app.common.data.model.FileInfoModel
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Headers
import retrofit2.http.POST
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
}