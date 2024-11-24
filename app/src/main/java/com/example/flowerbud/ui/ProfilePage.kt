package com.example.flowerbud.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.flowerbud.R

@Composable
fun ProfilePage(
    navController: NavController,
    plantViewModel: PlantViewModel,
    modifier: Modifier = Modifier
) {
    val lightGreen = colorResource(id = R.color.lightGreen)
    val darkBlue = colorResource(id = R.color.darkBlue)

    val uiState by plantViewModel.uiState.collectAsState()

    // favouritePlants stores all <Plant>s in Favourites
    val favouritePlants: List<Plant> =
        uiState.favourites.map { plantId -> allPlants.find { it.plantId == plantId }!! }
    // // myPlants stores all <Plant>s in My Plants
    val myPlants: List<Plant> =
        uiState.myPlants.map { userPlant -> allPlants.find { it.plantId == userPlant.plantId }!! }

    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.width(40.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
//                .height(430.dp)
                .absolutePadding(50.dp, 50.dp, 50.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
            colors = CardDefaults.cardColors(
                containerColor = lightGreen
            )
        ) {
            // Profile image and "Welcome back" text
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Profile picture",
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .size(300.dp)
            )
            Spacer(modifier = Modifier.width(15.dp))

            Text(
                "Welcome back, ${uiState.username}.",
                fontSize = 40.sp,
                style = TextStyle(lineHeight = 48.sp),
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .absolutePadding(40.dp, 0.dp, 40.dp, 40.dp)
            )
        }

        // State to keep track of the selected tab index
        var selectedTabIndex by remember { mutableStateOf(0) }

        // List of tab titles
        val tabs = listOf("My plants", "Favourites")

        // TabRow to display the tabs
        Column(modifier = Modifier.fillMaxWidth()) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier
                    .padding(50.dp, 30.dp)
                    .align(alignment = Alignment.CenterHorizontally),
                contentColor = darkBlue,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = darkBlue, // Change the underline color
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                title,
                                fontSize = 20.sp,
                                modifier = Modifier.absolutePadding(0.dp, 0.dp, 0.dp, 5.dp)
                            )
                        }
                    )
                }
            }

            // Content for each tab, specified using the Tab Index
            when (selectedTabIndex) {
                0 -> TabContent(plantList = myPlants, navController = navController)
                1 -> TabContent(plantList = favouritePlants, navController = navController)
            }
        }

    }

}


@Composable
fun TabContent(plantList: List<Plant>, navController: NavController) {
    // Basic content displayed below the TabRow
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 0.dp)
            .verticalScroll(rememberScrollState())
    ) {
        if (plantList.isEmpty()) {
            Text(
                "No plants available.",
                fontSize = 20.sp,
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(0.dp, 15.dp))
        }
        else {
            for (plant in plantList) {
                PlantCard(plant = plant, navController = navController)
            }
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
}
