package com.example.fragmentvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fragmentvm.utils.CombinedLiveData
import com.example.fragmentvm.utils.Validator
import timber.log.Timber

class LoginViewModel : ViewModel() {

    private var _isValidDescription = MutableLiveData<Boolean>()
    private var _isValidEmail = MutableLiveData<Boolean>()

    val isValidEmail: MutableLiveData<Boolean> get() = _isValidEmail.skipFirst()
    val isValidDescription: MutableLiveData<Boolean> get() = _isValidDescription.skipFirst()
    val isValidForm: LiveData<Boolean> get() = _combined

    init {
        this._isValidEmail.value = false
        this._isValidDescription.value = false
    }

    private fun <T> LiveData<T>.skipFirst(): MutableLiveData<T> {
        val result = MediatorLiveData<T>()
        var isFirst = true
        result.addSource(this) {
            if (isFirst) isFirst = false
            else result.value = it
        }
        return result
    }

    private var _combined: LiveData<Boolean> =
        CombinedLiveData.combine(_isValidEmail, _isValidDescription)
        { left, right ->
            return@combine left.and(right)
        }

    fun updateEmail(data: String) {
        _isValidEmail.postValue(Validator.isEmailValid(data))
    }

    fun updateDescription(data: String) {
        _isValidDescription.postValue(Validator.isDescriptionValid(data))
    }
}