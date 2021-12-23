package com.example.fragmentvm.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fragmentvm.di.RetroServiceInterface
import com.example.fragmentvm.model.signupResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class UserRepository @Inject constructor(private val apiService: RetroServiceInterface) {
    val postForm: LiveData<signupResponse>
        get() {
            val response = MutableLiveData<signupResponse>()
            apiService.signUp(email, descr)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    response.value = result

                }, { error -> Timber.e(error) })
            return response
        }
}