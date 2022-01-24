package com.example.fragmentvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fragmentvm.App
import com.example.fragmentvm.model.Signup
import com.example.fragmentvm.repository.DataStoreRepositoryImpl
import com.example.fragmentvm.repository.RepositoryRetrofit
import com.example.fragmentvm.utils.Util.Companion.skipFirst
import com.example.fragmentvm.utils.Validator
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okio.IOException
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class ApiViewModel : ViewModel() {
    private var _isSuccessRequest = MutableLiveData<Boolean>()
    val isSuccessRequest: LiveData<Boolean>
        get() = _isSuccessRequest

    private var _errorLiveData = MutableLiveData<Signup>()
    val errorLiveData: LiveData<Signup>
        get() = _errorLiveData

    @Inject
    lateinit var repositoryRetrofit: RepositoryRetrofit

    @Inject
    lateinit var ds: DataStoreRepositoryImpl

    private var _isValidApiKey = MutableLiveData<Boolean>()
    val isValidApiKey: LiveData<Boolean>
        get() = _isValidApiKey.skipFirst()

    init {
        App.instance().appGraph.embed(this)
        this._isValidApiKey.value = false
        this._isSuccessRequest.value = false
    }

    fun updateApiKey(data: String) {
        val isValidKey = Validator.isNotEmpty(data)
        if (isValidKey) {
            viewModelScope.launch {
                ds.putString("apikey", data)
            }
        }
        _isValidApiKey.postValue(isValidKey)
    }

    fun sendRequest() {
        val apikey = runBlocking { ds.getString("apikey") }
        repositoryRetrofit.getFavourites(apikey.toString())
            .subscribe({
                _isSuccessRequest.postValue(true)
            }, {
                _isSuccessRequest.postValue(false)
                if (it is HttpException) {
                    val body = it.response()?.errorBody()
                    val gson = Gson()
                    val adapter: TypeAdapter<Signup> =
                        gson.getAdapter(Signup::class.java)
                    try {
                        val error: Signup =
                            adapter.fromJson(body?.string())
                        _errorLiveData.value = error
                    } catch (e: IOException) {
                        Timber.e(e)
                    }
                }
            })
    }
}