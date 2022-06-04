package com.example.fragmentvm.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fragmentvm.App
import com.example.fragmentvm.core.utils.Constants.DataStore.KEY_API
import com.example.fragmentvm.core.utils.Constants.DataStore.KEY_FLAGREG
import com.example.fragmentvm.core.utils.NetworkResult
import com.example.fragmentvm.core.utils.SingleLiveEvent
import com.example.fragmentvm.core.utils.Util.skipFirst
import com.example.fragmentvm.data.repository.CatRepository
import com.example.fragmentvm.domain.DataStoreInterface
import com.example.fragmentvm.domain.model.favourite.FavCatListDomain
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class ApiViewModel : ViewModel() {
    private var _isSuccessRequest = MutableLiveData(false)
    fun getIsSuccessRequest(): LiveData<Boolean> = _isSuccessRequest

    private var _errorLiveData = SingleLiveEvent<NetworkResult<FavCatListDomain>>()
    fun getErrorLiveData() = _errorLiveData

    private var _isValidApiKey = MutableLiveData(false)
    fun getIsValidApiKey(): LiveData<Boolean> = _isValidApiKey.skipFirst()

    @Inject
    lateinit var catRepository: CatRepository

    @Inject
    lateinit var ds: DataStoreInterface
    private var _exceptionMessage = MutableLiveData<String>()
    val exceptionMessage: LiveData<String> get() = _exceptionMessage

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onException(throwable)
    }

    private fun onException(throwable: Throwable) {
        Timber.e(throwable)
        _exceptionMessage.postValue(throwable.message)
    }

    init {
        App.instance().appGraph.embed(this)
    }

    fun updateApiKey(data: String) {
        val isValidKey = data.isNotEmpty()
        if (isValidKey) {
            viewModelScope.launch {
                ds.putString(KEY_API, data)
            }
        }
        _isValidApiKey.postValue(isValidKey)
    }

    fun sendApiKey() {
        val apikey = runBlocking { ds.getString(KEY_API) }
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            when (val response = catRepository.sendApiKey(
                apikey.toString()
            )) {
                is NetworkResult.Error -> {
                    _isSuccessRequest.postValue(false)
                    _errorLiveData.postValue(response)
                }
                is NetworkResult.Exception -> Timber.e(response.e)
                is NetworkResult.Success -> {
                    ds.putBool(KEY_FLAGREG, true)
                    _isSuccessRequest.postValue(true)
                }
            }
        }
    }
}
