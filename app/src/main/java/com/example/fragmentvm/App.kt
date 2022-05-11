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
        return makeClickableLineNumber(element)
    }

    private fun makeClickableLineNumber(
        element: StackTraceElement,
    ): String {
        val className = element.fileName
        val methodName = element.methodName
        val lineNumber = element.lineNumber
        val fileName = element.fileName
        val stringBuilder = StringBuilder(className)
            .append(".")
            .append(methodName)
            .append(" (")
            .append(fileName)
            .append(":")
            .append(lineNumber)
            .append(")  ")
        return stringBuilder.toString()
    }
    /*override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, tag, "111 $str $message", t)
    }*/
}