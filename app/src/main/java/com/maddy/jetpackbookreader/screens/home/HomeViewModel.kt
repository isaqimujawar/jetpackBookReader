package com.maddy.jetpackbookreader.screens.home

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
    private val currentUser = FirebaseAuth.getInstance().currentUser
    val books = mutableStateOf(DataOrException<List<ReadingBook>>())

    init {
        getAllBooks()
    }

    fun getAllBooks() {
        viewModelScope.launch(Dispatchers.Default) {
            books.value = repository.getAllBooks()
        }
    }

    fun getUserDisplayName() =
        currentUser?.email?.split('@')?.get(0).toString() ?: "username"

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }

    fun getReadingBookList(): List<ReadingBook> = if (!books.value.data.isNullOrEmpty()) {
        books.value.data!!.toList().filter { readingBook ->
            // here the predicate is that the books should belong to the currentUser.uid
            readingBook.userId == currentUser?.uid
        }
    } else emptyList<ReadingBook>()
}
