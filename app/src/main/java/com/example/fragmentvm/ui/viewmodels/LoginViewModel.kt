package com.example.fragmentvm.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fragmentvm.App
import com.example.fragmentvm.core.utils.Constants.DataStore.KEY_DESCRIPTION
import com.example.fragmentvm.core.utils.Constants.DataStore.KEY_EMAIL
import com.example.fragmentvm.core.utils.Util
import com.example.fragmentvm.core.utils.Util.isEmail
import com.example.fragmentvm.core.utils.Util.parseResponseLoginError
import com.example.fragmentvm.core.utils.Util.skipFirst
import com.example.fragmentvm.data.RetrofitRepository
import com.example.fragmentvm.data.model.login.LoginDtoMapper
import com.example.fragmentvm.data.model.login.LoginResponseDto
import com.example.fragmentvm.data.model.login.LoginResponseDtoMapper
import com.example.fragmentvm.domain.DataStoreInterface
import com.example.fragmentvm.domain.model.login.LoginDomain
import com.example.fragmentvm.domain.model.login.LoginResponseDomain
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
    lateinit var ds: DataStoreInterface

    @Inject
    lateinit var retrofitRepository: RetrofitRepository

    private var _signUpLiveData = MutableLiveData<LoginResponseDomain>()
    val signUpLiveData: LiveData<LoginResponseDomain>
        get() = _signUpLiveData

    fun postRequest() {
        val desc = runBlocking { ds.getString(KEY_DESCRIPTION) }
        val eml = runBlocking { ds.getString(KEY_EMAIL) }
        val loginModel =
            LoginDtoMapper().mapFromDomainModel(LoginDomain(desc.toString(), eml.toString()))
        retrofitRepository.postSignUp(loginModel)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _signUpLiveData.value = LoginResponseDtoMapper().mapToDomainModel(it)
            }, {
                if (it is HttpException) {
                    parseResponseLoginError(it.response()
                        ?.errorBody()).let { error: LoginResponseDto ->
                        _signUpLiveData.value = LoginResponseDtoMapper().mapToDomainModel(error)
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