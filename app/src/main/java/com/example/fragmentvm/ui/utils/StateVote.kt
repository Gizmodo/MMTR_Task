package com.example.fragmentvm.ui.utils

sealed class StateVote<out T> {
    object Empty : StateVote<Nothing>()
    object Loading : StateVote<Nothing>()
    object Finished : StateVote<Nothing>()
    data class Success<T>(val item: T) : StateVote<T>()
    data class Error(val throwable: Throwable) : StateVote<Nothing>()
    data class BadResponse<T>(val badResponse: T) : StateVote<T>()
}
