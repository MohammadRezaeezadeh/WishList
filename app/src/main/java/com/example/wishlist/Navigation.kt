package com.example.wishlist


import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

object NavArgs {
    const val ID = "id"
}

@Composable
fun Navigation(
    viewModel: WishViewModel = viewModel(),
    navhostcontroller: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navhostcontroller,
        startDestination = Screen.HomeScrren.route
    ) {
        // صفحه اصلی برای نمایش لیست آرزوها
        composable(Screen.HomeScrren.route) {
            HomeView(navcontroller = navhostcontroller, viewmodel = viewModel)
        }
        // صفحه افزودن یا ویرایش آرزو با پارامتر id
        composable(
            route = "${Screen.AddScreen.route}/{${NavArgs.ID}}",
            arguments = listOf(
                navArgument(name = NavArgs.ID) {
                    type = NavType.LongType
                    defaultValue = 0L
                    nullable = false
                }
            )
        ) { entry ->
            val id = entry.arguments?.getLong(NavArgs.ID) ?: 0L
            AddDetalsView(id = id, viewmodel = viewModel, navcontroller = navhostcontroller)
        }
    }
}