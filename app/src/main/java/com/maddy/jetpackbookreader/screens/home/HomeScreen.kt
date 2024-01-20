package com.maddy.jetpackbookreader.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.maddy.jetpackbookreader.R
import com.maddy.jetpackbookreader.navigation.ReaderScreens

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val displayName = viewModel.getUserDisplayName()

    Scaffold(
        topBar = { HomeTopAppBar(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ },
                modifier = Modifier,
                shape = RoundedCornerShape(50.dp),
                containerColor = MaterialTheme.colorScheme.background
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_button),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues = paddingValues)
                .fillMaxSize()
        ) {
            HomeContent(
                navController,
                displayName = displayName
            )
        }
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
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "My Reading Activity",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Column(
                modifier = Modifier.clickable {
                    navController.navigate(route = ReaderScreens.ReaderStatsScreen.name)
                },
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
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun HomeTopAppBar(navController: NavController, modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Book Reader",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
            )
        },
        modifier = modifier
            .padding(8.dp)
            .shadow(elevation = 0.dp),
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = stringResource(R.string.logo_icon)
            )
        },
        actions = {
            IconButton(
                onClick = {
                    FirebaseAuth.getInstance().signOut().run {
                        navController.navigate(route = ReaderScreens.LoginScreen.name) {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                        }
                    }
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.logout_icon),
                    contentDescription = stringResource(R.string.logout_icon)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
    )
}

/*
@Preview(name = "home screen", showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    JetpackBookReaderTheme {
       // HomeScreen()
    }
}*/
