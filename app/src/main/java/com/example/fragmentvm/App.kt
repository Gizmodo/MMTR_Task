package com.example.fragmentvm

import android.app.Application
import com.example.fragmentvm.core.di.AppGraph
import com.example.fragmentvm.core.di.AppModule
import com.example.fragmentvm.core.di.DaggerAppGraph
import com.example.fragmentvm.core.di.RetrofitModule
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

internal class LineNumberDebugTree : Timber.DebugTree() {

    override fun createStackElementTag(element: StackTraceElement): String {
        return "(${element.fileName}:${element.lineNumber}) #${element.methodName}"
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, "MMTR_$tag", message, t)
    }
}