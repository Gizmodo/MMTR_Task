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

    /* @Inject
     private val apiService: RetroServiceInterface = api*/

    val getCatsLD: MutableLiveData<MutableList<Cat>>
        get() {
            val response = apiService.getFiveCatsLD()
            var data: MutableLiveData<MutableList<Cat>> = MutableLiveData<MutableList<Cat>>()
            val liveDataList: MutableLiveData<MutableList<Cat>>
            Timber.d("Send request")
            response.enqueue(object : Callback<MutableLiveData<MutableList<Cat>>> {
                override fun onResponse(
                    call: Call<MutableLiveData<MutableList<Cat>>>,
                    response: Response<MutableLiveData<MutableList<Cat>>>
                ) {
                    if (response.isSuccessful) {
                        data = response.body()!!
                    }
                }

                override fun onFailure(
                    call: Call<MutableLiveData<MutableList<Cat>>>,
                    t: Throwable
                ) {
                    Timber.e(t.message)
//                    data.postValue(null)
                }

            })
            return data
        }
}
