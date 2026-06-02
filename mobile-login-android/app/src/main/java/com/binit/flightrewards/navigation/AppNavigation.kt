package com.binit.flightrewards.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.binit.flightrewards.ui.screens.HomeScreen
import com.binit.flightrewards.ui.screens.LoginScreen
import com.binit.flightrewards.viewmodel.LoginViewModel

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val viewModel: LoginViewModel =
        hiltViewModel()

    val isLoggedIn by
    viewModel.isLoggedIn.collectAsState()

    val startDestination =
        if (isLoggedIn) {
            Routes.Home.route
        } else {
            Routes.Login.route
        }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(Routes.Login.route) {

            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {

                    navController.navigate(
                        Routes.Home.route
                    ) {

                        popUpTo(
                            Routes.Login.route
                        ) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Routes.Home.route) {

            HomeScreen(

                onLogout = {

                    viewModel.logout()

                    navController.navigate(
                        Routes.Login.route
                    ) {

                        popUpTo(
                            Routes.Home.route
                        ) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}