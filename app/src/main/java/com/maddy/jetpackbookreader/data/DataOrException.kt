package com.maddy.jetpackbookreader.data

/**
 * Wrapper Class - for handling network Data and Metadata
 *      parameters:
 *          data - is the actual network Data
 *          loading, exception - are Metadata
 */
data class DataOrException<T>(
    val data: T? = null,
    val loading: Boolean? = null,
    val exception: Exception? = null
)
