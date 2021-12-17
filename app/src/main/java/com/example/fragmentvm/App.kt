package com.example.fragmentvm

import android.app.Application
import com.example.fragmentvm.utils.LineNumberDebugTree
import timber.log.Timber

@Suppress("unused")
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(LineNumberDebugTree())
    }
}