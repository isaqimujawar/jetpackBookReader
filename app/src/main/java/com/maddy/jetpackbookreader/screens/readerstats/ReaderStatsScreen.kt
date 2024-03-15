package com.maddy.jetpackbookreader.screens.readerstats

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material.icons.sharp.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.maddy.jetpackbookreader.R
import com.maddy.jetpackbookreader.components.ShowProgressIndicator
import com.maddy.jetpackbookreader.model.ReadingBook
import com.maddy.jetpackbookreader.navigation.ReaderScreens
import com.maddy.jetpackbookreader.screens.home.viewModelOld.NewHomeViewModel
import com.maddy.jetpackbookreader.utils.formatDate
import com.maddy.jetpackbookreader.widgets.ReaderTopAppBar

@Composable
fun ReaderStatsScreen(
    navController: NavController,
    viewModel: NewHomeViewModel = hiltViewModel()
) {
    val listOfBooks: List<ReadingBook> = viewModel.getReadingBookList()
    val loading = viewModel.loadingStateFlow.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            ReaderTopAppBar(title = "Book Stats") {
                navController.popBackStack()
            }
        }
    ) {
        Surface(modifier = Modifier.padding(it)) {
            Column {
                ShowUsername(viewModel.getUserDisplayName().uppercase())
                Text(
                    text = "Your Stats",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
                if (loading.value) {
                    ShowProgressIndicator()
                } else {
                    YourStats(navController, viewModel, listOfBooks)
                }

            }
        }

    }
}

@Composable
fun YourStats(
    navController: NavController,
    viewModel: NewHomeViewModel,
    listOfBooks: List<ReadingBook>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,

        ) {
        ReadingCard("You have finished", viewModel.getFinishBookList(listOfBooks), navController)
        ReadingCard("Your are reading", viewModel.getReadingNowBookList(listOfBooks), navController)
        ReadingCard("You have added", viewModel.getAddedBookList(listOfBooks), navController)
    }
}

@Composable
fun ReadingCard(cardTitle: String, listOfBooks: List<ReadingBook>, navController: NavController) {
    val showList = rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { showList.value = !showList.value },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.cardElevation(6.dp),
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "$cardTitle: ${listOfBooks.size} books")
                Icon(
                    imageVector = if (showList.value) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                    contentDescription = stringResource(R.string.toggle_icon)
                )
            }
            if (showList.value) {
                listOfBooks.forEach {
                    BookStatsCard(book = it) {book ->
                        navController.navigate(ReaderScreens.UpdateScreen.name + "/$book")
                    }
                }
            }
        }
    }
}

@Composable
private fun ShowUsername(username: String = "username") {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RectangleShape,
        color = MaterialTheme.colorScheme.primary,
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Sharp.Person,
                contentDescription = stringResource(R.string.person_icon)
            )
            Text(
                text = "Hi, $username",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun BookStatsCard(book: ReadingBook, onClick: (String) -> Unit = {}) {
    val unsplashLink =
        "https://images.unsplash.com/photo-1589829085413-56de8ae18c73?q=80&w=1512&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
    val imageUrl = book.photoUrl ?: unsplashLink
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClick(book.id ?: "") },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(6.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = stringResource(R.string.book_image),
                modifier = Modifier
                    .padding(end = 4.dp)
                    .width(80.dp)
                    .fillMaxHeight(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .fillMaxWidth(0.8f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = book.title.toString(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Authors: ${book.authors.toString()}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontStyle = FontStyle.Italic,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Started: ${formatDate(book.startedReading) ?: "Not Yet"}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontStyle = FontStyle.Italic,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Finished: ${formatDate(book.finishedReading) ?: "Not Yet"}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontStyle = FontStyle.Italic,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (book.yourRating?.toDouble()?.toInt()!! >= 4) {
                Column(
                    modifier = Modifier.padding(4.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ThumbUp,
                        contentDescription = stringResource(R.string.thumbup_icon),
                        tint = Color.Green.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}