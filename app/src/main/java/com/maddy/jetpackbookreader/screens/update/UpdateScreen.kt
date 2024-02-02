package com.maddy.jetpackbookreader.screens.update

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.Timestamp
import com.maddy.jetpackbookreader.R
import com.maddy.jetpackbookreader.components.NoteRow
import com.maddy.jetpackbookreader.components.RoundedButton
import com.maddy.jetpackbookreader.components.ShowProgressIndicator
import com.maddy.jetpackbookreader.model.ReadingBook
import com.maddy.jetpackbookreader.navigation.ReaderScreens
import com.maddy.jetpackbookreader.screens.home.HomeViewModel
import com.maddy.jetpackbookreader.screens.home.NewHomeViewModel
import com.maddy.jetpackbookreader.ui.theme.JetpackBookReaderTheme
import com.maddy.jetpackbookreader.utils.getBook
import com.maddy.jetpackbookreader.widgets.AverageRatingBar
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
            else ShowBookUpdate(navController, newHomeViewModel, book)
        }
    }
}

@Composable
fun ShowBookUpdate(
    navController: NavController,
    newHomeViewModel: NewHomeViewModel,
    book: ReadingBook = getBook()
) {
    val yourRating = book.yourRating?.toDouble()?.toInt() ?: 0

    // variables to check if we need to update
    val updateRatingState = remember { mutableIntStateOf(yourRating) }
    val updateStartReadingState = remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        UpdateAndDeleteButton(navController, newHomeViewModel, book, yourRating, updateRatingState, updateStartReadingState)
        BookImageAndTitle(book) {
            navController.navigate(ReaderScreens.BookDetailsScreen.name + "/${book.googleBookId}")
        }
        BookRatingBar(text = "Your Rating", rating = yourRating) { newRating ->
            updateRatingState.value = newRating
            Log.d("TAG", "ShowSimpleForm: ${updateRatingState.value}")
        }
        StartReadingCard(updateStartReadingState, book)
        FinishReadingCard()
        EditNoteTextField { note ->
            Log.d("UpdateScreen", "EditNotesTextField: $note ")
            // TODO("Save the note to the given book")
        }
        NoteList(notes = book.notes ?: listOf("dummy note 1", "dummy note 2", "dummy note 3"))
    }
}

@Composable
fun UpdateAndDeleteButton(
    navController: NavController,
    newHomeViewModel: NewHomeViewModel,
    book: ReadingBook,
    yourRating: Int,
    ratingState: MutableState<Int>,
    updateStartReadingState: MutableState<Boolean>
) {
    val context = LocalContext.current

    val changedRating = yourRating != ratingState.value
    val isStartedTimestamp = if (updateStartReadingState.value) Timestamp.now() else book.startedReading

    // if true - then will update the book with new changes
    val bookUpdate = changedRating || updateStartReadingState.value

    Row(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RoundedButton(text = "Update") {
            if (bookUpdate) {
                newHomeViewModel.updateBook(book.id, ratingState.value, isStartedTimestamp) { updated ->
                    if (updated) {
                        Toast.makeText(context, "Book Updated Succefully", Toast.LENGTH_SHORT).show()
                        navController.navigate(ReaderScreens.HomeScreen.name) {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                        }
                    } else {
                        Toast.makeText(context, "Book Update Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        RoundedButton(text = "Delete")
    }
}

@Composable
private fun BookImageAndTitle(book: ReadingBook, onClick: () -> Unit = {}) {
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
    var startReadingEnabled = if(book.startedReading != null ) remember { mutableStateOf(false) }
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
                        text = "${book.startedReading?.toDate() ?: "start date"}",
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
fun FinishReadingCard() {
    var finishReadingState by rememberSaveable { mutableStateOf("Mark as Read") }
    var finishReadingEnabled by rememberSaveable { mutableStateOf(true) }

    Surface(
        modifier = Modifier
            .padding(4.dp)
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
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    finishReadingState = "Mark as Read"
                    finishReadingEnabled = !finishReadingEnabled
                },
                enabled = finishReadingEnabled
            ) {
                Text(
                    text = finishReadingState,
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
                        text = "finish date: ",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
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
fun NoteList(notes: List<String>) {
    /*
    // We cannot add a LazyColumn in a Vertically Scrollable Column
    // it leads to infinity maximum height issue
    LazyColumn {
        items(items = notes) { note ->
            NoteRow(note = note)
        }
    }*/

    for(element in notes) {
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