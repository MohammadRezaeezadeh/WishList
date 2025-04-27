package com.example.wishlist

sealed class Screen(val route: String){
    object HomeScrren: Screen("home_screen")
    object AddScreen: Screen("add_screen")
}