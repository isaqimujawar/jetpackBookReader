package com.maddy.jetpackbookreader.navigation

enum class ReaderScreens {
    SplashScreen,
    LoginScreen,
    CreateAccountScreen,
    HomeScreen,
    SearchScreen,
    BookDetailsScreen,
    UpdateScreen,
    ReaderStatsScreen;

    companion object {
        fun fromRoute(route: String?): ReaderScreens =
            when (route?.substringBefore("/")) {
                SplashScreen.name -> SplashScreen
                LoginScreen.name -> LoginScreen
                CreateAccountScreen.name -> CreateAccountScreen
                HomeScreen.name -> HomeScreen
                SearchScreen.name -> SearchScreen
                BookDetailsScreen.name -> BookDetailsScreen
                UpdateScreen.name -> UpdateScreen
                ReaderStatsScreen.name -> ReaderStatsScreen
                null -> HomeScreen

                else -> throw IllegalArgumentException("Route $route is not recognized")
            }
    }
}