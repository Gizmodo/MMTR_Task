package com.example.fragmentvm.utils

import com.example.fragmentvm.model.Cat

sealed class CatUiState {
    object Empty : CatUiState()
    object Loading : CatUiState()
    object Finished : CatUiState()
    class Loaded(val data: List<Cat>) : CatUiState()
    class Error(val t: Throwable) : CatUiState()
}
