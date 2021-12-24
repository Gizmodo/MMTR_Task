package com.example.fragmentvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fragmentvm.App
import com.example.fragmentvm.repository.RepositoryPrefs
import com.example.fragmentvm.utils.CombinedLiveData
import com.example.fragmentvm.utils.Util.Companion.skipFirst
import com.example.fragmentvm.utils.Validator
import timber.log.Timber
import javax.inject.Inject


class LoginViewModel : ViewModel() {

    private var _isValidDescription = MutableLiveData<Boolean>()
    private var _isValidEmail = MutableLiveData<Boolean>()

    val isValidEmail: MutableLiveData<Boolean> get() = _isValidEmail.skipFirst()
    val isValidDescription: MutableLiveData<Boolean> get() = _isValidDescription.skipFirst()
    val isValidForm: LiveData<Boolean> get() = _combined

    init {
        App.instance().appGraph.embed(this)
        this._isValidEmail.value = false
        this._isValidDescription.value = false
    }

    @Inject
    lateinit var repo: RepositoryPrefs

    private var _combined: LiveData<Boolean> =
        CombinedLiveData.combine(_isValidEmail, _isValidDescription)
        { left, right ->
            return@combine left.and(right)
        }

    fun updateEmail(data: String) {
        val isEmailValid = Validator.isEmailValid(data)
        if (isEmailValid) repo.setEmail(data)
        _isValidEmail.postValue(isEmailValid)
    }

    fun updateDescription(data: String) {
        val isDescriptionValid = Validator.isNotEmpty(data)
        if (isDescriptionValid) Timber.d(repo.getEmail())
        _isValidDescription.postValue(isDescriptionValid)
    }
}