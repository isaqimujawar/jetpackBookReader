package com.maddy.jetpackbookreader.screens.update

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.maddy.jetpackbookreader.R
import com.maddy.jetpackbookreader.components.RoundedButton
import com.maddy.jetpackbookreader.components.ShowProgressIndicator
import com.maddy.jetpackbookreader.model.ReadingBook
import com.maddy.jetpackbookreader.screens.home.HomeViewModel
import com.maddy.jetpackbookreader.screens.home.NewHomeViewModel
import com.maddy.jetpackbookreader.utils.getBook
import com.maddy.jetpackbookreader.widgets.BookRatingBar
import com.maddy.jetpackbookreader.widgets.ReaderTopAppBar

@Composable
fun UpdateScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
    newHomeViewModel: NewHomeViewModel = hiltViewModel(),
    bookId: String
) {
    // val book = viewModel.getBookById(bookId)
    val book = newHomeViewModel.getBookById(bookId)

    Scaffold(
        topBar = {
            ReaderTopAppBar(title = stringResource(R.string.update_book)) {
                navController.popBackStack()
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (book.title.isNullOrEmpty()) ShowProgressIndicator()
            else ShowBookUpdate(book)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ShowBookUpdate(book: ReadingBook = getBook()) {
    val averageRating = book.averageRating?.toDouble()?.toInt() ?: 0
    val yourRating = book.yourRating?.toDouble()?.toInt() ?: 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        UpdateAndDeleteButton()
        BookImageAndTitle(book)
        StartAndFinishReadingButton()
        BookRatingBar(text = "Average Rating", rating = averageRating)
        BookRatingBar(text = "Your Rating", rating = yourRating)
        EditNoteTextField(book.notes ?: "Book Notes") { note ->
            Log.d("UpdateScreen", "EditNotesTextField: $note ")
            // TODO("Save the note to the given book")
        }
    }
}

@Composable
fun UpdateAndDeleteButton() {
    Row(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RoundedButton(text = "Update")
        RoundedButton(text = "Delete")
    }
}

@Composable
private fun BookImageAndTitle(book: ReadingBook) {
    val photoUrl = book.photoUrl ?: stringResource(R.string.stock_image_unsplash_url)

    Surface(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 16.dp)
            .fillMaxWidth(),
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
            }
        }

    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditNoteTextField(note: String, onNoteEdit: (String) -> Unit) {
    var noteState by rememberSaveable { mutableStateOf(note) }
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
        placeholder = { Text(text = stringResource(R.string.your_notes)) },
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
fun StartAndFinishReadingButton() {
    var startReadingState by rememberSaveable { mutableStateOf("Start Reading") }
    var finishReadingState by rememberSaveable { mutableStateOf("Mark as Read") }

    var startReadingEnabled by rememberSaveable { mutableStateOf(true) }
    var finishReadingEnabled by rememberSaveable { mutableStateOf(true) }

    Row(
        modifier = Modifier
            .padding(1.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TextButton(
            onClick = {
                startReadingState = "Started Reading"
                startReadingEnabled = !startReadingEnabled
            },
            enabled = startReadingEnabled
        ) {
            Text(
                text = startReadingState,
                style = MaterialTheme.typography.titleMedium,
                color = if (startReadingEnabled) MaterialTheme.colorScheme.primary
                else Color.Red.copy(alpha = 0.4f),
                fontWeight = FontWeight.Bold,
                maxLines = 1,
            )
        }
        TextButton(
            onClick = {
                finishReadingState = "Finished Reading"
                finishReadingEnabled = !finishReadingEnabled
            },
            enabled = finishReadingEnabled
        ) {
            Text(
                text = finishReadingState,
                style = MaterialTheme.typography.titleMedium,
                color = if (finishReadingEnabled) MaterialTheme.colorScheme.primary
                else Color.Red.copy(alpha = 0.4f),
                fontWeight = FontWeight.Bold,
                maxLines = 1,
            )
        }
    }
}