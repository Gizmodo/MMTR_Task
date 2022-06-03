package com.example.fragmentvm.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fragmentvm.App
import com.example.fragmentvm.R
import com.example.fragmentvm.core.utils.Constants.DataStore.KEY_DESCRIPTION
import com.example.fragmentvm.core.utils.Constants.DataStore.KEY_EMAIL
import com.example.fragmentvm.core.utils.NetworkResult
import com.example.fragmentvm.core.utils.StatefulData
import com.example.fragmentvm.core.utils.UiText
import com.example.fragmentvm.core.utils.Util
import com.example.fragmentvm.core.utils.Util.isEmail
import com.example.fragmentvm.core.utils.Util.skipFirst
import com.example.fragmentvm.data.repository.CatRepository
import com.example.fragmentvm.domain.DataStoreInterface
import com.example.fragmentvm.domain.model.BackendResponseDomain
import com.example.fragmentvm.domain.model.login.LoginDomain
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
    lateinit var ds: DataStoreInterface

    @Inject
    lateinit var catRepository: CatRepository

    private val _signupState =
        MutableStateFlow<StatefulData<BackendResponseDomain>>(StatefulData.Loading)
    val signupStateFlow: StateFlow<StatefulData<BackendResponseDomain>>
        get() = _signupState

    private var _exceptionMessage = MutableLiveData<String>()
    val exceptionMessage: LiveData<String> get() = _exceptionMessage

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onException(throwable)
    }

    private fun onException(throwable: Throwable) {
        Timber.e(throwable)
        _exceptionMessage.postValue(throwable.message)
    }

    fun postRequest() {
        val desc = runBlocking { ds.getString(KEY_DESCRIPTION) }
        val eml = runBlocking { ds.getString(KEY_EMAIL) }
        val loginModel = LoginDomain(desc.toString(), eml.toString())
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            when (val response = catRepository.postSignUp(loginModel)) {
                is NetworkResult.Error -> {
                    _signupState.value = StatefulData.ErrorUiText(
                        UiText.StringResource(
                            resId = R.string.signup_error,
                            response.message.toString()
                        )
                    )
                }
                is NetworkResult.Exception -> {
                    Timber.e(response.e)
                }
                is NetworkResult.Success -> {
                    _signupState.value = StatefulData.Success(response.data)
                }
            }
        }
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
