package com.maddy.jetpackbookreader.repository

import com.google.firebase.firestore.Query
import com.maddy.jetpackbookreader.data.DataOrException
import com.maddy.jetpackbookreader.model.ReadingBook
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireRepository @Inject constructor(private val firestoreQuery: Query) {
    suspend fun getAllBooksFromFirebase(): DataOrException<List<ReadingBook>> {
        val dataOrException = DataOrException<List<ReadingBook>>()

        try {
            dataOrException.loading = true
            dataOrException.data = firestoreQuery.get().await().documents.map { documentSnapshot ->
                documentSnapshot.toObject(ReadingBook::class.java)!!
            }
            if (!dataOrException.data.isNullOrEmpty()) dataOrException.loading = false
        } catch (e: Exception) {
            dataOrException.exception = e
        }

        return dataOrException
    }
}