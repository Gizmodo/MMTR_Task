package com.example.fragmentvm.repository

import androidx.lifecycle.MutableLiveData
import com.example.fragmentvm.di.RetroServiceInterface
import com.example.fragmentvm.model.Cat
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject


class CatRepository @Inject constructor(private val apiService: RetroServiceInterface) {
    val getCats: MutableLiveData<List<Cat>>
        get() {
            val cats = MutableLiveData<List<Cat>>()
            apiService.getCats()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    cats.value = result
                }, { error ->
                    Timber.e(error)
                })
            return cats
        }
}
