package com.maddy.jetpackbookreader.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.maddy.jetpackbookreader.features.auth.presentation.CreateAccountScreen
import com.maddy.jetpackbookreader.features.auth.presentation.LoginScreen
import com.maddy.jetpackbookreader.features.auth.presentation.LoginViewModel
import com.maddy.jetpackbookreader.features.bookdetails.presentation.BookDetailsScreen
import com.maddy.jetpackbookreader.features.bookdetails.presentation.BookDetailsViewModel
import com.maddy.jetpackbookreader.features.home.presentation.HomeScreen
import com.maddy.jetpackbookreader.features.home.presentation.NewHomeViewModel
import com.maddy.jetpackbookreader.features.profile.presentation.ReaderStatsScreen
import com.maddy.jetpackbookreader.features.search.presentation.SearchScreen
import com.maddy.jetpackbookreader.features.search.presentation.SearchViewModel
import com.maddy.jetpackbookreader.features.splash.presentation.SplashScreen
import com.maddy.jetpackbookreader.features.updatebook.presentation.UpdateScreen

@Composable
fun ReaderNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ReaderScreens.SplashScreen.name
    ) {
        // NavGraphBuilder
        // route - Similar to a web address, www.google.com/city="Seattle"
        composable(route = ReaderScreens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }
        composable(route = ReaderScreens.LoginScreen.name) {
            val loginViewModel = hiltViewModel<LoginViewModel>()
            LoginScreen(navController = navController, viewModel = loginViewModel)
        }
        composable(route = ReaderScreens.CreateAccountScreen.name) {
            val loginViewModel = hiltViewModel<LoginViewModel>()
            CreateAccountScreen(navController = navController, viewModel = loginViewModel)
        }
        composable(route = ReaderScreens.HomeScreen.name) {
            val newHomeViewModel = hiltViewModel<NewHomeViewModel>()
            HomeScreen(navController = navController, newHomeViewModel= newHomeViewModel)
        }
        composable(route = ReaderScreens.SearchScreen.name) {
            val searchViewModel = hiltViewModel<SearchViewModel>()
            SearchScreen(navController = navController, viewModel = searchViewModel)
        }
        composable(
            route = ReaderScreens.BookDetailsScreen.name + "/{bookId}",
            arguments = listOf(
                navArgument(name = "bookId", builder = { type = NavType.StringType })
            )
        ) { backStackEntry ->
            val bookDetailsViewModel = hiltViewModel<BookDetailsViewModel>()
            backStackEntry.arguments?.getString("bookId")?.let { bookId ->
                BookDetailsScreen(navController, bookDetailsViewModel, bookId)
            }
        }
        composable(
            route = ReaderScreens.UpdateScreen.name + "/{bookId}",
            arguments = listOf(
                navArgument(name = "bookId", builder = { type = NavType.StringType })
            )
        ) { navBackStackEntry ->
            navBackStackEntry.arguments?.getString("bookId")?.let { bookId ->
                val newHomeViewModel = hiltViewModel<NewHomeViewModel>()
                UpdateScreen(navController, newHomeViewModel, bookId)
            }
        }
        composable(route = ReaderScreens.ReaderStatsScreen.name) {
            val newHomeViewModel = hiltViewModel<NewHomeViewModel>()
            ReaderStatsScreen(navController = navController, viewModel = newHomeViewModel)
        }
    }
}