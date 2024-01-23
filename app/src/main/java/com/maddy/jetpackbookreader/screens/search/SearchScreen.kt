package com.maddy.jetpackbookreader.screens.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.maddy.jetpackbookreader.widgets.ReaderTopAppBar

@Composable
fun SearchScreen(navController: NavController) {
    Scaffold(
        topBar = {
            ReaderTopAppBar { navController.popBackStack() }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues = paddingValues)
                .fillMaxSize()
        ) {
            Text(text = "Search Books")
        }
    }
}