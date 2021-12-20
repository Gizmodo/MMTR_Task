package com.example.fragmentvm.di

import com.example.fragmentvm.ui.main.MainVM
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RetroModule::class])
interface AppGraph {
    fun embed(mainVM: MainVM)
}