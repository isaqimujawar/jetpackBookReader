package com.maddy.jetpackbookreader.screens.search

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.maddy.jetpackbookreader.R
import com.maddy.jetpackbookreader.widgets.ReaderTopAppBar

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(navController: NavController) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val searchQueryState = rememberSaveable { mutableStateOf("") }

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
            Column {
                OutlinedTextField(
                    value = searchQueryState.value,
                    onValueChange = { searchQueryState.value = it },
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    enabled = true,
                    label = { Text(text = stringResource(R.string.search)) },
                    placeholder = { Text(text = stringResource(R.string.enter_book_name)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            Log.d("SearchScreen", "query: ${searchQueryState.value}")
                        }
                    ),
                    singleLine = true,
                    maxLines = 1,
                    shape = RoundedCornerShape(size = 15.dp),
                )
            }
        }
    }
}