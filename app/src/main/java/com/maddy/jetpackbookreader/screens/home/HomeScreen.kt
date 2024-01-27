package com.maddy.jetpackbookreader.screens.home

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
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.maddy.jetpackbookreader.R
import com.maddy.jetpackbookreader.components.RoundedButton
import com.maddy.jetpackbookreader.components.TitleText
import com.maddy.jetpackbookreader.model.ReadingBook
import com.maddy.jetpackbookreader.navigation.ReaderScreens
import com.maddy.jetpackbookreader.ui.theme.JetpackBookReaderTheme
import com.maddy.jetpackbookreader.utils.getBook
import com.maddy.jetpackbookreader.widgets.HomeTopAppBar

@Composable
fun HomeScreen(
    navController: NavController, viewModel: HomeViewModel = hiltViewModel()
) {
    val displayName = viewModel.getUserDisplayName()

    Scaffold(
        topBar = {
            HomeTopAppBar {
                viewModel.signOut().run {
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
            HomeContent(navController, displayName = displayName)
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
    modifier: Modifier = Modifier,
    displayName: String
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
        BookCard(book = getBook()) {
            // Todo("Card OnClick impl")
        }
        Spacer(modifier = Modifier.height(12.dp))
        TitleText("Reading List")
        ReadingList(listOfBooks = listOf(getBook(), getBook(), getBook()))
    }
}

@Composable
private fun TitleSection(
    modifier: Modifier,
    displayName: String,
    onProfileClicked: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TitleText("My Reading Activity")
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
                modifier = Modifier.width(60.dp),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Clip
            )
            Divider(modifier = Modifier.width(60.dp))
        }
    }
}

@Composable
fun BookCard(book: ReadingBook, onClick: (String?) -> Unit = {}) {
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
            BookImageAndRating(imageUrl = "", rating = 4.5.toString())
            BookTitleAndAuthor(book.title, book.author)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RoundedButton("Reading")
            }
        }
    }
}

@Composable
private fun BookImageAndRating(imageUrl: String, rating: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.books_unsplash),
            modifier = Modifier
                .width(200.dp)
                .fillMaxHeight(),
            contentDescription = stringResource(R.string.book_image),
            contentScale = ContentScale.Crop
        )
        BookRating(rating)
    }
}

@Composable
fun BookRating(rating: String = "4.0") {
    Column(
        modifier = Modifier.padding(end = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Rounded.FavoriteBorder,
            contentDescription = stringResource(R.string.favorite_icon),
            modifier = Modifier.padding(top = 2.dp)
        )
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
                    contentDescription = stringResource(R.string.star_icon)
                )
                Text(
                    text = rating, style = MaterialTheme.typography.headlineSmall
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
            style = MaterialTheme.typography.headlineMedium,
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
fun ReadingList(listOfBooks: List<ReadingBook>) {
    LazyRow {
        items(items = listOfBooks) {
            BookCard(book = it) {
                // TODO("Card OnClick impl")
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun BookCardPreview() {
    JetpackBookReaderTheme { BookCard(getBook()) }
}