package com.example.fragmentvm.di

import com.example.fragmentvm.screen.MainActivity
import com.example.fragmentvm.screen.MainActivityViewModel
import com.example.fragmentvm.screen.api.ApiViewModel
import com.example.fragmentvm.screen.login.LoginViewModel
import com.example.fragmentvm.screen.main.MainViewModel
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