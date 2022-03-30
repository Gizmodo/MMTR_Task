package com.example.fragmentvm.ui.utils

sealed class StateVote<out T : Any> {
    object Empty : StateVote<Nothing>()
    object Loading : StateVote<Nothing>()
    object Finished : StateVote<Nothing>()
    data class Success<out T : Any>(val data: T) : StateVote<T>()
    data class UnSuccess<out T : Any>(val data: T) : StateVote<T>()
    data class Error(val throwable: Throwable) : StateVote<Nothing>()
}