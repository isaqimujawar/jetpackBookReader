package com.maddy.jetpackbookreader.features.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.maddy.jetpackbookreader.R
import com.maddy.jetpackbookreader.common.model.ReadingBook
import com.maddy.jetpackbookreader.common.presentation.components.RoundedButton
import com.maddy.jetpackbookreader.common.presentation.components.ShowFavoriteIcon
import com.maddy.jetpackbookreader.common.presentation.components.ShowProgressIndicator
import com.maddy.jetpackbookreader.common.presentation.components.ShowText
import com.maddy.jetpackbookreader.common.presentation.components.TitleText
import com.maddy.jetpackbookreader.common.presentation.widgets.HomeTopAppBar
import com.maddy.jetpackbookreader.common.utils.getBook
import com.maddy.jetpackbookreader.navigation.ReaderScreens
import com.maddy.jetpackbookreader.ui.theme.JetpackBookReaderTheme

@Composable
fun HomeScreen(
    navController: NavController,
    newHomeViewModel: NewHomeViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            HomeTopAppBar(
                onRefreshClicked = { newHomeViewModel.getAllBooks() }
            ) {
                newHomeViewModel.signOut().run {
                    navController.navigate(route = ReaderScreens.LoginScreen.name) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            FABAddBook {
                navController.navigate(route = ReaderScreens.SearchScreen.name)
            }
        },
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues = paddingValues)
                .fillMaxSize()
        ) {
            // HomeContent(navController, viewModel)
            HomeContent(navController, newHomeViewModel)
        }
    }
}

@Composable
private fun FABAddBook(onFABClicked: () -> Unit) {
    FloatingActionButton(
        onClick = { onFABClicked() },
        modifier = Modifier,
        shape = RoundedCornerShape(50.dp),
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.add_button),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun HomeContent(
    navController: NavController,
    viewModel: NewHomeViewModel,
    modifier: Modifier = Modifier
) {
    val displayName = viewModel.getUserDisplayName()
    val listOfBooks: List<ReadingBook> = viewModel.getReadingBookList()
    val loading = viewModel.loadingStateFlow.collectAsStateWithLifecycle()

    // if (listOfBooks.isEmpty()) ShowProgressIndicator()
    if (loading.value) {
        ShowProgressIndicator()
    } else {
        if (listOfBooks.isNullOrEmpty()) NoBookFoundText(navController)
        else ShowHomeScreen(modifier, displayName, navController, viewModel, listOfBooks)
    }
}

@Composable
private fun NoBookFoundText(navController: NavController) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        TitleSection(displayName = "username") {
            navController.navigate(route = ReaderScreens.ReaderStatsScreen.name)
        }
        ShowText(text = "No Books found. Add a Book")
    }
}

@Composable
private fun ShowHomeScreen(
    modifier: Modifier,
    displayName: String,
    navController: NavController,
    viewModel: NewHomeViewModel,
    listOfBooks: List<ReadingBook>
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TitleSection(modifier, displayName) {
            navController.navigate(route = ReaderScreens.ReaderStatsScreen.name)
        }
        ReadingNowBookList(navController, viewModel, listOfBooks)
        Spacer(modifier = Modifier.height(12.dp))
        TitleText("Added List")
        AddedBookList(navController, viewModel, listOfBooks)
    }
}

@Composable
private fun TitleSection(
    modifier: Modifier = Modifier,
    displayName: String = "Title",
    onProfileClicked: () -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TitleText("My Reading Now")
        Column(
            modifier = Modifier.clickable { onProfileClicked() },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = stringResource(R.string.profile_icon),
                modifier = Modifier.size(50.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = displayName,
                modifier = Modifier.width(65.dp),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            HorizontalDivider(modifier = Modifier.width(60.dp))
        }
    }
}

@Composable
fun BookCard(book: ReadingBook, buttonText: String, onClick: (String?) -> Unit = {}) {
    Card(
        modifier = Modifier
            .padding(12.dp)
            .width(260.dp)
            .wrapContentHeight()
            .clickable { onClick(book.id) },
        shape = RoundedCornerShape(29.dp),
        elevation = CardDefaults.cardElevation(6.dp),
    ) {
        Column {
            BookImageAndRating(imageUrl = book.photoUrl, rating = book.yourRating ?: "N/A")
            BookTitleAndAuthor(book.title, book.authors)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RoundedButton(buttonText)
            }
        }
    }
}

@Composable
private fun BookImageAndRating(imageUrl: String?, rating: String) {
    val photoUrl = imageUrl ?: stringResource(R.string.stock_image_unsplash_url)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = photoUrl),
            contentDescription = stringResource(R.string.book_image),
            modifier = Modifier
                .width(200.dp)
                .fillMaxHeight(),
            contentScale = ContentScale.Crop
        )
        BookRating(rating)
    }
}

@Composable
fun BookRating(rating: String) {
    val yourRating = rating.toDouble().toInt()
    Column(
        modifier = Modifier.padding(end = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ShowFavoriteIcon(rating = yourRating)
        Spacer(modifier = Modifier.height(16.dp))
        Surface(
            shape = RoundedCornerShape(56.dp),
            shadowElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.padding(2.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Rounded.Star,
                    contentDescription = stringResource(R.string.star_icon),
                    tint = Color(0xFFFFD700)
                )
                Text(
                    text = "${yourRating}.0", style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}

@Composable
private fun BookTitleAndAuthor(title: String?, author: String?) {
    Column(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = title ?: "Book Title",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = author ?: "Author",
            style = MaterialTheme.typography.labelLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ReadingNowBookList(
    navController: NavController,
    viewModel: NewHomeViewModel,
    listOfBooks: List<ReadingBook>
) {
    val readingBooks = viewModel.getReadingNowBookList(listOfBooks)

    if (readingBooks.isNullOrEmpty()) {
        ShowText(text = "Start Reading a Book")
    } else {
        LazyRow {
            items(items = readingBooks) {
                BookCard(book = it, buttonText = "Reading") { bookId ->
                    navController.navigate(ReaderScreens.UpdateScreen.name + "/$bookId")
                }
            }
        }
    }
}

@Composable
fun AddedBookList(
    navController: NavController,
    viewModel: NewHomeViewModel,
    listOfBooks: List<ReadingBook>
) {
    val addedBooksList = viewModel.getAddedBookList(listOfBooks)

    if (addedBooksList.isNullOrEmpty()) {
        ShowText(text = "Add a Book")
    } else {
        LazyRow {
            items(items = addedBooksList) {
                BookCard(book = it, buttonText = "Added") { bookId ->
                    navController.navigate(ReaderScreens.UpdateScreen.name + "/$bookId")
                }
            }
        }
    }
}



@Preview(showSystemUi = true)
@Composable
fun BookCardPreview() {
    JetpackBookReaderTheme { BookCard(getBook(), "Added") }
}