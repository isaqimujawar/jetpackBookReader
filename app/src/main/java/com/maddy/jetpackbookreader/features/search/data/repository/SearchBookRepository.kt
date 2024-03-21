package com.maddy.jetpackbookreader.features.search.data.repository

import com.maddy.jetpackbookreader.common.model.Item

interface SearchBookRepository {
    suspend fun getBooks(query: String): List<Item>
    suspend fun getBookInfo(bookId: String): Item
}