package com.example.fragmentvm.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fragmentvm.model.Cat
import com.example.fragmentvm.model.signupModel

class SharedViewModel : ViewModel() {
    val selected = MutableLiveData<Cat>()
    private val _signupForm = MutableLiveData<signupModel>()
    val signupForm: LiveData<signupModel>
        get() = _signupForm

    fun select(cat: Cat) {
        selected.value = cat
    }

    fun setSignupForm(form: signupModel) {
        _signupForm.value = form
    }

}