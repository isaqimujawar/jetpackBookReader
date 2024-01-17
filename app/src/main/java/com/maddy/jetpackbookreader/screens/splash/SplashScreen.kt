package com.maddy.jetpackbookreader.screens.splash

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun SplashScreen(navController: NavController) {
    Column {
        Text(text = "SplashScreen of Book Reader App")
    }
}