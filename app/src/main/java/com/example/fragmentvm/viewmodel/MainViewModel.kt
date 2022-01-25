package com.example.fragmentvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fragmentvm.App
import com.example.fragmentvm.model.Cat
import com.example.fragmentvm.repository.DataStoreRepositoryImpl
import com.example.fragmentvm.repository.RepositoryRetrofit
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class MainViewModel : ViewModel() {
    init {
        App.instance().appGraph.embed(this)
        getCats()
    }

    @Inject
    lateinit var repositoryRetrofit: RepositoryRetrofit

    @Inject
    lateinit var ds: DataStoreRepositoryImpl

    private var _cats = MutableLiveData<List<Cat>>()
    val cats: LiveData<List<Cat>>
        get() = _cats

    private fun getCats() {
        val apikey = runBlocking { ds.getString("apikey") }
        repositoryRetrofit.getCats(apikey.toString())
            .subscribe({ _cats.postValue(it) }, { Timber.e(it) })
    }
}