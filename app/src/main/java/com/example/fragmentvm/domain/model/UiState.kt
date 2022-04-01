package com.example.fragmentvm.domain.model

import com.example.fragmentvm.temp.CatResponse

data class UiState(
    val searchResult: CatResponse,
    val query: String = "",
    val lastQueryScrolled: String = "",
    val hasNotScrolledForCurrentSearch: Boolean = false,
)