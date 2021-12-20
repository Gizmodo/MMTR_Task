package com.example.fragmentvm

import android.app.Application
import com.example.fragmentvm.di.AppGraph
import com.example.fragmentvm.di.DaggerAppGraph
import com.example.fragmentvm.di.RetroModule

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
        appGraph = DaggerAppGraph
            .builder()
            .retroModule(RetroModule())
            .build()
        appInstance = this
    }


}