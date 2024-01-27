package com.maddy.jetpackbookreader.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.maddy.jetpackbookreader.screens.details.BookDetailsScreen
import com.maddy.jetpackbookreader.screens.details.BookDetailsViewModel
import com.maddy.jetpackbookreader.screens.home.HomeScreen
import com.maddy.jetpackbookreader.screens.home.HomeViewModel
import com.maddy.jetpackbookreader.screens.login.CreateAccountScreen
import com.maddy.jetpackbookreader.screens.login.LoginScreen
import com.maddy.jetpackbookreader.screens.login.LoginViewModel
import com.maddy.jetpackbookreader.screens.readerstats.ReaderStatsScreen
import com.maddy.jetpackbookreader.screens.search.SearchScreen
import com.maddy.jetpackbookreader.screens.search.SearchViewModel
import com.maddy.jetpackbookreader.screens.splash.SplashScreen
import com.maddy.jetpackbookreader.screens.update.UpdateScreen

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
            val homeViewModel = hiltViewModel<HomeViewModel>()
            HomeScreen(navController = navController, viewModel = homeViewModel)
        }
        composable(route = ReaderScreens.SearchScreen.name) {
            val searchViewModel = hiltViewModel<SearchViewModel>()
            SearchScreen(navController = navController, viewModel = searchViewModel)
        }
        val routeAddress = ReaderScreens.BookDetailsScreen.name + "/{bookId}"
        composable(
            route = routeAddress,
            arguments = listOf(
                navArgument(name = "bookId", builder = { type = NavType.StringType })
            )
        ) { backStackEntry ->
            val bookDetailsViewModel = hiltViewModel<BookDetailsViewModel>()
            backStackEntry.arguments?.getString("bookId")?.let {
                BookDetailsScreen(
                    navController = navController,
                    viewModel = bookDetailsViewModel,
                    bookId = it
                )
            }
        }
        composable(route = ReaderScreens.UpdateScreen.name) {
            UpdateScreen(navController = navController)
        }
        composable(route = ReaderScreens.ReaderStatsScreen.name) {
            ReaderStatsScreen(navController = navController)
        }
    }
}