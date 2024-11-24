package com.example.flowerbud.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.flowerbud.R


// Function that creates a search bar composable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String, // The current text in the search bar, dynamically updated as the user types
    onQueryChanged: (String) -> Unit, // A callback function that handles changes in the search query (text input)
    onSearch: () -> Unit // A callback function that triggers the search action when the user submits the search
) {
    val darkBlue = colorResource(id = R.color.darkBlue)
    // OutlinedTextField resembles a search bar
    OutlinedTextField(
        shape = RoundedCornerShape(20.dp),
        value = query, // Sets the current text in the field to the value of `query`, updated as the user types
        onValueChange = { newQuery -> // Called every time the text changes
            onQueryChanged(newQuery) // Calls the onQueryChanged function with the updated text. It updates query to newQuery (defined when SearchBar is called in SearchPage() below)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        label = { Text("Search for a plant") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch() // Calls the onSearch function when the search button on the keyboard is pressed
            }
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = darkBlue,
//                unfocusedBorderColor = darkGreen,
            focusedLabelColor = darkBlue,
//                unfocusedLabelColor = darkGreen,
            focusedLeadingIconColor = darkBlue,
//                unfocusedLeadingIconColor = darkGreen
        ),
    )
}



// Function to show plant cards for different plants
@Composable
fun PlantCard(modifier: Modifier = Modifier, plant: Plant, navController: NavController) {
    val lightGreen = colorResource(id = R.color.lightGreen)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(20.dp, 10.dp)
            .clickable {
                // When clicked, navigate to PlantDetailsPage(id = plant.plantId)
                navController.navigate(route = "details/${plant.plantId}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = lightGreen
        )
    ) {
        Row(modifier = Modifier.align(Alignment.Start)) {
            // Image of plant
            Image(
                painter = painterResource(id = plant.image),
                contentDescription = plant.name,
            )
            Column() {
                // Name of plant
                Text(
                    text = plant.name,
                    fontSize = 30.sp,
                    modifier = Modifier.absolutePadding(20.dp, 20.dp)
                )
                // Description of plant
                Text(
                    text = plant.description,
                    fontSize = 20.sp,
                    modifier = Modifier.absolutePadding(20.dp, 10.dp, 20.dp, 10.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Row() {
                    // Icon(s) representing light requirements
                    Card(
                        modifier = Modifier
                            .height(40.dp)
                            .absolutePadding(20.dp, 0.dp, 10.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = lightGreen
                        )
                    ) {
                        Image(
                            painter = painterResource(id = plant.lightImage),
                            contentDescription = "Light intensity icon"
                        )
                    }
                    // Icon(s) representing price
                    Card(
                        modifier = Modifier
                            .height(40.dp)
                            .padding(10.dp, 0.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = lightGreen
                        )
                    ) {
                        Image(
                            painter = painterResource(id = plant.priceImage),
                            contentDescription = "Pound icon"
                        )

                    }
                    // Icon(s) representing water requirements
                    Card(
                        modifier = Modifier
                            .height(40.dp)
                            .padding(10.dp, 0.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = lightGreen
                        )
                    ) {
                        Image(
                            painter = painterResource(id = plant.waterImage),
                            contentDescription = "Water icon"
                        )
                    }
                }

            }

        }
    }
}

// Function for entire search page, including search bar and possible plant cards
@Composable
fun SearchPage(navController: NavController, plantViewModel: PlantViewModel, modifier: Modifier = Modifier) {
    var plantQuery by remember { mutableStateOf("") } // For passing in a mutable "" value for query parameter in SearchBar()
    val selectedPlants = remember {
        mutableStateListOf(*allPlants.toTypedArray())
    } // Stores plants that fit user search
    Column(
        modifier = Modifier
            .absolutePadding(10.dp, 15.dp, 10.dp, 10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        SearchBar(query = plantQuery, onQueryChanged = { q -> plantQuery = q }, onSearch = {
            selectedPlants.clear()
            val plants = getPlantsBySearch(plantQuery) // plants = all plants that fit user search
            selectedPlants.addAll(plants) // Add all plants that fit user search to selectedPlants
        })
        Spacer(modifier = Modifier.padding(0.dp, 15.dp))
        if (selectedPlants.isEmpty()) {
            Text("No plants found.",
                modifier = Modifier
                    .padding(top = 40.dp)
                    .align(alignment = Alignment.CenterHorizontally),
                fontSize = 20.sp)
        }

        // For each plant that fits user search, display plant card
        for (plant in selectedPlants) {
            PlantCard(plant = plant, navController = navController)
        }
    }
}