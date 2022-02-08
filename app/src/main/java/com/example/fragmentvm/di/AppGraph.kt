package com.example.fragmentvm.di

import com.example.fragmentvm.MainActivity
import com.example.fragmentvm.base.cavm
import com.example.fragmentvm.viewmodel.ApiViewModel
import com.example.fragmentvm.viewmodel.LoginViewModel
import com.example.fragmentvm.viewmodel.MainViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        RetroModule::class,
        AppModule::class
    ]
) // Providers
interface AppGraph {
    fun embed(vm: MainViewModel)
    fun embed(vm: ApiViewModel)
    fun embed(vm: LoginViewModel) //consumer
    fun embed(activity: MainActivity)
    fun embed(vm: cavm)
}