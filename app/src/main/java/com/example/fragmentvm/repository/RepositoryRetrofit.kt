package com.example.fragmentvm.repository

import androidx.lifecycle.MutableLiveData
import com.example.fragmentvm.di.RetroServiceInterface
import com.example.fragmentvm.model.Cat
import com.example.fragmentvm.model.Payload
import com.example.fragmentvm.model.Signup
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject


class RepositoryRetrofit @Inject constructor(
    private val apiService: RetroServiceInterface,
) {
    fun postSignUp(payload: Payload): @NonNull Observable<Signup> {
        return apiService.signUp(payload)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getFavourites(apikey: String): @NonNull Observable<List<Signup>> {
        return apiService.favourites(apikey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    val getCats: MutableLiveData<List<Cat>>
        get() {
            val cats = MutableLiveData<List<Cat>>()
            apiService.getCats()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        cats.value = result
                    },
                    { error ->
                        Timber.e(error)
                    }
                )
            return cats
        }
}
