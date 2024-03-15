package com.maddy.jetpackbookreader.repository


import com.google.firebase.firestore.Query
import com.maddy.jetpackbookreader.model.ReadingBook
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FlowRepository @Inject constructor(private val firestoreQuery: Query) {
    val bookList: Flow<List<ReadingBook?>> = flow {
        val bookList: List<ReadingBook?> = firestoreQuery.get().await().documents.map { documentSnapshot ->
            documentSnapshot.toObject(ReadingBook::class.java)
        }
        emit(bookList)
    }

    // fun getBookInfo() = flow<Int> {  }
}