package com.example.fragmentvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fragmentvm.App
import com.example.fragmentvm.repository.RepositoryRetrofit
import com.example.fragmentvm.repository.RepositorySharedPref
import com.example.fragmentvm.utils.Util.Companion.skipFirst
import com.example.fragmentvm.utils.Validator
import javax.inject.Inject

class ApiViewModel : ViewModel() {
    @Inject
    lateinit var repositoryRetrofit: RepositoryRetrofit

    @Inject
    lateinit var sharedRepo: RepositorySharedPref

    private val _apikey = MutableLiveData<String>()

    private val _isValidApiKey = MutableLiveData<Boolean>()
    val isValidApiKey: MutableLiveData<Boolean>
        get() = _isValidApiKey.skipFirst()

    val apikey: LiveData<String>
        get() = _apikey

    init {
        App.instance().appGraph.embed(this)
    }

    fun updateApiKey(data: String) {
        // TODO: Save to prefs
        _isValidApiKey.postValue(Validator.isNotEmpty(data))
    }

}