package com.example.fragmentvm.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fragmentvm.App
import com.example.fragmentvm.utils.CombinedLiveData
import com.example.fragmentvm.utils.Util.Companion.skipFirst
import com.example.fragmentvm.utils.Validator
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
    lateinit var sharedPreferences: SharedPreferences

    private var _combined: LiveData<Boolean> =
        CombinedLiveData.combine(_isValidEmail, _isValidDescription)
        { left, right ->
            return@combine left.and(right)
        }

    fun updateEmail(data: String) {
        val isEmailValid = Validator.isEmailValid(data)
        if (isEmailValid) {
            // TODO: Save to prefs
            /*val preferences = Preferences()
            preferences.saveEmail(data)*/
        }
        _isValidEmail.postValue(isEmailValid)
    }

    fun updateDescription(data: String) {
        val isDescriptionValid = Validator.isNotEmpty(data)
        if (isDescriptionValid) {
            // TODO: Save to prefs
            val prefEditor: SharedPreferences.Editor =
                sharedPreferences.edit()
            prefEditor.putString("key",data)
            prefEditor.apply()

        }
        _isValidDescription.postValue(isDescriptionValid)
    }
}