package com.example.fragmentvm.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber


abstract class BaseViewModel : ViewModel() {
    protected val disposable = CompositeDisposable()

    val errorLivaData = MutableLiveData<Throwable>()

    protected fun <T> Single<T>.safeSubscribe(onSuccess: (T) -> Unit) =
        subscribeOn(Schedulers.io()).subscribe({ onSuccess(it, onSuccess) }, ::onErrorHandled)

    protected fun <T> Maybe<T>.safeSubscribe(onSuccess: (T) -> Unit) =
        subscribeOn(Schedulers.io()).subscribe({ onSuccess(it, onSuccess) }, ::onErrorHandled)

    protected fun Completable.safeSubscribe(onSuccess: () -> Unit) =
        subscribeOn(Schedulers.io()).subscribe(
            { onSuccessCompletable(onSuccess) },
            ::onErrorHandled
        )

    protected fun <T> Observable<T>.safeSubscribe(onSuccess: (T) -> Unit) =
        subscribeOn(Schedulers.io()).subscribe({ onSuccess(it, onSuccess) }, ::onErrorHandled)

    private fun onErrorHandled(t: Throwable) {
        t.printStackTrace()
        errorLivaData.postValue(t)
    }

    private fun <T> onSuccess(value: T, action: (T) -> Unit) {
        Timber.d("onSuccess", value.toString())
        action.invoke(value)
    }

    private fun onSuccessCompletable(action: () -> Unit) {
        Timber.d("completable")
        action.invoke()
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}