package com.maddy.jetpackbookreader.features.updatebook.presentation

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.maddy.jetpackbookreader.R
import com.maddy.jetpackbookreader.common.model.ReadingBook
import com.maddy.jetpackbookreader.common.presentation.components.RoundedButton
import com.maddy.jetpackbookreader.common.presentation.components.ShowProgressIndicator
import com.maddy.jetpackbookreader.common.presentation.widgets.BookRatingBar
import com.maddy.jetpackbookreader.common.presentation.widgets.ReaderTopAppBar
import com.maddy.jetpackbookreader.common.utils.getBook
import com.maddy.jetpackbookreader.features.home.presentation.NewHomeViewModel
import com.maddy.jetpackbookreader.features.updatebook.presentation.components.BookImageAndTitle
import com.maddy.jetpackbookreader.features.updatebook.presentation.components.EditNoteTextField
import com.maddy.jetpackbookreader.features.updatebook.presentation.components.FinishReadingCard
import com.maddy.jetpackbookreader.features.updatebook.presentation.components.NoteList
import com.maddy.jetpackbookreader.features.updatebook.presentation.components.StartReadingCard
import com.maddy.jetpackbookreader.navigation.ReaderScreens

@Composable
fun UpdateScreen(
    navController: NavController,
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
    val context = LocalContext.current

    val yourRating = book.yourRating?.toDouble()?.toInt() ?: 0
    val ratingState = rememberSaveable { mutableIntStateOf(yourRating) }
    val noteState = rememberSaveable { mutableStateOf("") }

    // variables to check if we need to update
    val updateRatingState = rememberSaveable { mutableStateOf(false) }
    val updateStartReadingState = rememberSaveable { mutableStateOf(false) }
    val updateFinishReadingState = rememberSaveable { mutableStateOf(false) }
    val updateNoteState = rememberSaveable { mutableStateOf(false) }

    // Get Map of updates from ViewModel
    // Every time "updates" variable is called it will call "constructUpdates" in Viewmodel
    val updates = newHomeViewModel.constructUpdates(
        book,
        ratingState.value,
        updateRatingState.value,
        updateStartReadingState.value,
        updateFinishReadingState.value,
        noteState.value,
        updateNoteState.value
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        UpdateAndDeleteButton(
            onUpdateClick = {
                updateBook(navController, newHomeViewModel, context, book, updates)
            },
            onDeleteClick = {
                deleteBook(navController, newHomeViewModel, context, book)
            }
        )
        BookImageAndTitle(book) { navController.navigate(ReaderScreens.BookDetailsScreen.name + "/${book.googleBookId}") }
        BookRatingBar(text = "Your Rating", rating = yourRating) { newRating ->
            if (newRating != ratingState.intValue) {
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
fun UpdateAndDeleteButton(onUpdateClick: () -> Unit, onDeleteClick:() -> Unit) {
    Row(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Update Button
        RoundedButton(text = "Update") {
            onUpdateClick()
        }

        // Delete Button
        val openAlertDialog = rememberSaveable { mutableStateOf(false) }
        when {
            openAlertDialog.value -> {
                ShowAlertDialog(
                    dialogTitle = stringResource(id = R.string.are_you_sure),
                    dialogText = stringResource(id = R.string.action_not_reversible),
                    icon = Icons.Rounded.Info,
                    onDismissRequest = { openAlertDialog.value = false },
                    onConfirmation = {
                        openAlertDialog.value = false
                        onDeleteClick()         // Add logic here to handle confirmation.
                    }
                )
            }
        }
        RoundedButton(text = "Delete") { openAlertDialog.value = true }
    }
}

@Composable
fun ShowAlertDialog(
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    AlertDialog(
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = stringResource(R.string.alert_dialog_icon)
            )
        },
        title = { Text(text = dialogTitle) },
        text = { Text(text = dialogText) },
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(
                onClick = { onConfirmation() }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismissRequest() }
            ) {
                Text("Dismiss")
            }
        }
    )
}

fun updateBook(
    navController: NavController,
    newHomeViewModel: NewHomeViewModel,
    context: Context,
    book: ReadingBook,
    updates: Map<String, Any?>,
) {
    newHomeViewModel.updateBookInFirestore(book.id, updates) { updateSuccessful ->
        // Handle update result
        if (updateSuccessful) {
            // Update successful
            Toast.makeText(context, "Book Updated Successfully", Toast.LENGTH_SHORT).show()
            navController.navigate(ReaderScreens.HomeScreen.name) {
                popUpTo(navController.graph.id) { inclusive = true }
            }
        } else {
            // Update failed
            Toast.makeText(context, "Book Update Failed", Toast.LENGTH_SHORT).show()
        }
    }
}

fun deleteBook(
    navController: NavController,
    newHomeViewModel: NewHomeViewModel,
    context: Context,
    book: ReadingBook
) {
    newHomeViewModel.deleteBook(book) { deleteSuccessful ->
        if (deleteSuccessful) {
            // Delete successful
            Toast.makeText(context, "Book Deleted Successfully", Toast.LENGTH_SHORT).show()
            navController.navigate(ReaderScreens.HomeScreen.name) {
                popUpTo(navController.graph.id) { inclusive = true }
            }
        } else {
            // Delete failed
            Toast.makeText(context, "Book Delete Failed", Toast.LENGTH_SHORT).show()
        }
    }
}
