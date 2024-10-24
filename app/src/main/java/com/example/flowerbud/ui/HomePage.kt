package com.example.flowerbud.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun HomePage(navController: NavController, plantViewModel: PlantViewModel, modifier: Modifier = Modifier) {
    Text("The home page")
}