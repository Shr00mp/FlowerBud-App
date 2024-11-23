package com.example.flowerbud.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun PlantApp(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val plantViewModel: PlantViewModel = viewModel()
    Scaffold(
        // Bottom navigation bar
        bottomBar = {
            if (currentDestination?.route != PlantScreens.Login.title) {
                BottomNavigationBar(
                    currentDestination,
                    onTabClick = {
                        navController.navigate(it.title) // When tab in navbar is clicked, navigates to the page
                    },
                    modifier
                )
            }

        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = PlantScreens.Login.title, // Specifies starting destination
            modifier = Modifier.padding(innerPadding)
        ) {
            /*
            Specifies routes for when each page function should be called           
            e.g. If route is PlantScreens.home.title (= "Home"), then HomePage() is called
            */
            composable(route = PlantScreens.Login.title) {
                LoginPage(navController = navController, plantViewModel = plantViewModel, modifier = modifier)
            }
            composable(route = PlantScreens.Home.title) {
                HomePage(navController = navController, plantViewModel = plantViewModel, modifier = modifier)
            }
            composable(route = PlantScreens.Quiz.title) {
                QuizPage(navController = navController, plantViewModel = plantViewModel, modifier = modifier)
            }
            composable(route = PlantScreens.Search.title) {
                SearchPage(navController = navController, plantViewModel = plantViewModel, modifier = modifier)
            }
            composable(route = PlantScreens.Journal.title) {
                JournalPage(navController = navController, plantViewModel = plantViewModel, modifier = modifier)
            }
            composable(route = PlantScreens.CameraScreen.title) {
                CameraScreen(navController = navController, plantViewModel = plantViewModel, modifier = modifier)
            }
            composable(route = PlantScreens.Profile.title) {
                ProfilePage(navController = navController, plantViewModel = plantViewModel, modifier = modifier)
            }
            composable(route = "details/{id}") {
                    backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
                PlantDetailsPage(id, navController = navController, plantViewModel = plantViewModel, modifier = modifier)
            }
        }
    }
}