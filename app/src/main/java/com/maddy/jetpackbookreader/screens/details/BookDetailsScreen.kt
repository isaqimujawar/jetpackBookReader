package com.maddy.jetpackbookreader.screens.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.maddy.jetpackbookreader.R
import com.maddy.jetpackbookreader.widgets.ReaderTopAppBar

@Composable
fun BookDetailsScreen(
    navController: NavController,
    viewModel: BookDetailsViewModel = hiltViewModel(),
    bookId: String
) {
    // ViewModel SavedStateHandle - survives configuration changes and process death
    val bookIdSavedStateHandle = viewModel.bookId.collectAsStateWithLifecycle().value

    Scaffold(
        topBar = {
            ReaderTopAppBar(title = stringResource(R.string.book_details)) {
                navController.popBackStack()
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                viewModel.getBookInfo(bookId).run { ShowBookDetails(viewModel) }
            }
        }
    }
}

@Composable
fun ShowBookDetails(viewModel: BookDetailsViewModel) {
    val volumeInfo = viewModel.bookItem.collectAsStateWithLifecycle().value.volumeInfo

    if (volumeInfo == null) {
        Column {
            LinearProgressIndicator()
            Text(text = "Loading book...")
        }
    } else {
        Text(text = volumeInfo.title.toString())
        Text(text = volumeInfo.authors.toString())
        Text(text = volumeInfo.categories.toString())
        Text(text = volumeInfo.pageCount.toString())
        //Text(text = volumeInfo.imageLinks?.thumbnail.toString())
    }
}