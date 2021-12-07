package com.example.fragmentvm.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fragmentvm.model.CatItem
import com.example.fragmentvm.network.RetrofitServices
import com.example.fragmentvm.utils.Common
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class MainViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    private var mService: RetrofitServices = Common.retrofitService
    val catList = MutableLiveData<MutableList<CatItem>?>()
    lateinit var oldCatList: MutableList<CatItem>
    var urlCat: MutableLiveData<String?> = MutableLiveData<String?>().apply { value = "" }

   /* fun newRequest() {
        mService.getCatList2().enqueue(object : Callback<LiveData<MutableList<CatItem>>> {
            override fun onResponse(
                call: Call<LiveData<MutableList<CatItem>>>,
                response: Response<LiveData<MutableList<CatItem>>>
            ) {
                if (response.isSuccessful) {
                    val res = response.body()?.value
                    Timber.d(res.toString())
                    catList.postValue(res)
                }
            }

            override fun onFailure(call: Call<LiveData<MutableList<CatItem>>>, t: Throwable) {
                Timber.e(t.message.toString())
            }
        })
    }
*/
    fun getCats() {
        mService.getCatList().enqueue(object : Callback<MutableList<CatItem>> {
            override fun onResponse(
                call: Call<MutableList<CatItem>>,
                response: Response<MutableList<CatItem>>
            ) {
                if (response.isSuccessful) {
                    val url = response.body()?.get(0)?.url
                    urlCat.postValue(url)
                }
            }

            override fun onFailure(call: Call<MutableList<CatItem>>, t: Throwable) {
                Timber.e(t.message.toString())
            }
        })
    }
}