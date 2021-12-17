package com.example.fragmentvm.di

import com.example.fragmentvm.repository.CatRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RetroModule::class])
interface AppGraph {
    fun repository(): CatRepository
}