package com.example.fragmentvm.utils

sealed class StateUIMain {
    object Empty : StateUIMain()
    object Loading : StateUIMain()
    object Finished : StateUIMain()
    class Error(val t: Throwable) : StateUIMain()
}
