package com.maddy.jetpackbookreader.screens.update

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.maddy.jetpackbookreader.R
import com.maddy.jetpackbookreader.components.RoundedButton
import com.maddy.jetpackbookreader.components.ShowProgressIndicator
import com.maddy.jetpackbookreader.model.ReadingBook
import com.maddy.jetpackbookreader.navigation.ReaderScreens
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
            else ShowUpdateScreen(navController, newHomeViewModel, book)
        }
    }
}

@Composable
fun ShowUpdateScreen(
    navController: NavController,
    newHomeViewModel: NewHomeViewModel,
    book: ReadingBook = getBook()
) {
    val yourRating = book.yourRating?.toDouble()?.toInt() ?: 0
    val ratingState = rememberSaveable { mutableIntStateOf(yourRating) }
    val noteState = rememberSaveable { mutableStateOf("") }

    // variables to check if we need to update
    val updateRatingState = rememberSaveable { mutableStateOf(false) }
    val updateStartReadingState = rememberSaveable { mutableStateOf(false) }
    val updateFinishReadingState = rememberSaveable { mutableStateOf(false) }
    val updateNoteState = rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        UpdateAndDeleteButton(
            navController,
            newHomeViewModel,
            book,
            ratingState,
            updateRatingState,
            updateStartReadingState,
            updateFinishReadingState,
            noteState,
            updateNoteState,
        )
        BookImageAndTitle(book) { navController.navigate(ReaderScreens.BookDetailsScreen.name + "/${book.googleBookId}") }
        BookRatingBar(text = "Your Rating", rating = yourRating) { newRating ->
            if (newRating != ratingState.intValue){
                ratingState.intValue = newRating
                updateRatingState.value = true
            }
        }
        StartReadingCard(updateStartReadingState, book)
        FinishReadingCard(updateFinishReadingState, book)
        EditNoteTextField { note ->
            if (note != null && note.length > 0) {
                noteState.value = note
                updateNoteState.value = true
            }
        }
        NoteList(notes = book.notes ?: listOf("notes not added"))
    }
}

@Composable
fun UpdateAndDeleteButton(
    navController: NavController,
    newHomeViewModel: NewHomeViewModel,
    book: ReadingBook,
    ratingState: MutableState<Int>,
    updateRatingState: MutableState<Boolean>,
    updateStartReadingState: MutableState<Boolean>,
    updateFinishReadingState: MutableState<Boolean>,
    noteState: MutableState<String>,
    updateNoteState: MutableState<Boolean>
) {
    val context = LocalContext.current

    // if true - then will update the book with new changes
    val updateBook = updateRatingState.value
            || updateStartReadingState.value
            || updateFinishReadingState.value
            || updateNoteState.value

    Row(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RoundedButton(text = "Update") {
            if (updateBook) {
                newHomeViewModel.updateBookInDatabase(
                    book,
                    book.id,
                    ratingState.value,
                    noteState.value,
                    updateRatingState.value,
                    updateStartReadingState.value,
                    updateFinishReadingState.value,
                    updateNoteState.value,
                ) { updateSuccessful ->
                    if (updateSuccessful) {
                        Toast
                            .makeText(context, "Book Updated Successfully", Toast.LENGTH_SHORT)
                            .show()
                        navController.navigate(ReaderScreens.HomeScreen.name) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    } else {
                        Toast
                            .makeText(context, "Book Update Failed", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
        RoundedButton(text = "Delete")
    }
}