package com.maddy.jetpackbookreader.common.model

data class BooksVolume(
    val items: List<Item>,
    val kind: String,
    val totalItems: Int
)