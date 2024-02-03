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
    val loadingStateFlow: StateFlow<Boolean> = repository.loadingStateFlow

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

    fun getReadingBookList(): List<ReadingBook> {
        return if (!bookListStateFlow.value.isNullOrEmpty()) {
            bookListStateFlow.value!!.toList().filter { readingBook ->
                // here the predicate is that the books should belong to the currentUser.uid
                readingBook.userId == currentUser?.uid
            }
        } else emptyList<ReadingBook>()
    }

    fun getReadingNowBookList(listOfBooks: List<ReadingBook>) =
        listOfBooks.filter { book ->
            book.startedReading != null && book.finishedReading == null
        }

    fun getAddedBookList(listOfBooks: List<ReadingBook>) =
        listOfBooks.filter { book ->
            book.startedReading == null && book.finishedReading == null
        }

    fun getFinishBookList(listOfBooks: List<ReadingBook>) =
        listOfBooks.filter { book ->
            book.startedReading != null && book.finishedReading != null
        }


    fun getBookById(bookId: String): ReadingBook {
        return if (errorStateFlow.value == null)
            bookListStateFlow.value.firstOrNull { it.id == bookId } ?: ReadingBook()
        else
            ReadingBook()
    }

    // Function to construct updates map based on user changes
    fun constructUpdates(
        book: ReadingBook,
        ratingState: Int,
        updateRatingState: Boolean,
        updateStartReadingState: Boolean,
        updateFinishReadingState: Boolean,
        noteState: String,
        updateNoteState: Boolean
    ): Map<String, Any?> {
        val updates = mutableMapOf<String, Any?>()
        if (updateRatingState) {
            updates["your_rating"] = ratingState.toString()
        }
        if (updateStartReadingState) {
            updates["started_reading_at"] = Timestamp.now()
        }
        if (updateFinishReadingState) {
            updates["finished_reading_at"] = Timestamp.now()
        }
        if (updateNoteState) {
            val newNote = noteState.trim()
            if (newNote.isNotEmpty()) {
                val mutableNotes = (book.notes ?: emptyList()).toMutableList()
                mutableNotes.add(0, newNote)
                updates["notes"] = mutableNotes
            }
        }
        return updates
    }

    fun updateBookInFirestore(
        bookId: String?,
        updates: Map<String, Any?>,
        onUpdateComplete: (Boolean) -> Unit
    ) {
        if (updates.isEmpty()) {
            // No updates required
            onUpdateComplete(false)
            return
        }

        FirebaseFirestore.getInstance()
            .collection("books")
            .document(bookId!!)
            .update(updates)
            .addOnCompleteListener { onUpdateComplete(true) }
            .addOnFailureListener { onUpdateComplete(false) }
    }

    fun deleteBook(book: ReadingBook, onDeleteComplete: (Boolean) -> Unit) {
        FirebaseFirestore.getInstance()
            .collection("books")
            .document(book.id!!)
            .delete()
            .addOnCompleteListener { onDeleteComplete(true) }
            .addOnFailureListener { onDeleteComplete(false) }
    }
}