package com.maddy.jetpackbookreader.common.utils

import android.icu.text.DateFormat
import com.google.firebase.Timestamp
import com.maddy.jetpackbookreader.common.model.ReadingBook

fun getBook(
    id: String = "1",
    title: String = "Atomic Habits",
    authors: String = "James Clear",
    notes: List<String> = listOf("Tiny Habits need to be established first before expanding.")
) = ReadingBook(id, title, authors, notes)

fun formatDate(timestamp: Timestamp?): String? {
    return if (timestamp != null) {
        val date = DateFormat.getDateInstance()
            .format(timestamp.toDate())
            .toString()
            .split(',')[0]

        date
    } else
        null
}