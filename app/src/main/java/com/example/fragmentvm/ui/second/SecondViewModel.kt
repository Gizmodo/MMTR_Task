package com.example.fragmentvm.ui.second

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fragmentvm.model.CatItem
import com.example.fragmentvm.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class SecondViewModel constructor(protected val repository: MainRepository) : ViewModel() {
    val catsList = MutableLiveData<List<CatItem>>()
    val errorMessage = MutableLiveData<String>()

    fun getFiveCats() {
        val response = repository.getFiveRandomCats()
        response.enqueue(object : Callback<List<CatItem>> {
            override fun onResponse(call: Call<List<CatItem>>, response: Response<List<CatItem>>) {
                catsList.postValue(response.body())
            }

            override fun onFailure(call: Call<List<CatItem>>, t: Throwable) {
                Timber.e(t.message)
                errorMessage.postValue(t.message)
            }
        })
    }
}