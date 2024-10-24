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
import androidx.compose.material.icons.filled.DateRange
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.flowerbud.R

@Composable
fun PlantDetailsPage(id: String?, navController: NavController, plantViewModel: PlantViewModel, modifier: Modifier = Modifier) {
    val plant = idToPlant(id)
    val uiState by plantViewModel.uiState.collectAsState()
    val isFavourite = uiState.favourites.contains(id)
    val isMyPlant = uiState.myPlants.any { it.plantId == id }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Fixed Image at the top
        Image(
            painter = painterResource(id = plant.image),
            contentDescription = "Fixed Image",
//            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        )

        // Scrollable content with overlapping card
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
//                    .padding(horizontal = 16.dp)
                    .offset(y = (-50).dp), // Negative offset to overlap the image
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                shape = RoundedCornerShape(50.dp)
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
                            onClick = {
                                if (!isMyPlant) {
                                    plantViewModel.addToMyPlants(plant.plantId)
                                }
                                else {
                                    plantViewModel.removeFromMyPlants(plant.plantId)
                                }
                            },
//                            colors = ButtonDefaults.buttonColors(
//                                containerColor = purpleColour,
//                                contentColor = Color.White
//                            ),
                            modifier = Modifier
                                .height(55.dp)
                                .width(400.dp)
                        ) {
                            Text(
                                text = if (isMyPlant) "Remove from My Plants" else "Add to My Plants",
                                fontSize = 25.sp)
                        }
                        Spacer(modifier = Modifier.width(40.dp))
                        // Favourites button
                        IconButton(
                            onClick = {
                                if (!isFavourite) {
                                    plantViewModel.addToFavourites(plant.plantId)
                                }
                                else {
                                    plantViewModel.removeFromFavourites(plant.plantId)
                                }
                            },
                            modifier = Modifier.size(55.dp).absolutePadding(0.dp, 0.dp, 0.dp, 5.dp),
//                            colors = ButtonDefaults.buttonColors(
//                                containerColor = Color.Red, // Background color of the button
//                                contentColor = Color.White  // Default icon color
//                            )
                        ) {
                            Icon(
                                imageVector = if (isFavourite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = "Heart Icon",
                                modifier = Modifier.size(40.dp),
                                tint = if (isFavourite) Color.Red else Color.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        text = "Description",
                        fontSize = 30.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = plant.description,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(25.dp))
                    Row() {
                        // Water
                        Card(
                            modifier = Modifier
                                .height(55.dp).width(245.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                            shape = RoundedCornerShape(20.dp)
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
                        // Sunlight
                        Card(
                            modifier = Modifier
                                .height(55.dp).width(245.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                            shape = RoundedCornerShape(20.dp)
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
                        // Price
                        Card(
                            modifier = Modifier
                                .height(55.dp).width(245.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                            shape = RoundedCornerShape(20.dp)
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
                        // Pet-Friendly
                        Card(
                            modifier = Modifier
                                .height(55.dp).width(245.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Row() {
                                Image(
                                    painter = painterResource(R.drawable.caticon),
                                    contentDescription = "Water icon"
                                )
                                var toxicText = "Non-Toxic"
                                if (plant.toxic) {
                                    toxicText = "Toxic"
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
                        // Indoor Outdoor
                        Card(
                            modifier = Modifier
                                .height(55.dp).width(245.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                            shape = RoundedCornerShape(20.dp)
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
                        // Size
                        Card(
                            modifier = Modifier
                                .height(55.dp).width(245.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                            shape = RoundedCornerShape(20.dp)
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
                    Text(
                        text = "Common Issues",
                        fontSize = 30.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = plant.commonIssues,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        text = "Helpful Tips",
                        fontSize = 30.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = plant.issueSolutions,
                        fontSize = 15.sp
                    )
                }
            }

            // Additional content to enable scrolling
//            Spacer(modifier = Modifier.height(16.dp))
//            for (i in 1..20) {
//                Text(
//                    text = "Scrollable content item $i",
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp)
//                )
//            }
        }
    }
}