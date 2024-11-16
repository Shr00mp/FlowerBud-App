package com.example.flowerbud.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.flowerbud.R

@Composable
fun PlantDetailsPage(id: String?, navController: NavController, plantViewModel: PlantViewModel, modifier: Modifier = Modifier) {
    val plant = idToPlant(id) // Get plant from the plant id
    val uiState by plantViewModel.uiState.collectAsState()
    val isFavourites = uiState.favourites.contains(id) // = true if plant is already in Favourites and vice versa
    val isMyPlants = uiState.myPlants.any { it.plantId == id } // = true if plant is already in MyPlants and vice versa

    val lightGreen = colorResource(id = R.color.lightGreen)
    val darkGreen = colorResource(id = R.color.darkGreen)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Fixed image at the top, i.e. stays when scrolling
        Image(
            painter = painterResource(id = plant.image),
            contentDescription = "Fixed Image",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        )

        // Scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 500.dp) // Padding to start below the image

        ) {
            // Card that slightly overlaps the image
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-50).dp), // Negative offset to overlap the image
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                shape = RoundedCornerShape(50.dp),
                colors = CardDefaults.cardColors(
                    containerColor = lightGreen
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(40.dp)
                ) {
                    Text(
                        text = plant.name,
                        fontSize = 40.sp
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Row() {
                        // Add to My Plants button
                        Button(
                            // Adds to / Removes from My Plants depending on if already in My Plants
                            onClick = {
                                if (!isMyPlants) {
                                    plantViewModel.addToMyPlants(plant)
                                }
                                else {
                                    plantViewModel.removeFromMyPlants(plant.plantId)
                                }
                            },
                            modifier = Modifier
                                .height(55.dp)
                                .width(450.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = darkGreen
                            )
                        ) {
                            // Shows different text depending on if it's already in My Plants or not
                            Text(
                                text = if (isMyPlants) "Remove from My Plants" else "Add to My Plants",
                                fontSize = 25.sp)
                        }
                        Spacer(modifier = Modifier.width(40.dp))

                        // Favourites button
                        IconButton(
                            // Adds to / Removes from Favourites depending on if already in Favourites
                            onClick = {
                                if (!isFavourites) {
                                    plantViewModel.addToFavourites(plant.plantId)
                                }
                                else {
                                    plantViewModel.removeFromFavourites(plant.plantId)
                                }
                            },
                            modifier = Modifier.size(55.dp).absolutePadding(0.dp, 0.dp, 0.dp, 5.dp),
                        ) {
                            Icon(
                                imageVector = if (isFavourites) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = "Heart Icon",
                                modifier = Modifier.size(40.dp),
                                tint = if (isFavourites) darkGreen else Color.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    // Description of plant
//                    Text(
//                        text = "Description",
//                        fontSize = 30.sp
//                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = plant.description,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(25.dp))
                    Row() {
                        // Water requirement details
                        Card(
                            modifier = Modifier
                                .height(55.dp).width(245.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = lightGreen
                            )
                        ) {
                            Row() {
                                Image(
                                    painter = painterResource(R.drawable.watericon),
                                    contentDescription = "Water icon"
                                )
                                if (plant.waterWeek == 1) {
                                    Text(
                                        text = "Every week",
                                        modifier = Modifier.absolutePadding(5.dp, 15.dp),
                                        fontSize = 25.sp
                                    )
                                }
                                else {
                                    Text(
                                        text = "Every ${plant.waterWeek} weeks",
                                        modifier = Modifier.absolutePadding(5.dp, 14.dp),
                                        fontSize = 23.sp
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                        // Sunlight requirement details
                        Card(
                            modifier = Modifier
                                .height(55.dp).width(245.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = lightGreen
                            )
                        ) {
                            Row() {
                                Image(
                                    painter = painterResource(plant.lightImage),
                                    contentDescription = "Water icon"
                                )
                                Text(
                                    text = plant.plantSun,
                                    modifier = Modifier.absolutePadding(5.dp, 14.dp),
                                    fontSize = 23.sp
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Row() {
                        // Price details
                        Card(
                            modifier = Modifier
                                .height(55.dp).width(245.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = lightGreen
                            )
                        ) {
                            Row() {
                                Image(
                                    painter = painterResource(R.drawable.poundicon),
                                    contentDescription = "Water icon"
                                )
                                Text (
                                    text = plant.plantPrice,
                                    modifier = Modifier.absolutePadding(5.dp, 14.dp),
                                    fontSize = 23.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                        // Toxic or not details
                        Card(
                            modifier = Modifier
                                .height(55.dp).width(245.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = lightGreen
                            )
                        ) {
                            Row() {
                                Image(
                                    painter = painterResource(R.drawable.caticon),
                                    contentDescription = "Water icon"
                                )
                                var toxicText = "Pet-friendly"
                                if (plant.toxic) {
                                    toxicText = "Non pet-friendly"
                                }
                                Text(
                                    text = toxicText,
                                    modifier = Modifier.absolutePadding(5.dp, 14.dp),
                                    fontSize = 23.sp
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Row() {
                        // Indoor or Outdoor details
                        Card(
                            modifier = Modifier
                                .height(55.dp).width(245.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = lightGreen
                            )
                        ) {
                            Row() {
                                Image(
                                    painter = painterResource(R.drawable.locationicon),
                                    contentDescription = "Location icon"
                                )
                                var IOText = "Indoor"
                                if (plant.outdoor) {
                                    IOText = "Outdoor"
                                }
                                Text (
                                    text = IOText,
                                    modifier = Modifier.absolutePadding(5.dp, 14.dp),
                                    fontSize = 23.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                        // Size details
                        Card(
                            modifier = Modifier
                                .height(55.dp).width(245.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = lightGreen
                            )
                        ) {
                            Row() {
                                Image(
                                    painter = painterResource(R.drawable.rulericon),
                                    contentDescription = "Water icon"
                                )
                                Text(
                                    text = plant.height,
                                    modifier = Modifier.absolutePadding(5.dp, 14.dp),
                                    fontSize = 23.sp
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))

                    // Common issues of plant
                    Text(
                        text = "Common Issues",
                        fontSize = 30.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = plant.commonIssues,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(30.dp))

                    // Helpful tips for plant
                    Text(
                        text = "Helpful Tips",
                        fontSize = 30.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = plant.issueSolutions,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}