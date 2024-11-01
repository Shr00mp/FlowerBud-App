package com.example.flowerbud.ui


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination


@Composable
fun BottomNavigationBar(
    currentDestination: NavDestination?, // Determines the active tab
    onTabClick: (PlantScreens) -> Unit, // Handles tab click events
    modifier: Modifier = Modifier
) {
    // Navbar
    NavigationBar {
        // Home tab
        NavigationBarItem(
            selected = PlantScreens.Home.title == currentDestination?.route, // Mark as selected if current route matches Home route
            label = { Text(PlantScreens.Home.title) },
            icon = { Icon(Icons.Filled.Home, contentDescription = PlantScreens.Home.title) },
            onClick = { onTabClick(PlantScreens.Home) } // Handles event when tab is clicked (specified in PlantApp())
        )
        // Quiz tab
        NavigationBarItem(
            selected = PlantScreens.Quiz.title == currentDestination?.route,
            label = { Text(PlantScreens.Quiz.title) },
            icon = { Icon(Icons.Filled.Info, contentDescription = PlantScreens.Quiz.title) },
            onClick = { onTabClick(PlantScreens.Quiz) }
        )
        // Search tab
        NavigationBarItem(
            selected = PlantScreens.Search.title == currentDestination?.route,
            label = { Text(PlantScreens.Search.title) },
            icon = { Icon(Icons.Filled.Search, contentDescription = PlantScreens.Search.title) },
            onClick = { onTabClick(PlantScreens.Search) }
        )
        // Journal tab
        NavigationBarItem(
            selected = PlantScreens.Journal.title == currentDestination?.route,
            label = { Text(PlantScreens.Journal.title) },
            icon = { Icon(Icons.Filled.DateRange, contentDescription = PlantScreens.Journal.title) },
            onClick = { onTabClick(PlantScreens.Journal) }
        )
        // Profile tab
        NavigationBarItem(
            selected = PlantScreens.Profile.title == currentDestination?.route,
            label = { Text(PlantScreens.Profile.title) },
            icon = { Icon(Icons.Filled.Person, contentDescription = PlantScreens.Profile.title) },
            onClick = { onTabClick(PlantScreens.Profile) }
        )
    }
}