package com.maddy.jetpackbookreader.network

import com.maddy.jetpackbookreader.common.model.BooksVolume
import com.maddy.jetpackbookreader.common.model.Item
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface BooksApi {
    @GET("volumes")
    suspend fun getBooks(@Query("q") query: String): BooksVolume

    @GET("volumes/{bookId}")
    suspend fun getBookInfo(@Path("bookId") bookId: String): Item
}