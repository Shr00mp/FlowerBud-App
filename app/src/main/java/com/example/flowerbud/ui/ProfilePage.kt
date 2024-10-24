package com.example.flowerbud.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.flowerbud.R

@Composable
fun ProfilePage(navController: NavController, plantViewModel: PlantViewModel, modifier: Modifier = Modifier) {
    Column (modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.width(40.dp))
        Image(
            painter = painterResource(id = R.drawable.profile),
            contentDescription = "Profile picture",
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .size(300.dp)
        )
        Spacer(modifier = Modifier.width(15.dp))
        val username = "Sharon"
        Text(
            "Welcome back, $username.",
            fontSize = 40.sp,
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
        )

        // State to keep track of the selected tab index
        var selectedTabIndex by remember { mutableStateOf(0) }

        // List of tab titles
        val tabs = listOf("My plants", "Favourites")

        // TabRow to display the tabs
        Column (modifier = Modifier.fillMaxWidth()) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.padding(50.dp, 30.dp).align(alignment = Alignment.CenterHorizontally),
//            containerColor = MaterialTheme.colorScheme.primary,
//            contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title, fontSize = 20.sp, modifier = Modifier.absolutePadding(0.dp, 0.dp, 0.dp, 5.dp)) }
                    )
                }
            }

            // Content for each tab
            when (selectedTabIndex) {
                0 -> TabContent(text = "This is content for Tab 1")
                1 -> TabContent(text = "This is content for Tab 2")
            }
        }

    }

}


@Composable
fun TabContent(text: String) {
    // Basic content displayed below the TabRow
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(text)
    }
}
