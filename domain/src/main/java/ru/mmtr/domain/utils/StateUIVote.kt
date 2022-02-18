package ru.mmtr.domain.utils

sealed class StateUIVote<out T> {
    object Empty : StateUIVote<Nothing>()
    object Loading : StateUIVote<Nothing>()
    object Finished : StateUIVote<Nothing>()
    data class Success<T>(val item: T) : StateUIVote<T>()
    data class Error(val throwable: Throwable) : StateUIVote<Nothing>()
    data class BadResponse<T>(val badResponse: T) : StateUIVote<T>()
}
