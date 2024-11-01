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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
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
    toxic_yn: Boolean,
    outdoor: Boolean
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
    val customFont = FontFamily(
        Font(R.font.plus_font)
    )
    // Variables to store user choices
    var price_start by remember { mutableStateOf(0) }
    var price_end by remember { mutableStateOf(50) }
    var water_start by remember { mutableStateOf(1) }
    var water_end by remember { mutableStateOf(4) }
    var space_start by remember { mutableStateOf(1) }
    var space_end by remember { mutableStateOf(5) }
    var light_start by remember { mutableStateOf(1) }
    var light_end by remember { mutableStateOf(3) }
    var toxic_yn by remember { mutableStateOf(false) }
    var outdoor by remember { mutableStateOf(false) }

    ConstraintLayout {
        // createRef() used in ConstraintLayout{} to create a reference point, used for anchoring floating Get Results button
        val submitBtnRef = createRef()

        val greenColour = Color(0xFF70805d)
        val purpleColour = Color(0xFF96a7b6)
        val grayColour = Color(0xFFcbc8c4)

        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .background(color = Color.White)
        ) {
            Row(modifier = modifier.align(alignment = Alignment.CenterHorizontally)) {
                Text(
                    text = "Plant Quiz",
                    fontSize = 60.sp,
                    fontFamily = customFont,
                    modifier = Modifier.absolutePadding(0.dp, 50.dp, 0.dp, 15.dp),
                    style = MaterialTheme.typography.headlineSmall,
                )
            }

            Text(
                text = "Answer some questions about your plant preferences, and we'll find the best plant " +
                        "for you!",
                fontSize = 20.sp,
                modifier = Modifier.absolutePadding(30.dp, 25.dp, 30.dp, 40.dp)
            )

            // Price range section:
            Text(
                text = "What is your price range?",
                modifier = Modifier.absolutePadding(30.dp, 20.dp, 0.dp, 0.dp),
                fontSize = 25.sp,
                style = MaterialTheme.typography.headlineSmall
            )

            var sliderPosition1 by remember { mutableStateOf(0f..50f) }
            Column(
                modifier = Modifier.absolutePadding(30.dp, 0.dp, 30.dp, 0.dp)
            ) {
                // Price range slider
                RangeSlider(
                    value = sliderPosition1,
                    steps = 4,
                    onValueChange = { range ->
                        run {
                            sliderPosition1 = range;
                            price_start = range.start.toInt() // price_start updates to user's min choice
                            price_end = range.endInclusive.toInt() // price_end updates to user's max choice
                        }
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = greenColour,
                        activeTrackColor = greenColour,
                    ),
                    valueRange = 0f..50f,
                    onValueChangeFinished = {},
                )
                Text(text = "£" + price_start.toString() + " to " + "£" + price_end.toString())
            }

            // Water frequency section:
            Text(
                text = "How often would you like to water your plant?",
                modifier = Modifier.absolutePadding(30.dp, 50.dp, 0.dp, 0.dp),
                fontSize = 25.sp,
                style = MaterialTheme.typography.headlineSmall
            )

            var sliderPosition2 by remember { mutableStateOf(1f..4f) }
            Column(
                modifier = Modifier.absolutePadding(30.dp, 0.dp, 30.dp, 0.dp)
            ) {
                // Water frequency slider
                RangeSlider(
                    value = sliderPosition2,
                    steps = 2,
                    onValueChange = { range ->
                        run {
                            sliderPosition2 = range;
                            water_start = range.start.toInt() // water_start updates to user's min choice
                            water_end = range.endInclusive.toInt() // water_end updates to user's max choice
                        }
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = greenColour,
                        activeTrackColor = greenColour,
                    ),
                    valueRange = 1f..4f,
                    onValueChangeFinished = {},
                )
                Text(text = "Every " + water_start.toString() + " to " + water_end.toString() + " weeks")
            }

            // Space availability section:
            Text(
                text = "On a scale from 1 to 5, how much space would you want your plant to take up?",
                modifier = Modifier.absolutePadding(30.dp, 50.dp, 0.dp, 0.dp),
                fontSize = 25.sp,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "(1 might be a small desk, 5 might be a large outdoor garden)",
                modifier = Modifier.absolutePadding(30.dp, 5.dp)
            )

            var sliderPosition3 by remember { mutableStateOf(1f..5f) }
            Column(
                modifier = Modifier.absolutePadding(30.dp, 0.dp, 30.dp, 0.dp)
            ) {
                // Space availability slider
                RangeSlider(
                    value = sliderPosition3,
                    steps = 3,
                    onValueChange = { range ->
                        run {
                            sliderPosition3 = range;
                            space_start = range.start.toInt() // space_start updates to user's min choice
                            space_end = range.endInclusive.toInt() // space_end updates to user's max choice
                        }
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = greenColour,
                        activeTrackColor = greenColour,
                    ),
                    valueRange = 1f..5f,
                    onValueChangeFinished = {},
                )
                Text(text = space_start.toString() + " to " + space_end.toString())
            }

            // Light availability section:
            Text(
                text = "On a scale from 1 to 3, how much light would your plant have access to?",
                modifier = Modifier.absolutePadding(30.dp, 50.dp, 0.dp, 0.dp),
                fontSize = 25.sp,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "(1 might be no light, 3 might be an outdoor location)",
                modifier = Modifier.absolutePadding(30.dp, 5.dp)
            )

            var sliderPosition4 by remember { mutableStateOf(1f..3f) }
            Column(
                modifier = Modifier.absolutePadding(30.dp, 0.dp, 30.dp, 0.dp)
            ) {
                // Light availability slider
                RangeSlider(
                    value = sliderPosition4,
                    steps = 1,
                    onValueChange = { range ->
                        run {
                            sliderPosition4 = range;
                            light_start = range.start.toInt() // light_start variable updates to user's min choice
                            light_end = range.endInclusive.toInt() // light_end variable updates to user's max choice
                        }
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = greenColour,
                        activeTrackColor = greenColour,
                    ),
                    valueRange = 1f..3f,
                    onValueChangeFinished = {},
                )
                Text(text = light_start.toString() + " to " + light_end.toString())
            }

            // Pets (determine if plant toxic or non-toxic) section:
            Text(
                text = "Do you have pets a plant could be toxic to?",
                modifier = Modifier.absolutePadding(30.dp, 50.dp, 0.dp, 0.dp),
                fontSize = 25.sp,
                style = MaterialTheme.typography.headlineSmall
            )

            val yesButtonColourT = remember { mutableStateOf(grayColour) }
            val yesButtonTextColourT = remember { mutableStateOf(Color.Gray) }
            val noButtonColourT = remember { mutableStateOf(grayColour) }
            val noButtonTextColourT = remember { mutableStateOf(Color.Gray) }
            var yesButtonClickedT by remember { mutableStateOf(false) }
            var noButtonClickedT by remember { mutableStateOf(false) }
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
                            toxic_yn = true
                            yesButtonColourT.value = greenColour
                            yesButtonTextColourT.value = Color.White
                            noButtonColourT.value = grayColour
                            noButtonTextColourT.value = Color.Gray
                            yesButtonClickedT = true
                            noButtonClickedT = false
                        },
                        modifier = Modifier
                            .absolutePadding(0.dp, 20.dp, 15.dp, 0.dp)
                            .width(200.dp),
                        border = BorderStroke(
                            2.dp, if (yesButtonClickedT) greenColour else Color.Gray
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = yesButtonColourT.value,
                            contentColor = yesButtonTextColourT.value
                        )
                    ) {
                        Text("Yes", fontSize = 25.sp)
                    }
                    // No (for if there are pets) button
                    Button(
                        onClick = {
                            toxic_yn = false
                            yesButtonColourT.value = grayColour
                            yesButtonTextColourT.value = Color.Gray
                            noButtonColourT.value = greenColour
                            noButtonTextColourT.value = Color.White
                            yesButtonClickedT = false
                            noButtonClickedT = true
                        },
                        modifier = Modifier
                            .absolutePadding(15.dp, 20.dp, 30.dp, 0.dp)
                            .width(200.dp),
                        border = BorderStroke(
                            2.dp, if (noButtonClickedT) greenColour else Color.Gray
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = noButtonColourT.value,
                            contentColor = noButtonTextColourT.value
                        )
                    ) {
                        Text("No", fontSize = 25.sp)
                    }
                }
            }

            // Indoor vs Outdoor section:
            Text(
                text = "Would you like an indoor or outdoor plant?",
                modifier = Modifier.absolutePadding(30.dp, 50.dp, 0.dp, 0.dp),
                fontSize = 25.sp,
                style = MaterialTheme.typography.headlineSmall
            )

            val indoorButtonColour = remember { mutableStateOf(grayColour) }
            val indoorButtonTextColour = remember { mutableStateOf(Color.Gray) }
            val outdoorButtonColour = remember { mutableStateOf(grayColour) }
            val outdoorButtonTextColour = remember { mutableStateOf(Color.Gray) }
            var indoorButtonClicked by remember { mutableStateOf(false) }
            var outdoorButtonClicked by remember { mutableStateOf(false) }
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
                            outdoor = false
                            indoorButtonColour.value = greenColour
                            indoorButtonTextColour.value = Color.White
                            outdoorButtonColour.value = grayColour
                            outdoorButtonTextColour.value = Color.Gray
                            indoorButtonClicked = true
                            outdoorButtonClicked = false
                        },
                        modifier = Modifier
                            .absolutePadding(0.dp, 20.dp, 15.dp, 0.dp)
                            .width(200.dp),
                        border = BorderStroke(
                            2.dp, if (indoorButtonClicked) greenColour else Color.Gray
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = indoorButtonColour.value,
                            contentColor = indoorButtonTextColour.value
                        )
                    ) {
                        Text("Indoor", fontSize = 25.sp)
                    }
                    // Outdoor button
                    Button(
                        onClick = {
                            outdoor = true
                            indoorButtonColour.value = grayColour
                            indoorButtonTextColour.value = Color.Gray
                            outdoorButtonColour.value = greenColour
                            outdoorButtonTextColour.value = Color.White
                            indoorButtonClicked = false
                            outdoorButtonClicked = true
                        },
                        modifier = Modifier
                            .absolutePadding(15.dp, 20.dp, 30.dp, 0.dp)
                            .width(200.dp),
                        border = BorderStroke(
                            2.dp, if (outdoorButtonClicked) greenColour else Color.Gray
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = outdoorButtonColour.value,
                            contentColor = outdoorButtonTextColour.value
                        )
                    ) {
                        Text("Outdoor", fontSize = 25.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(50.dp))
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
                    val top5Results = getPlants(price_start, price_end,
                    water_start, water_end,
                    space_start, space_end,
                    light_start, light_end,
                    toxic_yn, outdoor)
                    top5Plants.addAll(top5Results)
                    onContentChange("Results") // content = "Results" so display changes to ResultsContent()
                          },
                colors = ButtonDefaults.buttonColors(
                    containerColor = purpleColour,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .height(70.dp)
                    .width(300.dp)
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
                modifier = Modifier.padding(20.dp, 0.dp)
            ){
                Text("Your top 5 results:", fontSize = 40.sp)
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
//            colors = ButtonDefaults.buttonColors(
//                containerColor = purpleColour,
//                contentColor = Color.White
//            ),
                modifier = Modifier
                    .height(70.dp)
                    .width(300.dp)
            ) {
                Text("Retake Quiz", fontSize = 25.sp)
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}