package com.example.fragmentvm.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fragmentvm.model.Cat
import com.example.fragmentvm.network.RetrofitServices
import com.example.fragmentvm.utils.Common
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class MainViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    private var retrofit: RetrofitServices = Common.retrofitService
    val catList = MutableLiveData<MutableList<Cat>?>()
    lateinit var oldCatList: MutableList<Cat>
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
    fun getCatsWithRxJava() {
        retrofit.getFiveCatsRxJava(10, "small")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ result ->
                val url = result.firstOrNull()?.url
                url.let {
                    Timber.d("RxJava result - $it")
                    urlCat.postValue(it)
                }
            }, { error -> Timber.e(error) })
    }

    fun getCats() {
        retrofit.getCatList().enqueue(object : Callback<MutableList<Cat>> {
            override fun onResponse(
                call: Call<MutableList<Cat>>,
                response: Response<MutableList<Cat>>
            ) {
                if (response.isSuccessful) {
                    val url = response.body()?.get(0)?.url
                    urlCat.postValue(url)
                }
            }

            override fun onFailure(call: Call<MutableList<Cat>>, t: Throwable) {
                Timber.e(t.message.toString())
            }
        })
    }
}