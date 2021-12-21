package com.example.fragmentvm.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fragmentvm.App
import com.example.fragmentvm.repository.CatRepository
import javax.inject.Inject

class MainViewModel : ViewModel() {
    init {
        App.instance().appGraph.embed(this)
    }
    @Inject
    lateinit var catRepository: CatRepository
    val cats = catRepository.getCats
}