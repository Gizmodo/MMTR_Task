package com.example.fragmentvm

import android.app.Application
import com.example.fragmentvm.di.AppGraph
import com.example.fragmentvm.di.AppModule
import com.example.fragmentvm.di.DaggerAppGraph
import com.example.fragmentvm.di.RetrofitModule
import com.example.fragmentvm.utils.LineNumberDebugTree
import timber.log.Timber

class App : Application() {
    lateinit var appGraph: AppGraph

    companion object {
        private lateinit var appInstance: App
        fun instance(): App {
            return appInstance
        }
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(LineNumberDebugTree())
        appGraph = DaggerAppGraph
            .builder()
            .retrofitModule(RetrofitModule())
            .appModule(AppModule(this))
            .build()
        appInstance = this
    }
}