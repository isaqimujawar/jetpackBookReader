package com.maddy.jetpackbookreader.repository

import android.util.Log
import com.maddy.jetpackbookreader.data.DataOrException
import com.maddy.jetpackbookreader.model.Item
import com.maddy.jetpackbookreader.network.BooksApi
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(private val api: BooksApi) : BookRepository {
    private val booksDataOrException = DataOrException<List<Item>>()
    private val bookInfoDataOrException = DataOrException<Item>()

    override suspend fun getBooks(query: String): DataOrException<List<Item>> {
        try {
            booksDataOrException.loading = true
            booksDataOrException.data = api.getBooks(query).items
            if (booksDataOrException.data.isNullOrEmpty()) {
                // data is null.
            } else { booksDataOrException.loading = false }
        } catch (e: Exception) {
            booksDataOrException.exception = e
        }
        Log.d(
            "BookRepositoryImpl",
            """getBooks(): 
                |dataOrException.data = ${booksDataOrException.data} 
                |dataOrException.loading = ${booksDataOrException.loading} 
                |dataOrException.exception = ${booksDataOrException.exception}""".trimMargin()
        )
        return booksDataOrException
    }

    override suspend fun getBookInfo(bookId: String): DataOrException<Item> {
        try {
            bookInfoDataOrException.loading = true
            bookInfoDataOrException.data = api.getBookInfo(bookId)
            if (bookInfoDataOrException.data != null) bookInfoDataOrException.loading = false
        } catch (e: Exception) {
            bookInfoDataOrException.exception = e
        }
        Log.d(
            "BookRepositoryImpl",
            """getBookInfo(): 
                |dataOrException.data = ${bookInfoDataOrException.data} 
                |dataOrException.loading = ${bookInfoDataOrException.loading} 
                |dataOrException.exception = ${bookInfoDataOrException.exception}""".trimMargin()
        )
        return bookInfoDataOrException
    }
}