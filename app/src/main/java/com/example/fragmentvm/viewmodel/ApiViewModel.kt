package com.example.fragmentvm.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fragmentvm.App
import com.example.fragmentvm.model.Payload
import com.example.fragmentvm.repository.Repository
import javax.inject.Inject

class ApiViewModel : ViewModel() {
    init {
        App.instance().appGraph.embed(this)
    }

    @Inject
    lateinit var repository: Repository
    private val payload: Payload = Payload("", "")
    val userResponse = repository.postForm(payload)
}