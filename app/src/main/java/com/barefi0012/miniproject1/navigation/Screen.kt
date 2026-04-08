package com.barefi0012.miniproject1.navigation

sealed class Screen(val route: String) {
    data object Main : Screen("main_screen")
    data object About : Screen("about_screen")
}