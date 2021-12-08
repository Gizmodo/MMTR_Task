package com.example.fragmentvm.ui.second

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fragmentvm.model.Cat
import com.example.fragmentvm.network.RetrofitServices
import com.example.fragmentvm.utils.Common
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class SecondViewModel : ViewModel() {
    private var retrofit: RetrofitServices = Common.retrofitService
    val catsList = MutableLiveData<List<Cat>>()
    val errorMessage = MutableLiveData<String>()

    fun getFiveCats() {
        val response = retrofit.getFiveCats()
        response.enqueue(object : Callback<List<Cat>> {
            override fun onResponse(call: Call<List<Cat>>, response: Response<List<Cat>>) {
                catsList.postValue(response.body())
            }

            override fun onFailure(call: Call<List<Cat>>, t: Throwable) {
                Timber.e(t.message)
                errorMessage.postValue(t.message)
            }
        })
    }
}