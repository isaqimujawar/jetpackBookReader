package com.maddy.jetpackbookreader.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.core.text.HtmlCompat

@Composable
fun TitleText(title: String = "Title") {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun formatHttpText(httpText: String?): String {
    return HtmlCompat.fromHtml(
        httpText ?: "No Text found",
        HtmlCompat.FROM_HTML_MODE_LEGACY
    ).toString()
}