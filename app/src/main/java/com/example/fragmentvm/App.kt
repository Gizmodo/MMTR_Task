package com.example.fragmentvm

import android.app.Application
import com.example.fragmentvm.utils.LineNumberDebugTree
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(LineNumberDebugTree())
        }
    }
}