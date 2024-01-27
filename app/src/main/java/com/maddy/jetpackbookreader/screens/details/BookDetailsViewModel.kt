package com.maddy.jetpackbookreader.screens.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maddy.jetpackbookreader.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailsViewModel @Inject constructor(
    private val repository: BookRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

      val title = savedStateHandle.getStateFlow(key = "title", initialValue = "")
      val authors = savedStateHandle.getStateFlow(key = "authors", initialValue = "")
      val categories = savedStateHandle.getStateFlow(key = "categories", initialValue = "")
      val pageCount = savedStateHandle.getStateFlow(key = "pageCount", initialValue = "")
      val thumbnail = savedStateHandle.getStateFlow(key = "thumbnail", initialValue = "")

    init {
        getBookInfo("")
    }

    fun getBookInfo(bookId: String) {
        viewModelScope.launch {
            try {
                val item = repository.getBookInfo(bookId).volumeInfo
                savedStateHandle["title"] = item?.title.toString()
                savedStateHandle["authors"] = item?.authors.toString()
                savedStateHandle["categories"] = item?.categories.toString()
                savedStateHandle["thumbnail"] = item?.imageLinks?.thumbnail.toString()
                savedStateHandle["pageCount"] = item?.pageCount.toString()
            } catch (e: Exception) {
                Log.d("BookDetailsViewModel", "getBookInfo:  exception = ${e.localizedMessage}")
            }
        }
    }
}