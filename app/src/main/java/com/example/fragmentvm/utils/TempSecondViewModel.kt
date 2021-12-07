package com.example.fragmentvm.utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fragmentvm.model.Cat
import com.example.fragmentvm.network.RetrofitServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class TempSecondViewModel : ViewModel() {
    private var retrofit: RetrofitServices = Common.retrofitService
    var catList = MutableLiveData<List<Cat>?>()

    fun getCatsList() {
        retrofit.getCatListRV().enqueue(object : Callback<MutableList<Cat>> {
            override fun onResponse(
                call: Call<MutableList<Cat>>,
                response: Response<MutableList<Cat>>
            ) {
                if (response.isSuccessful) {
                    val url = response.body()?.get(0)?.url
                    val list = response.body()
                    catList.postValue(list)
                }
            }

            override fun onFailure(call: Call<MutableList<Cat>>, t: Throwable) {
                Timber.e(t.message.toString())
            }
        })
    }
}