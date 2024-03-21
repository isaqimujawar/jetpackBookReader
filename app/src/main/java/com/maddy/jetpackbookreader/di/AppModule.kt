package com.maddy.jetpackbookreader.di

import com.google.firebase.firestore.FirebaseFirestore
import com.maddy.jetpackbookreader.common.utils.ReaderConstants
import com.maddy.jetpackbookreader.features.home.data.repository.NewFireRepository
import com.maddy.jetpackbookreader.features.search.data.repository.SearchBookRepository
import com.maddy.jetpackbookreader.features.search.data.repository.SearchBookRepositoryImpl
import com.maddy.jetpackbookreader.network.BooksApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideBookApi(): BooksApi {
        return Retrofit.Builder()
            .baseUrl(ReaderConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BooksApi::class.java)
    }

    @Singleton
    @Provides
    fun provideSearchBookRepository(api: BooksApi): SearchBookRepository = SearchBookRepositoryImpl(api)

    @Singleton
    @Provides
    fun provideNewFireRepository() =
        NewFireRepository(firestoreQuery = FirebaseFirestore.getInstance().collection("books"))
}