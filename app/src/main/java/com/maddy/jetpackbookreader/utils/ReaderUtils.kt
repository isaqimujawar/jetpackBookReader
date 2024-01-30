package com.maddy.jetpackbookreader.utils

import com.maddy.jetpackbookreader.model.ReadingBook

fun getBook(
    id: String = "1",
    title: String = "Atomic Habits",
    authors: String = "James Clear",
    notes: String = "Tiny Habits need to be established first before expanding."
) = ReadingBook(id, title, authors, notes)


fun getDummyBookList(): List<ReadingBook> = listOf(getBook(id = "22"), getBook("25"))