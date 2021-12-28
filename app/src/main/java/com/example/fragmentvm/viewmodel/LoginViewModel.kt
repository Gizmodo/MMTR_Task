package com.example.fragmentvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fragmentvm.App
import com.example.fragmentvm.model.Payload
import com.example.fragmentvm.model.Signup
import com.example.fragmentvm.repository.RepositoryRetrofit
import com.example.fragmentvm.repository.RepositorySharedPref
import com.example.fragmentvm.utils.CombinedLiveData
import com.example.fragmentvm.utils.Util.Companion.skipFirst
import com.example.fragmentvm.utils.Validator
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import okio.IOException
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject


class LoginViewModel : ViewModel() {

    private var _isValidDescription = MutableLiveData<Boolean>()
    private var _isValidEmail = MutableLiveData<Boolean>()

    val isValidEmail: MutableLiveData<Boolean> get() = _isValidEmail.skipFirst()
    val isValidDescription: MutableLiveData<Boolean> get() = _isValidDescription.skipFirst()
    val isValidForm: LiveData<Boolean> get() = _combined

    private val payload: Payload

    init {
        App.instance().appGraph.embed(this)
        this._isValidEmail.value = false
        this._isValidDescription.value = false
        this.payload = Payload(sharedRepo.description, sharedRepo.email)
    }

    @Inject
    lateinit var sharedRepo: RepositorySharedPref

    @Inject
    lateinit var repositoryRetrofit: RepositoryRetrofit

    private var _signUpLiveData = MutableLiveData<Signup>()
    val signUpLiveData: LiveData<Signup>
        get() = _signUpLiveData

    fun postRequest() {
        repositoryRetrofit.postSignUp(payload)
            .subscribe({
                _signUpLiveData.value = it
            }, {
                if (it is HttpException) {
                    val body = it.response()?.errorBody()
                    val gson = Gson()
                    val adapter: TypeAdapter<Signup> =
                        gson.getAdapter(Signup::class.java)
                    try {
                        val error: Signup =
                            adapter.fromJson(body?.string())
                        _signUpLiveData.value = error
                    } catch (e: IOException) {
                        Timber.e(e)
                    }
                }
            })
    }

    private var _combined: LiveData<Boolean> =
        CombinedLiveData.combine(_isValidEmail, _isValidDescription)
        { left, right ->
            return@combine left.and(right)
        }

    fun updateEmail(data: String) {
        val isValidEmail = Validator.isEmailValid(data)
        if (isValidEmail) sharedRepo.email = data
        _isValidEmail.postValue(isValidEmail)
    }

    fun updateDescription(data: String) {
        val isValidDescription = Validator.isNotEmpty(data)
        if (isValidDescription) sharedRepo.description = data
        _isValidDescription.postValue(isValidDescription)
    }
}