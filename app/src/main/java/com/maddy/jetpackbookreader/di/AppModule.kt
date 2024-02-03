package com.maddy.jetpackbookreader.di

import com.google.firebase.firestore.FirebaseFirestore
import com.maddy.jetpackbookreader.network.BooksApi
import com.maddy.jetpackbookreader.repository.BookRepository
import com.maddy.jetpackbookreader.repository.BookRepositoryImpl
import com.maddy.jetpackbookreader.repository.FireRepository
import com.maddy.jetpackbookreader.repository.NewFireRepository
import com.maddy.jetpackbookreader.repository.RemoteBookRepository
import com.maddy.jetpackbookreader.repository.RemoteBookRepositoryImpl
import com.maddy.jetpackbookreader.utils.ReaderConstants
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
    fun provideRemoteBookRepository(api: BooksApi): RemoteBookRepository =
        RemoteBookRepositoryImpl(api)

    @Singleton
    @Provides
    fun provideBookRepository(api: BooksApi): BookRepository = BookRepositoryImpl(api)

    @Singleton
    @Provides
    fun provideFireRepository() =
        FireRepository(firestoreQuery = FirebaseFirestore.getInstance().collection("books"))

    @Singleton
    @Provides
    fun provideNewFireRepository() =
        NewFireRepository(firestoreQuery = FirebaseFirestore.getInstance().collection("books"))
}