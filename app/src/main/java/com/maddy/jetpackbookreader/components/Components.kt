package com.maddy.jetpackbookreader.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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

@Composable
fun RoundedButton(text: String = "Rounded Button", onClick: () -> Unit = {}) {
    Surface(
        modifier = Modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(topStartPercent = 29, bottomEndPercent = 29))
            .clickable { onClick() },
        color = MaterialTheme.colorScheme.primary
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun ShowProgressIndicator() {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        LinearProgressIndicator()
        Text(text = "Loading book...")
    }
}

@Composable
fun NoteRow(
    modifier: Modifier = Modifier,
    note: String = "dummy note 1",
    onNoteClicked: (String) -> Unit = {},
) {
    Surface(
        modifier = modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(topEnd = 33.dp, bottomStart = 33.dp))
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 4.dp,
        shadowElevation = 6.dp,
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .clickable {
                    onNoteClicked(note)
                },
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = note, style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun ShowText(text: String) {
    Surface(
        modifier = Modifier
            .padding(23.dp)
            .fillMaxWidth()
            .height(40.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text)
        }
    }
}