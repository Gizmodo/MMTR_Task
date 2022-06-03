package com.example.fragmentvm.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fragmentvm.App
import com.example.fragmentvm.core.utils.Constants.DataStore.KEY_API
import com.example.fragmentvm.core.utils.Constants.DataStore.KEY_FLAGREG
import com.example.fragmentvm.core.utils.SingleLiveEvent
import com.example.fragmentvm.core.utils.Util.parseBackendResponseError
import com.example.fragmentvm.core.utils.Util.skipFirst
import com.example.fragmentvm.data.model.response.BackendResponseDto
import com.example.fragmentvm.data.model.response.BackendResponseDtoMapper
import com.example.fragmentvm.data.repository.CatRepository
import com.example.fragmentvm.domain.DataStoreInterface
import com.example.fragmentvm.domain.model.BackendResponseDomain
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException

class ApiViewModel : ViewModel() {
    private var _isSuccessRequest = MutableLiveData(false)
    fun getIsSuccessRequest(): LiveData<Boolean> = _isSuccessRequest

    private var _errorLiveData = SingleLiveEvent<BackendResponseDomain>()
    fun getErrorLiveData(): SingleLiveEvent<BackendResponseDomain> = _errorLiveData

    private var _isValidApiKey = MutableLiveData(false)
    fun getIsValidApiKey(): LiveData<Boolean> = _isValidApiKey.skipFirst()

    @Inject
    lateinit var catRepository: CatRepository

    @Inject
    lateinit var ds: DataStoreInterface

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
        catRepository.sendApiKey(apikey.toString())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewModelScope.launch {
                    ds.putBool(KEY_FLAGREG, true)
                }
                _isSuccessRequest.postValue(true)
            }, {
                _isSuccessRequest.postValue(false)
                if (it is HttpException) {
                    parseBackendResponseError(
                        it.response()
                            ?.errorBody()
                    ).let { error: BackendResponseDto ->
                        _errorLiveData.postValue(BackendResponseDtoMapper().mapToDomainModel(error))
                    }
                }
            })
    }
}
