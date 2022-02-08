package com.example.fragmentvm.base

import androidx.lifecycle.MutableLiveData
import com.example.fragmentvm.App
import com.example.fragmentvm.model.Cat
import com.example.fragmentvm.repository.DataStoreRepositoryImpl
import com.example.fragmentvm.repository.RepositoryRetrofit
import javax.inject.Inject

class cavm : BaseViewModel() {
    @Inject
    lateinit var retrofit: RepositoryRetrofit

    @Inject
    lateinit var ds: DataStoreRepositoryImpl
    val currentWeatherLiveData = MutableLiveData<Cat>()
    init {
        App.instance().appGraph.embed(this)
    }
    fun fetchCats(){
        disposable.add(
            repositoryCurrent.fetchCurrentWeather()
                .safeSubscribe(currentWeatherLiveData::postValue)
        )
    }
}