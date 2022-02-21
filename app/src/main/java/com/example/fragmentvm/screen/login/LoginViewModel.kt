package com.example.fragmentvm.screen.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fragmentvm.App
import com.example.fragmentvm.data.DataStoreRepository
import com.example.fragmentvm.model.BackendResponse
import com.example.fragmentvm.model.LoginPayload
import com.example.fragmentvm.network.RetrofitRepository
import com.example.fragmentvm.utils.Constants.DataStore.KEY_DESCRIPTION
import com.example.fragmentvm.utils.Constants.DataStore.KEY_EMAIL
import com.example.fragmentvm.utils.Util
import com.example.fragmentvm.utils.Util.skipFirst
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okio.IOException
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class LoginViewModel : ViewModel() {

    private var _isValidDescription = MutableLiveData(false)
    private var _isValidEmail = MutableLiveData(false)

    val isValidEmail: MutableLiveData<Boolean> get() = _isValidEmail.skipFirst()
    val isValidDescription: MutableLiveData<Boolean> get() = _isValidDescription.skipFirst()
    val isValidForm: LiveData<Boolean> get() = _combined

    init {
        App.instance().appGraph.embed(this)
    }

    @Inject
    lateinit var ds: DataStoreRepository

    @Inject
    lateinit var retrofitRepository: RetrofitRepository

    private var _signUpLiveData = MutableLiveData<BackendResponse>()
    val signUpLiveData: LiveData<BackendResponse>
        get() = _signUpLiveData

    fun postRequest() {
        val desc = runBlocking { ds.getString(KEY_DESCRIPTION) }
        val eml = runBlocking { ds.getString(KEY_EMAIL) }
        val loginPayload = LoginPayload(desc.toString(), eml.toString())
        retrofitRepository.postSignUp(loginPayload)
            .observeOn(Schedulers.io())
            .subscribe({
                _signUpLiveData.value = it
            }, {
                if (it is HttpException) {
                    val body = it.response()?.errorBody()
                    val gson = Gson()
                    val adapter: TypeAdapter<BackendResponse> =
                        gson.getAdapter(BackendResponse::class.java)
                    try {
                        val error: BackendResponse =
                            adapter.fromJson(body?.string())
                        _signUpLiveData.value = error
                    } catch (e: IOException) {
                        Timber.e(e)
                    }
                }
            })
    }

    private var _combined: LiveData<Boolean> =
        Util.combine(_isValidEmail, _isValidDescription)
        { left, right ->
            return@combine left.and(right)
        }

    fun updateEmail(data: String) {
        val isValidEmail = Util.isEmailValid(data)
        if (isValidEmail) viewModelScope.launch { ds.putString(KEY_EMAIL, data) }
        _isValidEmail.postValue(isValidEmail)
    }

    fun updateDescription(data: String) {
        val isValidDescription = Util.isNotEmpty(data)
        if (isValidDescription) viewModelScope.launch { ds.putString(KEY_DESCRIPTION, data) }
        _isValidDescription.postValue(isValidDescription)
    }
}