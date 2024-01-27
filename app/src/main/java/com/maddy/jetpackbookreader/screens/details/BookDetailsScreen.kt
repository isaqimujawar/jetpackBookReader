package com.maddy.jetpackbookreader.screens.details

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.maddy.jetpackbookreader.R
import com.maddy.jetpackbookreader.components.RoundedButton
import com.maddy.jetpackbookreader.components.formatHttpText
import com.maddy.jetpackbookreader.model.Item
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
            viewModel.getBookInfo(bookId).run { ShowBookDetails(navController, viewModel) }
        }
    }
}

@Composable
fun ShowBookDetails(navController: NavController, viewModel: BookDetailsViewModel) {
    val bookItem = viewModel.bookItem.collectAsStateWithLifecycle().value

    if (bookItem != null)
        BookDetails(navController, viewModel, bookItem)
    else
        ShowProgressIndicator()
}

@Composable
private fun ShowProgressIndicator() {
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
private fun BookDetails(
    navController: NavController,
    viewModel: BookDetailsViewModel,
    bookItem: Item
) {
    val context = LocalContext.current
    val volumeInfo = bookItem.volumeInfo

    Column(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = volumeInfo?.imageLinks?.thumbnail),
                contentDescription = stringResource(R.string.book_image),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Column(
                modifier = Modifier.height(120.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RoundedButton(text = "Save") {
                    // Save book to Firestore
                    viewModel.saveToFirebase(bookItem) {
                        Toast.makeText(context, "Book Saved", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                }
                RoundedButton(text = "Back") {
                    navController.popBackStack()
                }
            }
        }
        Text(
            text = volumeInfo?.title.toString(),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = stringResource(R.string.authors) + volumeInfo?.authors.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = stringResource(R.string.categories) + volumeInfo?.categories.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = stringResource(R.string.page_count) + volumeInfo?.pageCount.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Surface(
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface)
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top
            ) {
                Text(text = formatHttpText(httpText = volumeInfo?.description))
            }
        }
    }
}