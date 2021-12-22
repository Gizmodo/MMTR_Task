package com.example.fragmentvm.viewmodel

import android.text.Editable
import android.text.TextWatcher
import android.view.View
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

    val emailTextWatcher: TextWatcher
        get() = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                user?.email = s.toString()
            }
        }

    val descriptionTextWatcher: TextWatcher
        get() = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                user?.description = s.toString()
            }

        }

    fun onNextClicked(v: View) {

    }

    fun updateDescription(data: String) {
        _description.value = data
        Timber.d("Description $data")
    }

    fun updateEmail(data: String) {
        _email.value = data
        Timber.d("Email $data")
    }
}