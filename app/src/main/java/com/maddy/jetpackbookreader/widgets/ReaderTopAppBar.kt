package com.maddy.jetpackbookreader.widgets

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.maddy.jetpackbookreader.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderTopAppBar(
    title: String = stringResource(R.string.title),
    onNavigationClicked: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
            )
        },
        modifier = Modifier
            .padding(bottom = 2.dp)
            .shadow(elevation = 0.dp),
        navigationIcon = {
            IconButton(onClick = { onNavigationClicked() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back_arrow_icon),
                )
            }
        },
        actions = {},
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
    )
}