package com.maddy.jetpackbookreader.screens.update

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.maddy.jetpackbookreader.R
import com.maddy.jetpackbookreader.components.NoteRow
import com.maddy.jetpackbookreader.model.ReadingBook
import com.maddy.jetpackbookreader.ui.theme.JetpackBookReaderTheme
import com.maddy.jetpackbookreader.utils.formatDate
import com.maddy.jetpackbookreader.widgets.AverageRatingBar


@Composable
fun BookImageAndTitle(book: ReadingBook, onClick: () -> Unit = {}) {
    val photoUrl = book.photoUrl ?: stringResource(R.string.stock_image_unsplash_url)
    val averageRating = book.averageRating?.toDouble()?.toInt() ?: 0

    Surface(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 16.dp)
            .fillMaxWidth()
            .clickable { onClick.invoke() },
        shape = CircleShape,
        shadowElevation = 4.dp,
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = photoUrl),
                contentDescription = stringResource(R.string.book_image),
                modifier = Modifier
                    .padding(4.dp)
                    .width(120.dp)
                    .height(100.dp)
                    .clip(RoundedCornerShape(topStart = 120.dp, topEnd = 20.dp)),
            )
            Column {
                Text(
                    text = "${book.title}",
                    modifier = Modifier.padding(end = 4.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${book.authors}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${book.publishedDate}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                AverageRatingBar(rating = averageRating)
            }
        }
    }
}

@Composable
fun StartReadingCard(updateStartReadingState: MutableState<Boolean>, book: ReadingBook) {
    val startReadingEnabled = if (book.startedReading != null) remember { mutableStateOf(false) }
    else remember { mutableStateOf(true) }

    Surface(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .clip(RoundedCornerShape(topEnd = 33.dp, bottomStart = 33.dp))
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 4.dp,
        shadowElevation = 6.dp,
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    startReadingEnabled.value = !startReadingEnabled.value
                    updateStartReadingState.value = true
                },
                enabled = startReadingEnabled.value
            ) {
                Text(
                    text = "Start Reading",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                )
            }

            Column(
                modifier = Modifier
                    .clickable {},
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = "Started Reading on", style = MaterialTheme.typography.titleMedium)
                Surface(
                    modifier = Modifier
                        .clip(CircleShape),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                ) {
                    Text(
                        text = "${formatDate(book.startedReading) ?: "Not Yet"}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun FinishReadingCard(updateFinishReadingState: MutableState<Boolean>, book: ReadingBook) {
    val finishReadingEnabled = if (book.finishedReading != null) remember { mutableStateOf(false) }
    else remember { mutableStateOf(true) }

    Surface(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .clip(RoundedCornerShape(topEnd = 33.dp, bottomStart = 33.dp))
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 4.dp,
        shadowElevation = 6.dp,
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    finishReadingEnabled.value = !finishReadingEnabled.value
                    updateFinishReadingState.value = true
                },
                enabled = finishReadingEnabled.value
            ) {
                Text(
                    text = "Mark as Read",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                )
            }

            Column(
                modifier = Modifier
                    .clickable {},
                horizontalAlignment = Alignment.Start
            ) {

                Text(text = "Finished Reading on:", style = MaterialTheme.typography.titleMedium)
                Surface(
                    modifier = Modifier
                        .clip(CircleShape),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                ) {
                    Text(
                        text = "${formatDate(book.finishedReading) ?: "Not Yet"}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun EditNoteTextField(onNoteEdit: (String) -> Unit) {
    var noteState by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = noteState,
        onValueChange = { noteState = it },
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .fillMaxHeight(0.5f),
        //.verticalScroll(rememberScrollState()),
        enabled = true,
        label = { Text(text = stringResource(R.string.enter_your_thoughts)) },
        placeholder = { Text(text = stringResource(R.string.add_note)) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                onNoteEdit(noteState)
            }
        ),
        shape = RoundedCornerShape(size = 15.dp),
    )
}

@Composable
fun NoteList(notes: List<String>) {
    /*
    // We cannot add a LazyColumn in a Vertically Scrollable Column
    // it leads to infinity maximum height issue
    LazyColumn {
        items(items = notes) { note ->
            NoteRow(note = note)
        }
    }*/

    for (element in notes) {
        NoteRow(note = element)
    }
}

@Preview(name = "Phone", showSystemUi = true)
@Preview(name = "Tablet", device = Devices.TABLET, showSystemUi = true)
@Composable
fun UpdateScreenPreview() {
// You can use some sample data to preview your composable without the need to construct the ViewModel
    JetpackBookReaderTheme {
        // StartReadingCard()
    }
}