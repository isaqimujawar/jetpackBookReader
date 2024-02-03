package com.maddy.jetpackbookreader.repository

import com.maddy.jetpackbookreader.model.Item
import com.maddy.jetpackbookreader.network.BooksApi
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(private val api: BooksApi) : BookRepository {

    override suspend fun getBooks(query: String): List<Item> = api.getBooks(query).items

    override suspend fun getBookInfo(bookId: String): Item = api.getBookInfo(bookId)
}