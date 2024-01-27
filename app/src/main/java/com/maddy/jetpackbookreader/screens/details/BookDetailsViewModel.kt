package com.maddy.jetpackbookreader.screens.details

import androidx.lifecycle.ViewModel
import com.maddy.jetpackbookreader.model.Item
import com.maddy.jetpackbookreader.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookDetailsViewModel @Inject constructor(
    private val repository: BookRepository,
) : ViewModel() {
    suspend fun getBookInfo(bookId: String): Item = repository.getBookInfo(bookId)
}