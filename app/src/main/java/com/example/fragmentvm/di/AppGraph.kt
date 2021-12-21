package com.example.fragmentvm.di

import com.example.fragmentvm.viewmodel.MainViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RetroModule::class])
interface AppGraph {
    fun embed(mainViewModel: MainViewModel)
}