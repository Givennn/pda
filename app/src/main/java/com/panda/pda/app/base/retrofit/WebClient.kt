package com.panda.pda.app.base.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * created by AnJiwei 2020/10/15
 */


object WebClient {

    private val client by lazy { buildOkHttpClient() }

    private fun buildOkHttpClient() : OkHttpClient {

        return OkHttpClient.Builder()
            .connectTimeout(NetworkParams.CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(NetworkParams.READ_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(NetworkParams.WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
            .addInterceptor(QueryParamInterceptor())
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    fun<TService> request(serviceClass: Class<TService>) : TService {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(NetworkParams.SERVICE_URL)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(serviceClass)
    }
}

