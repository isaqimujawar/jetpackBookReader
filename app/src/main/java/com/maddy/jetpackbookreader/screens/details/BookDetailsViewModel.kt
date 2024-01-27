package com.maddy.jetpackbookreader.screens.details

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maddy.jetpackbookreader.model.Item
import com.maddy.jetpackbookreader.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailsViewModel @Inject constructor(private val repository: BookRepository): ViewModel() {
    var bookItem: Item by mutableStateOf(Item())
    var isBookLoading by mutableStateOf(false)

    fun getBookInfo(bookId: String) {
        isBookLoading = true
        viewModelScope.launch {
            if (bookId.isEmpty()) {
                isBookLoading = false
                return@launch
            }
            try {
                bookItem = repository.getBookInfo(bookId)
                if (bookItem.volumeInfo?.title  != null) isBookLoading = false
                else Log.d("BookDetailsViewModel", "getBookInfo(): bookItem.id = $bookItem.id")
            } catch (e: Exception) {
                isBookLoading = false
                Log.d("BookDetailsViewModel", "getBookInfo(): ${e.localizedMessage}")
            }
        }
    }
}