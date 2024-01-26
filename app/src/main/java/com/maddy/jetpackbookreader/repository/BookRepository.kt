package com.maddy.jetpackbookreader.repository

import com.maddy.jetpackbookreader.data.DataOrException
import com.maddy.jetpackbookreader.model.Item

interface BookRepository {
    suspend fun getBooks(query: String): DataOrException<List<Item>>
    suspend fun getBookInfo(bookId: String): DataOrException<Item>
}