package com.example.flowerbud.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.flowerbud.R

// Function to get the top 5 plants based on user's quiz selections
fun getPlants(
    priceStart: Int, priceEnd: Int,
    waterStart: Int, waterEnd: Int,
    spaceStart: Int, spaceEnd: Int,
    lightStart: Int, lightEnd: Int,
    toxic_yn: Boolean?,
    outdoor: Boolean?
): MutableList<Plant> {
    val allScores = MutableList(11) { 0 }
    // Calculates a score for each plant based on how many of the user's preference criteria it fits
    for (i in 0 .. 10) {
        val plant = allPlants[i]
        if (plant.price in priceStart..priceEnd) {
            allScores[i] += 1
        }
        if (plant.waterWeek in waterStart..waterEnd) {
            allScores[i] += 1
        }
        if (plant.space in spaceStart..spaceEnd) {
            allScores[i] += 1
        }
        if (plant.light in lightStart..lightEnd) {
            allScores[i] += 1
        }
        if (plant.toxic == toxic_yn) {
            allScores[i] += 1
        }
        if (plant.outdoor == outdoor) {
            allScores[i] += 1
        }
    }
    // Gets the top 5 plants by index
    val top5Indexes: List<Int> = allScores
        .withIndex()                     // Pair each score with its index
        .sortedByDescending { it.value } // Sort by score in descending order
        .take(5)                         // Take the top 5 elements
        .map { it.index }                // Extract the indexes
    val top5Plants = mutableListOf<Plant>()
    // Adds each of the top 5 plants to top5Plants<Plant> using the index
    for (index in top5Indexes) {
        top5Plants.add(allPlants[index])
    }
    return top5Plants
}

@Composable
fun QuizPage(
    navController: NavController,
    plantViewModel: PlantViewModel,
    modifier: Modifier = Modifier,
) {
    // var content used to indicate whether quiz or results page should be showing
    var content by remember{ mutableStateOf("Quiz") }
    var top5Plants = remember{ mutableStateListOf<Plant>() }
    if (content == "Quiz") {
        QuizContent(navController = navController, plantViewModel = plantViewModel, top5Plants, onContentChange = { content = it })
    }
    else {
        ResultsContent(navController = navController, plantViewModel = plantViewModel, top5Plants, onContentChange = { content = it })
    }
}

@Composable
fun QuizContent(
    navController: NavController,
    plantViewModel: PlantViewModel,
    top5Plants: MutableList<Plant>,
    onContentChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by plantViewModel.uiState.collectAsState()

    ConstraintLayout {
        // createRef() used in ConstraintLayout{} to create a reference point, used for anchoring floating Get Results button
        val submitBtnRef = createRef()

        val lightGreen = colorResource(id = R.color.lightGreen)
        val darkGreen = colorResource(id = R.color.darkGreen)
        val darkBlue = colorResource(id = R.color.darkBlue)
        val grayColour = colorResource(id = R.color.lightGrey)


        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .background(color = Color.White)
        ) {
            Row(modifier = modifier.align(alignment = Alignment.CenterHorizontally)) {
                Text(
                    text = "PLANT QUIZ",
                    fontSize = 40.sp,
                    modifier = Modifier.absolutePadding(0.dp, 40.dp, 0.dp, 15.dp)
                )
            }

            Text(
                text = "Answer some questions about your plant preferences, and we'll find the best plant " +
                        "for you!",
                fontSize = 20.sp,
                modifier = Modifier.absolutePadding(30.dp, 20.dp, 30.dp, 40.dp)
            )

            // Price range section:
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .padding(30.dp, 10.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                colors = CardDefaults.cardColors(
                    containerColor = lightGreen
                )
            ) {
                Column() {
                    Text(
                        text = "What is your price range?",
                        modifier = Modifier.absolutePadding(30.dp, 20.dp, 0.dp, 0.dp),
                        fontSize = 25.sp,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Column(
                        modifier = Modifier.absolutePadding(30.dp, 0.dp, 30.dp, 0.dp)
                    ) {
                        // Price range slider
                        RangeSlider(
                            value = uiState.quizChoices.priceStart.toFloat()..uiState.quizChoices.priceEnd.toFloat(),
                            steps = 4,
                            onValueChange = { range ->
                                run {
                                    plantViewModel.updatePriceStart(range.start.toInt()) // price_start updates to user's min choice
                                    plantViewModel.updatePriceEnd(range.endInclusive.toInt()) // price_end updates to user's max choice
                                }
                            },
                            colors = SliderDefaults.colors(
                                thumbColor = darkGreen,
                                activeTrackColor = darkGreen,
                            ),
                            valueRange = 0f..50f,
                            onValueChangeFinished = {},
                        )
                        Text(text = "£" + uiState.quizChoices.priceStart.toString() + " to " + "£" + uiState.quizChoices.priceStart.toString())
                    }
                }
            }



            // Water frequency section:
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(193.dp)
                    .padding(30.dp, 10.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                colors = CardDefaults.cardColors(
                    containerColor = lightGreen
                )
            ) {
                Column() {
                    Text(
                        text = "How often would you like to water your plant?",
                        modifier = Modifier.absolutePadding(30.dp, 20.dp, 30.dp, 0.dp),
                        fontSize = 25.sp,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Column(
                        modifier = Modifier.absolutePadding(30.dp, 0.dp, 30.dp, 0.dp)
                    ) {
                        // Water frequency slider
                        RangeSlider(
                            value = uiState.quizChoices.waterStart.toFloat()..uiState.quizChoices.waterEnd.toFloat(),
                            steps = 2,
                            onValueChange = { range ->
                                run {
                                    plantViewModel.updateWaterStart(range.start.toInt()) // water_start updates to user's min choice
                                    plantViewModel.updateWaterEnd(range.endInclusive.toInt()) // water_end updates to user's max choice
                                }
                            },
                            colors = SliderDefaults.colors(
                                thumbColor = darkGreen,
                                activeTrackColor = darkGreen,
                            ),
                            valueRange = 1f..4f,
                            onValueChangeFinished = {},
                        )
                        Text(text = "Every " + uiState.quizChoices.waterStart.toString() + " to " + uiState.quizChoices.waterEnd.toString() + " weeks")
                    }
                }
            }


            // Space availability section:
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(218.dp)
                    .padding(30.dp, 10.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                colors = CardDefaults.cardColors(
                    containerColor = lightGreen
                )
            ) {
                Column() {
                    Text(
                        text = "On a scale from 1 to 5, how much space would you want your plant to take up?",
                        modifier = Modifier.absolutePadding(30.dp, 20.dp, 0.dp, 0.dp),
                        fontSize = 25.sp,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "(1 might be a small desk, 5 might be a large outdoor garden)",
                        modifier = Modifier.absolutePadding(30.dp, 5.dp)
                    )

                    Column(
                        modifier = Modifier.absolutePadding(30.dp, 0.dp, 30.dp, 0.dp)
                    ) {
                        // Space availability slider
                        RangeSlider(
                            value = uiState.quizChoices.spaceStart.toFloat()..uiState.quizChoices.spaceEnd.toFloat(),
                            steps = 3,
                            onValueChange = { range ->
                                run {
                                    plantViewModel.updateSpaceStart(range.start.toInt()) // space_start updates to user's min choice
                                    plantViewModel.updateSpaceEnd(range.endInclusive.toInt()) // space_end updates to user's max choice
                                }
                            },
                            colors = SliderDefaults.colors(
                                thumbColor = darkGreen,
                                activeTrackColor = darkGreen,
                            ),
                            valueRange = 1f..5f,
                            onValueChangeFinished = {},
                        )
                        Text(text = uiState.quizChoices.spaceStart.toString() + " to " + uiState.quizChoices.spaceEnd.toString())
                    }
                }
            }


            // Light availability section:
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .padding(30.dp, 10.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                colors = CardDefaults.cardColors(
                    containerColor = lightGreen
                )
            ) {
                Column() {
                    Text(
                        text = "On a scale from 1 to 3, how much light would your plant have access to?",
                        modifier = Modifier.absolutePadding(30.dp, 20.dp, 30.dp, 0.dp),
                        fontSize = 25.sp,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "(1 might be no light, 3 might be an outdoor location)",
                        modifier = Modifier.absolutePadding(30.dp, 5.dp)
                    )

                    Column(
                        modifier = Modifier.absolutePadding(30.dp, 0.dp, 30.dp, 0.dp)
                    ) {
                        // Light availability slider
                        RangeSlider(
                            value = uiState.quizChoices.lightStart.toFloat()..uiState.quizChoices.lightEnd.toFloat(),
                            steps = 1,
                            onValueChange = { range ->
                                run {
                                    plantViewModel.updateLightStart(range.start.toInt()) // light_start variable updates to user's min choice
                                    plantViewModel.updateLightEnd(range.endInclusive.toInt()) // light_end variable updates to user's max choice
                                }
                            },
                            colors = SliderDefaults.colors(
                                thumbColor = darkGreen,
                                activeTrackColor = darkGreen,
                            ),
                            valueRange = 1f..3f,
                            onValueChangeFinished = {},
                        )
                        Text(text = uiState.quizChoices.lightStart.toString() + " to " + uiState.quizChoices.lightEnd.toString())
                    }
                }
            }


            // Pets (determine if plant toxic or non-toxic) section:
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(175.dp)
                    .padding(30.dp, 10.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                colors = CardDefaults.cardColors(
                    containerColor = lightGreen
                )
            ) {
                Column() {
                    Text(
                        text = "Do you have pets a plant could be toxic to?",
                        modifier = Modifier.absolutePadding(30.dp, 20.dp, 0.dp, 0.dp),
                        fontSize = 25.sp,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .absolutePadding(30.dp, 0.dp, 0.dp, 0.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .absolutePadding(0.dp, 5.dp)
                        ) {
                            // Yes (for if there are pets) button
                            Button(
                                onClick = {
                                    plantViewModel.updateToxicYn(true)
                                },
                                modifier = Modifier
                                    .absolutePadding(0.dp, 20.dp, 15.dp, 0.dp)
                                    .width(200.dp),
                                border = BorderStroke(
                                    2.dp, if (uiState.quizChoices.toxicYn == true) darkGreen else Color.Gray
                                ),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if(uiState.quizChoices.toxicYn == true) darkGreen else grayColour,
                                    contentColor = if(uiState.quizChoices.toxicYn == true) Color.White else Color.Gray
                                )
                            ) {
                                Text("Yes", fontSize = 25.sp)
                            }
                            // No (for if there are pets) button
                            Button(
                                onClick = {
                                    plantViewModel.updateToxicYn(false)
                                },
                                modifier = Modifier
                                    .absolutePadding(15.dp, 20.dp, 30.dp, 0.dp)
                                    .width(200.dp),
                                border = BorderStroke(
                                    2.dp, if (uiState.quizChoices.toxicYn == false) darkGreen else Color.Gray
                                ),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if(uiState.quizChoices.toxicYn == false) darkGreen else grayColour,
                                    contentColor = if(uiState.quizChoices.toxicYn == false) Color.White else Color.Gray
                                )
                            ) {
                                Text("No", fontSize = 25.sp)
                            }
                        }
                    }

                }
            }

            // Indoor vs Outdoor section:
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .padding(30.dp, 10.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                colors = CardDefaults.cardColors(
                    containerColor = lightGreen
                )
            ) {
                Column() {
                    Text(
                        text = "Would you like an indoor or outdoor plant?",
                        modifier = Modifier.absolutePadding(30.dp, 20.dp, 0.dp, 0.dp),
                        fontSize = 25.sp,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .absolutePadding(30.dp, 0.dp, 0.dp, 50.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .absolutePadding(0.dp, 5.dp)
                        ) {
                            // Indoor button
                            Button(
                                onClick = {
                                    plantViewModel.updateOutdoor(false)
                                },
                                modifier = Modifier
                                    .absolutePadding(0.dp, 20.dp, 15.dp, 0.dp)
                                    .width(200.dp),
                                border = BorderStroke(
                                    2.dp, if (uiState.quizChoices.outdoor == false) darkGreen else Color.Gray
                                ),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if(uiState.quizChoices.outdoor == false) darkGreen else grayColour,
                                    contentColor = if(uiState.quizChoices.outdoor == false) Color.White else Color.Gray
                                )
                            ) {
                                Text("Indoor", fontSize = 25.sp)
                            }
                            // Outdoor button
                            Button(
                                onClick = {
                                    plantViewModel.updateOutdoor(true)
                                },
                                modifier = Modifier
                                    .absolutePadding(15.dp, 20.dp, 30.dp, 0.dp)
                                    .width(200.dp),
                                border = BorderStroke(
                                    2.dp, if (uiState.quizChoices.outdoor == true) darkGreen else Color.Gray
                                ),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if(uiState.quizChoices.outdoor == true) darkGreen else grayColour,
                                    contentColor = if(uiState.quizChoices.outdoor == true) Color.White else Color.Gray
                                )
                            ) {
                                Text("Outdoor", fontSize = 25.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(95.dp))
        }

        // Row to contain floating Get Results button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxWidth()
                .constrainAs(submitBtnRef) {
                    bottom.linkTo(parent.bottom, margin = 5.dp)
                }
        ) {
            // Get Results button
            Button(
                onClick = {
                    top5Plants.clear()
                    val top5Results = getPlants(uiState.quizChoices.priceStart, uiState.quizChoices.priceEnd,
                        uiState.quizChoices.waterStart, uiState.quizChoices.waterEnd,
                        uiState.quizChoices.spaceStart, uiState.quizChoices.spaceEnd,
                        uiState.quizChoices.lightStart, uiState.quizChoices.lightEnd,
                        uiState.quizChoices.toxicYn, uiState.quizChoices.outdoor)
                    top5Plants.addAll(top5Results)
                    onContentChange("Results")// content = "Results" so display changes to ResultsContent()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = darkBlue,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .height(60.dp)
                    .width(350.dp)
            ) {
                Text("Get Results", fontSize = 25.sp)
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun ResultsContent(
    navController: NavController,
    plantViewModel: PlantViewModel,
    top5Plants: MutableList<Plant>,
    onContentChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lightGreen = colorResource(id = R.color.lightGreen)
    val darkGreen = colorResource(id = R.color.darkGreen)
    val darkBlue = colorResource(id = R.color.darkBlue)
    val grayColour = colorResource(id = R.color.lightGrey)
    ConstraintLayout {
        // createRef() used in ConstraintLayout{} to create a reference point, used for anchoring floating Get Results button
        val retakeBtnRef = createRef()
        Column (
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .background(color = Color.White)
                .padding(10.dp, 0.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                modifier = Modifier
                    .padding(20.dp, 0.dp)
                    .align(alignment = Alignment.CenterHorizontally)
            ){
                Text("YOUR TOP 5 RESULTS", fontSize = 40.sp)
            }
            Spacer(modifier = Modifier.height(30.dp))

            // Show plant cards for ever top 5 plant
            for (plant in top5Plants) {
                PlantCard(plant = plant, navController = navController)
            }
            Spacer(modifier = Modifier.height(95.dp))
        }

        // Row to contain floating Retake Quiz button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxWidth()
                .constrainAs(retakeBtnRef) {
                    bottom.linkTo(parent.bottom, margin = 5.dp)
                }
        ) {
            // Get Results button
            Button(
                onClick = {
                    top5Plants.clear() // Clear list of top 5 plants, ready for new user choices
                    onContentChange("Quiz") // content = "Quiz" so display changes to QuizContent()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = darkBlue,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .height(60.dp)
                    .width(350.dp)
            ) {
                Text("Retake Quiz", fontSize = 25.sp)
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}