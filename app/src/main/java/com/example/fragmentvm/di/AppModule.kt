package com.example.fragmentvm.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun providesApplication(): Application = application

    @Provides
    @Singleton
    fun providesApplicationContext(): Context = application
/*
    @Singleton
    @Provides
    fun providesNetworkConnectivityHelper(): NetworkConnectivityHelper{
        return NetworkConnectivityHelper(application.applicationContext)
    }*/
}