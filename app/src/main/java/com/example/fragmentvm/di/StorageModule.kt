package com.example.fragmentvm.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class StorageModule(private val context: Context) {
    @Singleton
    @Provides
    fun provideSharedPrefences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }
}