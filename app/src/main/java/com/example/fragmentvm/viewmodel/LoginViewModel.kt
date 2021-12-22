package com.example.fragmentvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fragmentvm.model.User
import timber.log.Timber


class LoginViewModel : ViewModel() {
    private var _description = MutableLiveData<String>()
    val description: MutableLiveData<String>
        get() = _description

    private var _email = MutableLiveData<String>()
    val email: MutableLiveData<String>
        get() = _email


    private var user: User? = null

    private val userMutableLiveData = MutableLiveData<User>()
    val userData: LiveData<User> = userMutableLiveData

    init {
        description.value = ""
        email.value = ""
        this.user = User("", "")
    }

    fun performValidation() {

        /* if (username.isBlank()) {
             logInResult.value = "Invalid username"
             return
         }

         if (password.isBlank()) {
             logInResult.value = "Invalid password"
             return
         }

         logInResult.value = "Valid credentials :)"*/
    }

    fun getUser(): MutableLiveData<User> {
        return userMutableLiveData
    }

    fun updateDescription(data: String) {
        Timber.d("Description received in VM $data")
        _description.value = data
    }

    fun updateEmail(data: String) {
        Timber.d("Email received in VM $data")
        _email.value = data
    }
}