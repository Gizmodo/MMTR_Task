package com.example.fragmentvm.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StorageModule(private val context: Context) {
    @Singleton
    @Provides
    fun provideSharedPrefs(): SharedPreferences {
        return context.getSharedPreferences("aw", Context.MODE_PRIVATE)
        //return PreferenceManager.getDefaultSharedPreferences(context)
    }
}