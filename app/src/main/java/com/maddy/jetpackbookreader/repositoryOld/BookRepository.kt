package com.maddy.jetpackbookreader.repositoryOld

import com.maddy.jetpackbookreader.model.Item

interface BookRepository {
    suspend fun getBooks(query: String): List<Item>
    suspend fun getBookInfo(bookId: String): Item
}