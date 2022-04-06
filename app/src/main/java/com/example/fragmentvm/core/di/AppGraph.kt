package com.example.fragmentvm.core.di

import com.example.fragmentvm.data.datasource.CatPagingSource
import com.example.fragmentvm.ui.activity.MainActivity
import com.example.fragmentvm.ui.viewmodels.ApiViewModel
import com.example.fragmentvm.ui.viewmodels.LoginViewModel
import com.example.fragmentvm.ui.viewmodels.MainActivityViewModel
import com.example.fragmentvm.ui.viewmodels.MainViewModel
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
    //    fun embed(vm: MainViewModel)
    fun embed(vm: ApiViewModel)
    fun embed(vm: LoginViewModel)
    fun embed(vm: MainActivityViewModel)
    fun embed(activity: MainActivity)
    fun embed(vm: MainViewModel)
    fun embed(vm: CatPagingSource)
}