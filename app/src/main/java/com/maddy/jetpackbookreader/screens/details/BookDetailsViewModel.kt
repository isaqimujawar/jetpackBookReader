package com.maddy.jetpackbookreader.screens.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.maddy.jetpackbookreader.model.Item
import com.maddy.jetpackbookreader.model.ReadingBook
import com.maddy.jetpackbookreader.repository.BookRepository
import com.maddy.jetpackbookreader.repository.NewFireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailsViewModel @Inject constructor(
    private val repository: BookRepository,
    private val newFireRepository: NewFireRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // ViewModel SavedStateHandle - survives configuration changes and process death
    val bookId = savedStateHandle.getStateFlow(key = "bookId", initialValue = "")

    private val _bookItem = MutableStateFlow(value = Item())
    val bookItem = _bookItem.asStateFlow()

    private val bookListStateFlow: StateFlow<List<ReadingBook>> = newFireRepository.bookListStateFlow
    val errorStateFlow: StateFlow<String?> = newFireRepository.errorStateFlow
    private val currentUser = FirebaseAuth.getInstance().currentUser

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

    fun getAllBooks() {
        viewModelScope.launch {
            newFireRepository.getAllBooks()
        }
    }

    private fun getReadingBookList(): List<ReadingBook> =
        if (!bookListStateFlow.value.isNullOrEmpty()) {
            bookListStateFlow.value!!.toList().filter { readingBook ->
                // here the predicate is that the books should belong to the currentUser.uid
                readingBook.userId == currentUser?.uid
            }
        } else emptyList<ReadingBook>()

    private fun checkIfBookAlreadySaved(bookItem: Item): Boolean {
        // return getReadingBookList().filter { it.googleBookId == bookItem.id }.isNotEmpty()
        return getReadingBookList().any { it.googleBookId == bookItem.id }
    }

    fun saveToFirebase(bookItem: Item, onSuccessCompleted: (Boolean) -> Unit) {
        if (checkIfBookAlreadySaved(bookItem)) {
            Log.d("BookDetailsViewModel", "saveToFirebase: book is already saved")
            onSuccessCompleted(false)
        } else {
            val book = createBook(bookItem)
            val db = FirebaseFirestore.getInstance()
            val dbCollection = db.collection("books")
            dbCollection.add(book).addOnSuccessListener { documentRef ->
                val documentId = documentRef.id
                dbCollection
                    .document(documentId)
                    .update(hashMapOf("id" to documentId) as Map<String, Any>)
                    .addOnCompleteListener { task -> if (task.isSuccessful) onSuccessCompleted(true) }
                    .addOnFailureListener { exception ->
                        Log.d(
                            "BookDetailsViewModel",
                            "saveToFirebase: Error updating document ${exception.localizedMessage}"
                        )
                    }
            }
        }
    }

    private fun createBook(bookItem: Item): ReadingBook {
        val volumeInfo = bookItem.volumeInfo
        val averageRating =
            if (volumeInfo?.averageRating == null) "0.0" else volumeInfo.averageRating.toString()
        val yourRating = "0.0"

        return ReadingBook(
            title = volumeInfo?.title.toString(),
            authors = volumeInfo?.authors.toString(),
            notes = listOf("note 1", "note 2", "note 3"),
            photoUrl = volumeInfo?.imageLinks?.thumbnail.toString(),
            categories = volumeInfo?.categories.toString(),
            publishedDate = volumeInfo?.publishedDate.toString(),
            yourRating = yourRating,
            averageRating = averageRating,
            description = volumeInfo?.description.toString(),
            pageCount = volumeInfo?.pageCount.toString(),
            userId = FirebaseAuth.getInstance().currentUser?.uid.toString(),
            googleBookId = bookItem.id
        )
    }
}