package com.example.fragmentvm.repository

import androidx.lifecycle.MutableLiveData
import com.example.fragmentvm.di.RetroServiceInterface
import com.example.fragmentvm.model.Cat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class CatRepository @Inject constructor(private val apiService: RetroServiceInterface) {
    val getCats: MutableLiveData<List<Cat>>
        get() {
            val cats = MutableLiveData<List<Cat>>()
            val response = apiService.getCats()
            response.enqueue(object : Callback<List<Cat>> {
                override fun onResponse(call: Call<List<Cat>>, response: Response<List<Cat>>) {
                    cats.postValue(response.body())
                }

                override fun onFailure(call: Call<List<Cat>>, t: Throwable) {
                    Timber.e(t.message)
                }
            })
            return cats
        }
}
