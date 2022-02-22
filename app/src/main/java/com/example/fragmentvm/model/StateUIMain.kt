package com.example.fragmentvm.model

sealed class StateUIMain {
    object Empty : StateUIMain()
    object Loading : StateUIMain()
    object Finished : StateUIMain()
    class Error(val t: Throwable) : StateUIMain()
}
