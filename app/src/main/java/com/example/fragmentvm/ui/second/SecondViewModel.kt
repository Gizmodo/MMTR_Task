package com.example.fragmentvm.ui.second

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fragmentvm.Common
import com.example.fragmentvm.model.CatItem
import com.example.fragmentvm.network.RetrofitServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class SecondViewModel : ViewModel() {
    private var retrofit: RetrofitServices = Common.retrofitService
    var catList = MutableLiveData<List<CatItem>?>()

    fun getCatsList() {
        retrofit.getCatListRV().enqueue(object : Callback<MutableList<CatItem>> {
            override fun onResponse(
                call: Call<MutableList<CatItem>>,
                response: Response<MutableList<CatItem>>
            ) {
                if (response.isSuccessful) {
                    val url = response.body()?.get(0)?.url
                    val list = response.body()
                    catList.postValue(list)
                }
            }

            override fun onFailure(call: Call<MutableList<CatItem>>, t: Throwable) {
                Timber.e(t.message.toString())
            }
        })
    }
}