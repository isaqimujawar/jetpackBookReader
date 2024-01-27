package com.maddy.jetpackbookreader.screens.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maddy.jetpackbookreader.model.Item
import com.maddy.jetpackbookreader.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailsViewModel @Inject constructor(
    private val repository: BookRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // ViewModel SavedStateHandle - survives configuration changes and process death
    val bookId = savedStateHandle.getStateFlow(key = "bookId", initialValue = "")

    private val _bookItem = MutableStateFlow<Item>(value = Item())
    val bookItem = _bookItem.asStateFlow()

    fun getBookInfo(bookId: String) {
        savedStateHandle["bookId"] = bookId
        viewModelScope.launch {
            try {
                _bookItem.update {
                    repository.getBookInfo(bookId)
                }
            } catch (e: Exception) {
                Log.d("BookDetailsViewModel", "getBookInfo:  exception = ${e.localizedMessage}")
            }
        }
    }
}