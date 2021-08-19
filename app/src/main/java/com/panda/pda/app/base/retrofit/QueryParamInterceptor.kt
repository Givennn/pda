package com.panda.pda.app.base.retrofit

import okhttp3.Interceptor
import okhttp3.Response

/**
 * created by AnJiwei 2020/10/15
 */
class QueryParamInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain
        .request()
        .newBuilder()
        .removeHeader("User_Agent")
        .addHeader("Content_type", "application/json")
        .addHeader("os", "Android")
        .build()
        .let { chain.proceed(it) }
}