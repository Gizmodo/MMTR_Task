package com.example.fragmentvm.utils

import timber.log.Timber

class LineNumberDebugTree  : Timber.DebugTree() {

    override fun createStackElementTag(element: StackTraceElement): String? {
        return "(${element.fileName}:${element.lineNumber})#${element.methodName}"
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, "MMTR_$tag", message, t)
    }
}