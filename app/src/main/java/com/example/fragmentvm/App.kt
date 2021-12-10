package com.example.fragmentvm

import android.app.Application
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(LineNumberDebugTree())
        }
    }

    class LineNumberDebugTree  : Timber.DebugTree() {

        override fun createStackElementTag(element: StackTraceElement): String? {
            return "(${element.fileName}:${element.lineNumber}) #${element.methodName}"
        }

        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            super.log(priority, "MMTR_$tag", message, t)
        }
    }
}