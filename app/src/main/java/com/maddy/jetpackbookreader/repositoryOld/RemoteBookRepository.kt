package com.maddy.jetpackbookreader.repositoryOld

import com.maddy.jetpackbookreader.data.Response

interface RemoteBookRepository {
    suspend fun getBooks(query: String): Response<Any>
    suspend fun getBookInfo(bookId: String): Response<Any>
}