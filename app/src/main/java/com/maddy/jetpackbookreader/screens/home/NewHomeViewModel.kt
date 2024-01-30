package com.maddy.jetpackbookreader.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maddy.jetpackbookreader.model.ReadingBook
import com.maddy.jetpackbookreader.repository.NewFireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Refactored Update Screen Architecture
 * - ViewModel only acts as a communicator between Repository and UI.
 * - All the error handling is now centralised to Repository.
 */

@HiltViewModel
class NewHomeViewModel @Inject constructor(private val repository: NewFireRepository) :
    ViewModel() {

    val bookListStateFlow: StateFlow<List<ReadingBook>> = repository.bookListStateFlow
    val errorStateFlow: StateFlow<String?> = repository.errorStateFlow

    init {
        // Fetch users when ViewModel is initialized
        getAllBooks()
    }

    private fun getAllBooks() {
        viewModelScope.launch {
            repository.getAllBooks()
        }
    }

    fun getBookById(bookId: String): ReadingBook {
        return   if (errorStateFlow.value == null)
         bookListStateFlow.value.firstOrNull { it.id == bookId } ?: ReadingBook()
        else
            ReadingBook()
    }
}