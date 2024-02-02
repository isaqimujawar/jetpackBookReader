package com.maddy.jetpackbookreader.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
    private val currentUser = FirebaseAuth.getInstance().currentUser

    init {
        // Fetch users when ViewModel is initialized
        getAllBooks()
    }

    fun getAllBooks() {
        viewModelScope.launch {
            repository.getAllBooks()
        }
    }

    fun getUserDisplayName() =
        currentUser?.email?.split('@')?.get(0).toString() ?: "username"

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }

    fun getReadingBookList(): List<ReadingBook> =
        if (!bookListStateFlow.value.isNullOrEmpty()) {
            bookListStateFlow.value!!.toList().filter { readingBook ->
                // here the predicate is that the books should belong to the currentUser.uid
                readingBook.userId == currentUser?.uid
            }
        } else emptyList<ReadingBook>()

    fun getBookById(bookId: String): ReadingBook {
        return if (errorStateFlow.value == null)
            bookListStateFlow.value.firstOrNull { it.id == bookId } ?: ReadingBook()
        else
            ReadingBook()
    }

    fun updateBook(
        bookId: String?,
        newRating: Int,
        isStartedTimestamp: Timestamp?,
        onUpdateComplete: (Boolean) -> Unit
    ) {
        val bookToUpdate = hashMapOf(
            "your_rating" to newRating.toString(),
            "started_reading_at" to isStartedTimestamp
        ).toMap()

        FirebaseFirestore.getInstance()
            .collection("books")
            .document(bookId!!)
            .update(bookToUpdate)
            .addOnCompleteListener { onUpdateComplete(true) }
            .addOnFailureListener { onUpdateComplete(false) }
    }
}