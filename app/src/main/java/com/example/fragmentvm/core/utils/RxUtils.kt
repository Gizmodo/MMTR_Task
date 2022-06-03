package com.example.fragmentvm.core.utils

import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers

object RxUtils {
    fun <T : Any> applySubscriberScheduler(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.io())
        }
    }
}
