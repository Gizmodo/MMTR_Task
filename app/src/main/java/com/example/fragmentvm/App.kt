package com.example.fragmentvm

import android.app.Application
import android.content.Context
import com.example.fragmentvm.di.*
import com.example.fragmentvm.utils.LineNumberDebugTree
import timber.log.Timber

class App : Application() {
    lateinit var appGraph: AppGraph

    companion object {
        private lateinit var appInstance: App
        lateinit var appContext: Context
        fun instance(): App {
            return appInstance
        }
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(LineNumberDebugTree())
        appGraph = DaggerAppGraph
            .builder()
            .retroModule(RetroModule())
            .contextModule(ContextModule())
            .storageModule(StorageModule(this))
//            .sharedPreferencesModule(PreferencesModule())
//            .prefsModule(PrefsModule())
            .build()
        appInstance = this
        appContext = this
    }


}