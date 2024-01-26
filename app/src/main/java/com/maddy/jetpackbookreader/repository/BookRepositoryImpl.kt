package com.maddy.jetpackbookreader.repository

import android.util.Log
import com.maddy.jetpackbookreader.data.DataOrException
import com.maddy.jetpackbookreader.model.BooksVolume
import com.maddy.jetpackbookreader.model.Item
import com.maddy.jetpackbookreader.network.BooksApi
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(private val api: BooksApi) : BookRepository {
    val booksDataOrException:DataOrException<BooksVolume> = DataOrException()
    val bookInfoDataOrException:DataOrException<Item> = DataOrException()

    override suspend fun getBooks(query: String): DataOrException<BooksVolume> {
        try {
            booksDataOrException.loading = true
            booksDataOrException.data = api.getBooks(query)
            if (booksDataOrException.data != null) { booksDataOrException.loading = false }
            Log.d(
                "BookRepositoryImpl",
                """getBooks(): 
                    |dataOrException.data = ${booksDataOrException.data} 
                    |dataOrException.loading = ${booksDataOrException.loading} 
                    |dataOrException.exception = ${booksDataOrException.exception}""".trimMargin()
            )
        } catch (e: Exception) {
            booksDataOrException.exception = e
        }
        return booksDataOrException
    }

    override suspend fun getBookInfo(bookId: String): DataOrException<Item> {
        try {
            bookInfoDataOrException.loading = true
            bookInfoDataOrException.data = api.getBookInfo(bookId)
            if (bookInfoDataOrException.data != null) { bookInfoDataOrException.loading = false }
            Log.d(
                "BookRepositoryImpl",
                """getBookInfo(): 
                |dataOrException.data = ${bookInfoDataOrException.data} 
                |dataOrException.loading = ${bookInfoDataOrException.loading} 
                |dataOrException.exception = ${bookInfoDataOrException.exception}""".trimMargin()
            )
        } catch (e: Exception) {
            bookInfoDataOrException.exception = e
        }
        return bookInfoDataOrException
    }
}