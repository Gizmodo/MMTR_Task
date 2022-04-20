package com.example.fragmentvm.core.di

import com.example.fragmentvm.data.datasource.CatPagingSource
import com.example.fragmentvm.data.datasource.FavCatPagingSource
import com.example.fragmentvm.ui.activity.MainActivity
import com.example.fragmentvm.ui.viewmodels.*
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
    fun embed(vm: CatViewModel)
    fun embed(vm: FavouriteViewModel)
    fun embed(vm: FavCatPagingSource)
}