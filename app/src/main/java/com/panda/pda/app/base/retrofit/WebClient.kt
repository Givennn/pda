package com.panda.pda.app.base.retrofit

import com.panda.pda.app.BuildConfig
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
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

    private val downLoadClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(NetworkParams.CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(NetworkParams.READ_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(NetworkParams.WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.HEADERS
            })
            .build()
    }

    private fun buildOkHttpClient(): OkHttpClient {

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



    fun <TService> request(serviceClass: Class<TService>): TService {
        return Retrofit.Builder()
            .client(client)
//            .baseUrl(NetworkParams.SERVICE_URL)
            .baseUrl(BuildConfig.GRADLE_API_BASE_URL)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(serviceClass)
    }

    fun downLoader(): Retrofit {
        return Retrofit.Builder()
            .client(downLoadClient)
            .baseUrl(BuildConfig.GRADLE_API_BASE_URL)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }
}

internal fun <T> Single<T>.onMainThread(): Single<T> {

    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

internal fun Completable.onMainThread(): Completable {

    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

internal fun <T> Single<BaseResponse<T>>.unWrapperData(): Single<T> {
    return this.concatMap {
        when (it.code) {
            NetworkParams.SUCCESS_CODE -> Single.just(it.data)
            else -> Single.error(HttpInnerException(it.code, it.message))
        }
    }
}

internal fun Single<BaseResponse<Any>>.unWrapperData(): Completable {
    return this.concatMapCompletable {
        when (it.code) {
            NetworkParams.SUCCESS_CODE -> Completable.complete()
            else -> Completable.error(HttpInnerException(it.code, it.message))
        }
    }
}