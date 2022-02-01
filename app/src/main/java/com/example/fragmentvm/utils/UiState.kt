package com.example.fragmentvm.utils

sealed class UiState<out T> {
    object Empty : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    object Finished : UiState<Nothing>()
    data class Success<T>(val item: T) : UiState<T>()
    data class Error(val throwable: Throwable) : UiState<Nothing>()
    data class BadResponse<T>(val badResponse: T) : UiState<T>()
}
