package com.maddy.jetpackbookreader.repository

import com.maddy.jetpackbookreader.data.DataOrException
import com.maddy.jetpackbookreader.model.BooksVolume
import com.maddy.jetpackbookreader.model.Item

interface BookRepository {
    suspend fun getBooks(query: String): DataOrException<BooksVolume>
    suspend fun getBookInfo(bookId: String): DataOrException<Item>
}