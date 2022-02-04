package com.example.fragmentvm.utils

import com.example.fragmentvm.model.Cat

sealed class StateUIMain {
    object Empty : StateUIMain()
    object Loading : StateUIMain()
    object Finished : StateUIMain()
    class Loaded(val data: List<Cat>) : StateUIMain()
    class Error(val t: Throwable) : StateUIMain()
}
