package com.example.fragmentvm.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fragmentvm.network.RetrofitServices
import com.example.fragmentvm.utils.Common
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber

class MainViewModel : ViewModel() {
    private var retrofit: RetrofitServices = Common.retrofitService
    var urlCat: MutableLiveData<String?> = MutableLiveData<String?>().apply { value = "" }

    fun getCatsWithRxJava() {
        retrofit.getFiveCatsRxJava(10, "small")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ result ->
                val url = result.firstOrNull()?.url
                url.let {
                    Timber.d("RxJava result - $it")
                    urlCat.postValue(it)
                }
            }, { error -> Timber.e(error) })
    }
}