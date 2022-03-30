package com.example.fragmentvm.ui.utils

sealed class StateMain {
    object Empty : StateMain()
    object Loading : StateMain()
    object Finished : StateMain()
    class Error(val t: Throwable) : StateMain()
}
