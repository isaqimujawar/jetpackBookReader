package com.maddy.jetpackbookreader.screens.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.maddy.jetpackbookreader.data.DataOrException
import com.maddy.jetpackbookreader.model.ReadingBook
import com.maddy.jetpackbookreader.repository.FireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: FireRepository) : ViewModel() {
    val books = mutableStateOf(DataOrException<List<ReadingBook>>())
    private val currentUser = FirebaseAuth.getInstance().currentUser

    init {
        getAllBooks()
    }

    fun getAllBooks() {
        viewModelScope.launch(Dispatchers.Default) {
            books.value = repository.getAllBooks()
            Log.d("HomeViewModel", "getAllBooks: ${books.value.data}")
        }
    }

    fun getUserDisplayName() =
        currentUser?.email?.split('@')?.get(0).toString() ?: "username"

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }

    fun getReadingBookList(): List<ReadingBook> =
        if (!books.value.data.isNullOrEmpty()) {
            books.value.data!!.toList().filter { readingBook ->
                // here the predicate is that the books should belong to the currentUser.uid
                readingBook.userId == currentUser?.uid
            }
        } else emptyList<ReadingBook>()

    fun getBookById(bookId: String): ReadingBook {
        return getReadingBookList().firstOrNull { it.id == bookId } ?: ReadingBook()
    }
}