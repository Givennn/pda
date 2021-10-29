package com.panda.pda.app.base.retrofit

import com.squareup.moshi.Types
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.Type


/**
 * created by AnJiwei 2021/9/24
 */


class BaseResponseConverter(private val delegate: Converter<ResponseBody, BaseResponse<*>>) :
    Converter<ResponseBody, Any> {

    @Throws(IOException::class)
    override fun convert(value: ResponseBody): Any? {
        val baseResponse: BaseResponse<*> =
            delegate.convert(value) ?: throw IOException("baseResponse convert error")
        if (baseResponse.code != NetworkParams.SUCCESS_CODE) {
            throw HttpInnerException(baseResponse)
        } else if (baseResponse.data == null) {
            return Unit
        }
        return baseResponse.data
    }
}

class BaseResponseConverterFactory : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val baseResponseType: Type = Types.newParameterizedType(BaseResponse::class.java, type)
        val delegate: Converter<ResponseBody, BaseResponse<*>> = retrofit.nextResponseBodyConverter(
            this, baseResponseType, annotations
        )
        return BaseResponseConverter(delegate)
    }
}