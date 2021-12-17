package com.example.fragmentvm.ui.main

import androidx.lifecycle.ViewModel
import com.example.fragmentvm.di.AppGraph
import com.example.fragmentvm.di.DaggerAppGraph
import com.example.fragmentvm.repository.CatRepository

class MainVM : ViewModel() {
    private val appGraph: AppGraph by lazy { DaggerAppGraph.create() }
    private val catRepository: CatRepository by lazy { appGraph.repository() }
    val cats = catRepository.getCats
}