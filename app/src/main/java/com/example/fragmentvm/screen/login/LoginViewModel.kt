package com.example.fragmentvm.screen.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fragmentvm.App
import com.example.fragmentvm.datastore.DataStoreRepository
import com.example.fragmentvm.model.backend.BackendResponse
import com.example.fragmentvm.network.RetrofitRepository
import com.example.fragmentvm.utils.Constants.DataStore.KEY_DESCRIPTION
import com.example.fragmentvm.utils.Constants.DataStore.KEY_EMAIL
import com.example.fragmentvm.utils.Util
import com.example.fragmentvm.utils.Util.isEmail
import com.example.fragmentvm.utils.Util.parseResponseError
import com.example.fragmentvm.utils.Util.skipFirst
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
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
        val loginModel = LoginModel(desc.toString(), eml.toString())
        retrofitRepository.postSignUp(loginModel)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _signUpLiveData.value = it
            }, {
                if (it is HttpException) {
                    parseResponseError(it.response()?.errorBody())?.let { error ->
                        _signUpLiveData.value = error
                    }
                }
            })
    }

    private var _combined: LiveData<Boolean> =
        Util.combine(_isValidEmail, _isValidDescription) { left, right ->
            return@combine left.and(right)
        }

    fun updateEmail(data: String) {
        val isValidEmail = data.isEmail()
        if (isValidEmail) viewModelScope.launch { ds.putString(KEY_EMAIL, data) }
        _isValidEmail.postValue(isValidEmail)
    }

    fun updateDescription(data: String) {
        val isValidDescription = data.isNotEmpty()
        if (isValidDescription) viewModelScope.launch { ds.putString(KEY_DESCRIPTION, data) }
        _isValidDescription.postValue(isValidDescription)
    }
}