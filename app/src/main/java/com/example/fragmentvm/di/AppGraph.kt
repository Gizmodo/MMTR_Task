package com.example.fragmentvm.di

import com.example.fragmentvm.ui.MainActivity
import com.example.fragmentvm.viewmodel.ApiViewModel
import com.example.fragmentvm.viewmodel.LoginViewModel
import com.example.fragmentvm.viewmodel.MainActivityViewModel
import com.example.fragmentvm.viewmodel.MainViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        RetrofitModule::class,
        AppModule::class,
        DataSourceModule::class
    ]
)
interface AppGraph {
    fun embed(vm: MainViewModel)
    fun embed(vm: ApiViewModel)
    fun embed(vm: LoginViewModel)
    fun embed(vm: MainActivityViewModel)
    fun embed(activity: MainActivity)
}