package com.example.fragmentvm.di

import com.example.fragmentvm.MainActivity
import com.example.fragmentvm.viewmodel.ApiViewModel
import com.example.fragmentvm.viewmodel.LoginViewModel
import com.example.fragmentvm.viewmodel.MainActivityVM
import com.example.fragmentvm.viewmodel.MainViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        RetroModule::class,
        AppModule::class,
        DataSourceModule::class
    ]
)
interface AppGraph {
    fun embed(vm: MainViewModel)
    fun embed(vm: ApiViewModel)
    fun embed(vm: LoginViewModel)
    fun embed(vm: MainActivityVM)
    fun embed(activity: MainActivity)
}