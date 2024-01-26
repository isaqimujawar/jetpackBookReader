package com.maddy.jetpackbookreader.data

/**
 * Wrapper Class - for handling network Data and Metadata
 *      parameters:
 *          data - is the actual network Data
 *          loading, exception - are Metadata
 */
data class DataOrException<T>(
    var data: T? = null,
    var loading: Boolean? = null,
    var exception: Exception? = null
)
