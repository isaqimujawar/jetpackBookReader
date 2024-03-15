package com.maddy.jetpackbookreader.repositoryOld

import com.maddy.jetpackbookreader.data.Response
import com.maddy.jetpackbookreader.network.BooksApi
import javax.inject.Inject

class RemoteBookRepositoryImpl @Inject constructor(private val api: BooksApi) : RemoteBookRepository {
    override suspend fun getBooks(query: String): Response<Any> =
        try {
            Response.Loading(data = true)
            val itemList = api.getBooks(query).items
            if (itemList.isNotEmpty()) Response.Loading(data = false)
            Response.Success(data = itemList)
        } catch (e: Exception) {
            Response.Error(data = e.message, message = "Exception")
        }

    override suspend fun getBookInfo(bookId: String): Response<Any> {
        TODO("Not yet implemented")
    }
}