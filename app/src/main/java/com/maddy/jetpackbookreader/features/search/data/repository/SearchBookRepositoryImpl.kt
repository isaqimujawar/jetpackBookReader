package com.maddy.jetpackbookreader.features.search.data.repository

import com.maddy.jetpackbookreader.common.model.Item
import com.maddy.jetpackbookreader.network.BooksApi
import javax.inject.Inject

class SearchBookRepositoryImpl @Inject constructor(private val api: BooksApi) : SearchBookRepository {

    override suspend fun getBooks(query: String): List<Item> = api.getBooks(query).items

    override suspend fun getBookInfo(bookId: String): Item = api.getBookInfo(bookId)
}