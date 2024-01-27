package com.maddy.jetpackbookreader.screens.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maddy.jetpackbookreader.data.Response
import com.maddy.jetpackbookreader.model.Item
import com.maddy.jetpackbookreader.repository.BookRepository
import com.maddy.jetpackbookreader.repository.RemoteBookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    private val repository: BookRepository,
    private val remoteRepository: RemoteBookRepository
) : ViewModel() {
    var bookList:List<Item> by mutableStateOf(listOf())
    var isLoading by mutableStateOf(true)

    init {
        loadBooks()
    }

    private fun loadBooks() {
        getBooks("bodybuilding")
        // searchBooks(query = "bodybuilding")
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

    /**
     * This is another way to access the Remote Repository
     */
    var list: List<Item> by mutableStateOf(listOf())

    fun searchBooks(query: String) {
        isLoading = true
        Log.d("SearchViewModel", "searchBooks(): query = $query")
        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()) return@launch
            try {
                when (val response = remoteRepository.getBooks(query)) {
                    is Response.Success -> {
                        list = response.data as List<Item>
                        if (list.isNotEmpty()) isLoading = false
                    }

                    is Response.Error -> {
                        isLoading = false
                        Log.d(
                            "SearchViewModel",
                            "searchBooks(): failed to get books ${response.data}"
                        )
                    }

                    else -> {
                        isLoading = false
                        Log.d("SearchViewModel", "searchBooks(): else block ${response.data}")
                    }
                }
            } catch (e: Exception) {
                isLoading = false
                Log.d("SearchViewModel", "searchBooks(): ${e.localizedMessage}")
            }
        }
    }
}