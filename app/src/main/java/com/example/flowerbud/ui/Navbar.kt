package com.example.flowerbud.ui


import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import com.example.flowerbud.R


@Composable
fun BottomNavigationBar(
    currentDestination: NavDestination?, // Determines the active tab
    onTabClick: (PlantScreens) -> Unit, // Handles tab click events
    modifier: Modifier = Modifier
) {
    val lightGreen = colorResource(id = R.color.lightGreen)
    val middleGreen = colorResource(id = R.color.middleGreen)
    val darkGreen = colorResource(id = R.color.darkGreen)
    val darkBlue = colorResource(id = R.color.darkBlue)
    val darkGrey = colorResource(id = R.color.darkGrey)
    val lightGrey = colorResource(id = R.color.lightGrey)
    // Navbar
    NavigationBar(
        containerColor = lightGrey,
        modifier = Modifier.clip(RoundedCornerShape(20.dp))
    ) {
        // Home tab
        NavigationBarItem(
            selected = PlantScreens.Home.title == currentDestination?.route, // Mark as selected if current route matches Home route
            label = { Text(PlantScreens.Home.title) },
            icon = { Icon(Icons.Filled.Home, contentDescription = PlantScreens.Home.title) },
            colors = NavigationBarItemColors(
                selectedIconColor = darkGrey,
                selectedTextColor = darkGrey,
                selectedIndicatorColor = middleGreen,
                unselectedIconColor = darkGrey,
                unselectedTextColor = darkGrey,
                disabledIconColor = darkGrey,
                disabledTextColor = darkGrey
            ),
            onClick = { onTabClick(PlantScreens.Home) } // Handles event when tab is clicked (specified in PlantApp())
        )
        // Quiz tab
        NavigationBarItem(
            selected = PlantScreens.Quiz.title == currentDestination?.route,
            label = { Text(PlantScreens.Quiz.title) },
            icon = { Icon(Icons.Filled.Info, contentDescription = PlantScreens.Quiz.title) },
            colors = NavigationBarItemColors(
                selectedIconColor = darkGrey,
                selectedTextColor = darkGrey,
                selectedIndicatorColor = middleGreen,
                unselectedIconColor = darkGrey,
                unselectedTextColor = darkGrey,
                disabledIconColor = darkGrey,
                disabledTextColor = darkGrey
            ),
            onClick = { onTabClick(PlantScreens.Quiz) }
        )
        // Search tab
        NavigationBarItem(
            selected = PlantScreens.Search.title == currentDestination?.route,
            label = { Text(PlantScreens.Search.title) },
            icon = { Icon(Icons.Filled.Search, contentDescription = PlantScreens.Search.title) },
            colors = NavigationBarItemColors(
                selectedIconColor = darkGrey,
                selectedTextColor = darkGrey,
                selectedIndicatorColor = middleGreen,
                unselectedIconColor = darkGrey,
                unselectedTextColor = darkGrey,
                disabledIconColor = darkGrey,
                disabledTextColor = darkGrey
            ),
            onClick = { onTabClick(PlantScreens.Search) }
        )
        // Journal tab
        NavigationBarItem(
            selected = PlantScreens.Journal.title == currentDestination?.route,
            label = { Text(PlantScreens.Journal.title) },
            icon = { Icon(Icons.Filled.DateRange, contentDescription = PlantScreens.Journal.title) },
            colors = NavigationBarItemColors(
                selectedIconColor = darkGrey,
                selectedTextColor = darkGrey,
                selectedIndicatorColor = middleGreen,
                unselectedIconColor = darkGrey,
                unselectedTextColor = darkGrey,
                disabledIconColor = darkGrey,
                disabledTextColor = darkGrey
            ),
            onClick = { onTabClick(PlantScreens.Journal) }
        )
        // Profile tab
        NavigationBarItem(
            selected = PlantScreens.Profile.title == currentDestination?.route,
            label = { Text(PlantScreens.Profile.title) },
            icon = { Icon(Icons.Filled.Person, contentDescription = PlantScreens.Profile.title) },
            colors = NavigationBarItemColors(
                selectedIconColor = darkGrey,
                selectedTextColor = darkGrey,
                selectedIndicatorColor = middleGreen,
                unselectedIconColor = darkGrey,
                unselectedTextColor = darkGrey,
                disabledIconColor = darkGrey,
                disabledTextColor = darkGrey
            ),
            onClick = { onTabClick(PlantScreens.Profile) }
        )
    }
}