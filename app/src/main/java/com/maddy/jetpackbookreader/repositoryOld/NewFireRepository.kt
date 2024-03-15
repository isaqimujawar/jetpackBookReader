package com.maddy.jetpackbookreader.repositoryOld

import android.util.Log
import com.google.firebase.firestore.Query
import com.maddy.jetpackbookreader.model.ReadingBook
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NewFireRepository @Inject constructor(private val firestoreQuery: Query) {
    private val _bookListStateFlow = MutableStateFlow<List<ReadingBook>>(emptyList())
    val bookListStateFlow: StateFlow<List<ReadingBook>> = _bookListStateFlow

    private val _errorStateFlow = MutableStateFlow<String?>(null)
    val errorStateFlow: StateFlow<String?> = _errorStateFlow

    private val _loadingStateFlow = MutableStateFlow<Boolean>(true)
    val loadingStateFlow: StateFlow<Boolean> = _loadingStateFlow

    val coroutineScope = CoroutineScope(Dispatchers.IO)

    suspend fun getAllBooks() {
        // Launch a new coroutine within the repository's coroutine scope
        // coroutineScope.launch { }
        _loadingStateFlow.value = true
        coroutineScope.launch {
            try {
                _bookListStateFlow.value =
                    firestoreQuery.get().await().documents.map { documentSnapshot ->
                        documentSnapshot.toObject(ReadingBook::class.java)!!
                    }
                _errorStateFlow.value = null // Clear error state if successful
                _loadingStateFlow.value = false
            } catch (e: Exception) {
                // Handle error
                Log.d("NewFireRepository", "getAllBooks: ${e.localizedMessage}")
                _errorStateFlow.value = e.message ?: "Unknown error occurred"
                _loadingStateFlow.value = false
                // Emit an empty list or maintain the current list of users if needed
                // _userListStateFlow.value = emptyList()
            }
        }
    }

    // Function to cancel ongoing API requests when the repository is no longer needed
    fun cancel() {
        coroutineScope.cancel()
    }
}