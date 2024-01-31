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

    private val _bookItem = MutableStateFlow(value = Item())
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

    fun saveToFirebase(bookItem: Item, onSuccessCompleted: () -> Unit) {
        val book = createBook(bookItem)
        val db = FirebaseFirestore.getInstance()
        val dbCollection = db.collection("books")
        dbCollection.add(book).addOnSuccessListener { documentRef ->
            val documentId = documentRef.id
            dbCollection
                .document(documentId)
                .update(hashMapOf("id" to documentId) as Map<String, Any>)
                .addOnCompleteListener { task -> if (task.isSuccessful) onSuccessCompleted() }
                .addOnFailureListener { exception ->
                    Log.d(
                        "BookDetailsViewModel",
                        "saveToFirebase: Error updating document ${exception.localizedMessage}"
                    )
                }
        }
    }

    private fun createBook(bookItem: Item): ReadingBook {
        val volumeInfo = bookItem.volumeInfo
        val averageRating = if (volumeInfo?.averageRating == null) "0.0" else volumeInfo.averageRating.toString()
        val yourRating = "0.0"

        return ReadingBook(
            title = volumeInfo?.title.toString(),
            authors = volumeInfo?.authors.toString(),
            notes = "",
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