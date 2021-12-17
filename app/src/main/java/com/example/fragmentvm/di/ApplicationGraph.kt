package com.example.fragmentvm.di

import android.app.Application
import com.example.fragmentvm.repository.CatRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RetroModule::class])
interface ApplicationGraph {
    fun inject(application: Application)
    fun repository(): CatRepository
}