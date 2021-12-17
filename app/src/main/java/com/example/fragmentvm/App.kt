package com.example.fragmentvm

import android.app.Application
import com.example.fragmentvm.di.ApplicationGraph
import com.example.fragmentvm.di.DaggerApplicationGraph
import com.example.fragmentvm.di.RetroModule
import timber.log.Timber

class App : Application() {
    private val component: ApplicationGraph by lazy {
        DaggerApplicationGraph.builder()
            .retroModule(RetroModule())
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        this.component.inject(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(LineNumberDebugTree())
        }
    }

    inner class LineNumberDebugTree : Timber.DebugTree() {

        override fun createStackElementTag(element: StackTraceElement): String? {
            return "(${element.fileName}:${element.lineNumber}) #${element.methodName}"
        }

        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            super.log(priority, "MMTR_$tag", message, t)
        }
    }
}