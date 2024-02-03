package com.maddy.jetpackbookreader.data

sealed class Response<T>(val data: T? = null, val message: String? = null) {
    class Loading<T>(data: T?): Response<T>(data)
    class Success<T>(data: T?): Response<T>(data)
    class Error<T>(data: T?, message: String?): Response<T>(data, message)
}