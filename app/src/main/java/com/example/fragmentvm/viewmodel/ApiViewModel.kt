package com.example.fragmentvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fragmentvm.App
import com.example.fragmentvm.model.Payload
import com.example.fragmentvm.repository.Repository
import com.example.fragmentvm.utils.Util.Companion.skipFirst
import com.example.fragmentvm.utils.Validator
import javax.inject.Inject

class ApiViewModel : ViewModel() {
    @Inject
    lateinit var repository: Repository
    private val payload: Payload = Payload("", "")

    private val _apikey = MutableLiveData<String>()

    private val _isValidApiKey = MutableLiveData<Boolean>()
    val isValidApiKey: MutableLiveData<Boolean>
        get() = _isValidApiKey.skipFirst()

    val apikey: LiveData<String>
        get() = _apikey

    init {
        App.instance().appGraph.embed(this)

    }

    fun updateApiKey(data: String) {
        _isValidApiKey.postValue(Validator.isNotEmpty(data))
    }
fun asd(){
    val userResponse = repository.postForm(payload)
}

}