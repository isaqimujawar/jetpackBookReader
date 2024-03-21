package com.maddy.jetpackbookreader.features.search.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maddy.jetpackbookreader.common.model.Item
import com.maddy.jetpackbookreader.features.search.data.repository.SearchBookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *     Keep uiState ready for the UI
 * - UI is only responsible for displaying the uiState.
 * - ViewModel acts as the StateHolder that stores the uiState,
 * - and is responsible for making sure that it keeps that uiState ready
 * - when the UI asks for the uiState.
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchBookRepository
) : ViewModel() {
    // var list: List<Item> by mutableStateOf(listOf())
    var bookList:List<Item> by mutableStateOf(listOf())
    var isLoading by mutableStateOf(true)

    init {
        loadBooks()
    }

    private fun loadBooks() {
        getBooks("Habits")
        // searchBooks(query = "Habits")
    }

    fun getBooks(query: String) {
        isLoading = true
        viewModelScope.launch {
            if (query.isEmpty()) {
                isLoading = false
                return@launch
            }
            try {
                bookList = repository.getBooks(query)
                if (bookList.isNotEmpty()) {
                    isLoading = false
                } else {
                    Log.d("SearchViewModel", "getBooks(): failed to retrieve books")
                }
            } catch (e: Exception) {
                isLoading = false
                Log.d("SearchViewModel", "getBooks(): ${e.localizedMessage}")
            }
        }
    }
}