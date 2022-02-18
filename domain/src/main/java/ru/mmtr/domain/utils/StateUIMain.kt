package ru.mmtr.domain.utils

sealed class StateUIMain {
    object Empty : StateUIMain()
    object Loading : StateUIMain()
    object Finished : StateUIMain()
    class Error(val t: Throwable) : StateUIMain()
}
