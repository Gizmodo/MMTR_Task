package com.example.fragmentvm.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fragmentvm.App
import com.example.fragmentvm.repository.RepositoryRetrofit
import javax.inject.Inject

class MainViewModel : ViewModel() {
    init {
        App.instance().appGraph.embed(this)
    }

    @Inject
    lateinit var repositoryRetrofit: RepositoryRetrofit
    val cats = repositoryRetrofit.getCats
}