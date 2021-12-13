package com.example.fragmentvm.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fragmentvm.model.Cat
import com.example.fragmentvm.network.RetrofitServices
import com.example.fragmentvm.utils.Common
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class MainVM : ViewModel() {
    private var retrofit: RetrofitServices = Common.retrofitService
    val cats = MutableLiveData<List<Cat>>()
    val errors = MutableLiveData<String>()

    fun getCats() {
        val response = retrofit.getFiveCats()
        response.enqueue(object : Callback<List<Cat>> {
            override fun onResponse(call: Call<List<Cat>>, response: Response<List<Cat>>) {
                cats.postValue(response.body())
            }

            override fun onFailure(call: Call<List<Cat>>, t: Throwable) {
                Timber.e(t.message)
                errors.postValue(t.message)
            }
        })
    }
}