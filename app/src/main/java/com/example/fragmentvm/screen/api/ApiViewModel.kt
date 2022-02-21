package com.example.fragmentvm.screen.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fragmentvm.App
import com.example.fragmentvm.data.DataStoreRepository
import com.example.fragmentvm.model.BackendResponse
import com.example.fragmentvm.network.RetrofitRepository
import com.example.fragmentvm.utils.SingleLiveEvent
import com.example.fragmentvm.utils.Util
import com.example.fragmentvm.utils.Util.skipFirst
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okio.IOException
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class ApiViewModel : ViewModel() {
    private var _isSuccessRequest = MutableLiveData(false)
    fun getIsSuccessRequest(): LiveData<Boolean> = _isSuccessRequest

    private var _errorLiveData = SingleLiveEvent<BackendResponse>()
    fun getErrorLiveData(): SingleLiveEvent<BackendResponse> = _errorLiveData

    private var _isValidApiKey = MutableLiveData(false)
    fun getIsValidApiKey(): LiveData<Boolean> = _isValidApiKey.skipFirst()

    @Inject
    lateinit var retrofitRepository: RetrofitRepository

    @Inject
    lateinit var ds: DataStoreRepository

    init {
        App.instance().appGraph.embed(this)
    }

    fun updateApiKey(data: String) {
        val isValidKey = Util.isNotEmpty(data)
        if (isValidKey) {
            viewModelScope.launch {
                ds.putString("apikey", data)
            }
        }
        _isValidApiKey.postValue(isValidKey)
    }

    fun sendRequest() {
        val apikey = runBlocking { ds.getString("apikey") }
        retrofitRepository.getFavourites(apikey.toString())
            .subscribe({
                viewModelScope.launch {
                    ds.putBool("flagReg", true)
                }
                _isSuccessRequest.postValue(true)
            }, {
                _isSuccessRequest.postValue(false)
                if (it is HttpException) {
                    val body = it.response()?.errorBody()
                    val gson = Gson()
                    val adapter: TypeAdapter<BackendResponse> =
                        gson.getAdapter(BackendResponse::class.java)
                    try {
                        val error: BackendResponse =
                            adapter.fromJson(body?.string())
                        _errorLiveData.postValue(error)
                    } catch (e: IOException) {
                        Timber.e(e)
                    }
                }
            })
    }
}