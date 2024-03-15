package com.maddy.jetpackbookreader.data

import com.maddy.jetpackbookreader.model.ReadingBook

sealed class UiState {
    object Loading : UiState()
    data class Success(val data: List<ReadingBook>) : UiState()
    data class Error(val message: String): UiState()
}