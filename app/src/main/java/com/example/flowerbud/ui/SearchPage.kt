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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.flowerbud.R

@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearch: () -> Unit
) {
    // OutlinedTextField for the search bar appearance
    OutlinedTextField(
        shape = RoundedCornerShape(20.dp),
        value = query, // The current text in the search bar
        onValueChange = { newQuery ->
            onQueryChanged(newQuery) // Update the query whenever the text changes
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), // Adding some padding around the search bar
        label = { Text("Search") }, // Placeholder or label text
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        },
        singleLine = true, // Make the search bar single line
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search // Sets the keyboard action to "Search"
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch() // Trigger the search action
            }
        )
    )
}

@Composable
fun PlantCard(modifier: Modifier = Modifier, plant: Plant, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(20.dp, 10.dp)
            .clickable {
                navController.navigate(route = "details/${plant.plantId}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(modifier = Modifier.align(Alignment.Start)) {
            Image(
                painter = painterResource(id = plant.image),
                contentDescription = plant.name,
            )
            Column() {
                Text(
                    text = plant.name,
                    fontSize = 30.sp,
                    modifier = Modifier.absolutePadding(20.dp, 20.dp)
                )
                Text(
                    text = plant.description,
                    fontSize = 20.sp,
                    modifier = Modifier.absolutePadding(20.dp, 10.dp, 20.dp, 10.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Row() {
                    Card(
                        modifier = Modifier
                            .height(40.dp)
                            .absolutePadding(20.dp, 0.dp, 10.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Image(
                            painter = painterResource(id = plant.lightImage),
                            contentDescription = "Light needed icon"
                        )
                    }

                    Card(
                        modifier = Modifier
                            .height(40.dp)
                            .padding(10.dp, 0.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Image(
                            painter = painterResource(id = plant.priceImage),
                            contentDescription = "Pound icon"
                        )

                    }

                    Card(
                        modifier = Modifier
                            .height(40.dp)
                            .padding(10.dp, 0.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Image(
                            painter = painterResource(id = plant.waterImage),
                            contentDescription = "Pound icon"
                        )
                    }
                }

            }

        }
    }
}

@Composable
fun SearchPage(navController: NavController, plantViewModel: PlantViewModel, modifier: Modifier = Modifier) {
    var plantQuery by remember { mutableStateOf("") }
    val selectedPlants = remember {
        mutableStateListOf(*allPlants.toTypedArray())
    }
    Column(
        modifier = Modifier
            .absolutePadding(10.dp, 15.dp, 10.dp, 10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        SearchBar(query = plantQuery, onQueryChanged = { q -> plantQuery = q }, onSearch = {
            selectedPlants.clear()
            val plants = getPlantsBySearch(plantQuery)
            selectedPlants.addAll(plants)
        })
        Spacer(modifier = Modifier.padding(0.dp, 15.dp))
        for (plant in selectedPlants) {
            PlantCard(plant = plant, navController = navController)
        }
    }
}