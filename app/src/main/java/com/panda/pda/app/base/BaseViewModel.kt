package com.panda.pda.app.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.panda.pda.app.base.retrofit.BaseResponse
import com.panda.pda.app.base.retrofit.HttpInnerException
import com.panda.pda.app.base.retrofit.NetworkParams
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

/**
 * created by AnJiwei 2020/10/29
 */
interface ILoadingViewModel {

    val isLoading: MutableLiveData<Pair<Boolean, String?>>
    val errorMessage: MutableLiveData<String>

}

internal fun <T> Single<T>.bindLoadingStatus(viewModel: ILoadingViewModel): Single<T> {

    return this.doOnSubscribe { viewModel.isLoading.postValue(Pair(true, null)) }
        .doOnError { viewModel.errorMessage.postValue(it.message) }
        .doFinally { viewModel.isLoading.postValue(Pair(false, null)) }
}

internal fun Completable.bindLoadingStatus(viewModel: ILoadingViewModel): Completable {
    return this.doOnSubscribe { viewModel.isLoading.postValue(Pair(true, null)) }
        .doOnError { viewModel.errorMessage.postValue(it.message) }
        .doFinally { viewModel.isLoading.postValue(Pair(false, null)) }
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

abstract class AsyncAppViewModel(app: Application) : AndroidViewModel(app), ILoadingViewModel {
    override val isLoading = MutableLiveData<Pair<Boolean, String?>>()
    override val errorMessage = MutableLiveData<String>()
}