package com.example.flowerbud.ui

import android.graphics.Typeface
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.flowerbud.R


@Composable
fun FilterPage(
    navController: NavController,
    plantViewModel: PlantViewModel,
    modifier: Modifier = Modifier,
){
    val customFont = FontFamily(
        Font(R.font.plus_font)
    )
    ConstraintLayout {
        val submitBtnRef = createRef();
        val greenColour = Color(0xFF70805d)
        val purpleColour = Color(0xFF96a7b6)
        val grayColour = Color(0xFFcbc8c4)

        Column(modifier = modifier
            .verticalScroll(rememberScrollState())
            .background(color = Color.White)) {
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

            Text(
                text = "What is your price range?",
                modifier = Modifier.absolutePadding(30.dp, 20.dp, 0.dp, 0.dp),
                fontSize = 25.sp,
                style = MaterialTheme.typography.headlineSmall
            )

            var sliderPosition1 by remember { mutableStateOf(0f..50f) }
            var price_start by remember { mutableStateOf(0) };
            var price_end by remember { mutableStateOf(50) };
            Column(
                modifier = Modifier.absolutePadding(30.dp, 0.dp, 30.dp, 0.dp)
            ) {
                RangeSlider(
                    value = sliderPosition1,
                    steps = 4,
                    onValueChange = { range ->
                        run {
                            sliderPosition1 = range;
                            price_start = range.start.toInt();
                            price_end = range.endInclusive.toInt();
                        }
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = greenColour,
                        activeTrackColor = greenColour,
                    ),
                    valueRange = 0f..50f,
                    onValueChangeFinished = {
                        // launch some business logic update with the state you hold
                        // viewModel.updateSelectedSliderValue(sliderPosition)
                    },
                )
                Text(text = "£" + price_start.toString() + " to " + "£" + price_end.toString())
            }

            Text(
                text = "How often would you like to water your plant?",
                modifier = Modifier.absolutePadding(30.dp, 50.dp, 0.dp, 0.dp),
                fontSize = 25.sp,
                style = MaterialTheme.typography.headlineSmall
            )

            var sliderPosition2 by remember { mutableStateOf(1f..4f) }
            var water_start by remember { mutableStateOf(1) };
            var water_end by remember { mutableStateOf(4) };
            Column(
                modifier = Modifier.absolutePadding(30.dp, 0.dp, 30.dp, 0.dp)
            ) {
                RangeSlider(
                    value = sliderPosition2,
                    steps = 2,
                    onValueChange = { range ->
                        run {
                            sliderPosition2 = range;
                            water_start = range.start.toInt();
                            water_end = range.endInclusive.toInt();
                        }
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = greenColour,
                        activeTrackColor = greenColour,
                    ),
                    valueRange = 1f..4f,
                    onValueChangeFinished = {
                        // launch some business logic update with the state you hold
                        // viewModel.updateSelectedSliderValue(sliderPosition)
                    },
                )
                Text(text = "Every " + water_start.toString() + " to " + water_end.toString() + " weeks")
            }

            Text(
                text = "On a scale from 1 to 5, how much space do you have for your plant?",
                modifier = Modifier.absolutePadding(30.dp, 50.dp, 0.dp, 0.dp),
                fontSize = 25.sp,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "(1 might be a small desk, 5 might be a large outdoor garden)",
                modifier = Modifier.absolutePadding(30.dp, 5.dp)
            )

            var sliderPosition3 by remember { mutableStateOf(1f..5f) }
            var space_start by remember { mutableStateOf(1) };
            var space_end by remember { mutableStateOf(5) };
            Column(
                modifier = Modifier.absolutePadding(30.dp, 0.dp, 30.dp, 0.dp)
            ) {
                RangeSlider(
                    value = sliderPosition3,
                    steps = 3,
                    onValueChange = { range ->
                        run {
                            sliderPosition3 = range;
                            space_start = range.start.toInt();
                            space_end = range.endInclusive.toInt();
                        }
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = greenColour,
                        activeTrackColor = greenColour,
                    ),
                    valueRange = 1f..5f,
                    onValueChangeFinished = {
                        // launch some business logic update with the state you hold
                        // viewModel.updateSelectedSliderValue(sliderPosition)
                    },
                )
                Text(text = space_start.toString() + " to " + space_end.toString())
            }

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
            var light_start by remember { mutableStateOf(1) };
            var light_end by remember { mutableStateOf(3) };
            Column(
                modifier = Modifier.absolutePadding(30.dp, 0.dp, 30.dp, 0.dp)
            ) {
                RangeSlider(
                    value = sliderPosition4,
                    steps = 1,
                    onValueChange = { range ->
                        run {
                            sliderPosition4 = range;
                            light_start = range.start.toInt();
                            light_end = range.endInclusive.toInt();
                        }
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = greenColour,
                        activeTrackColor = greenColour,
                    ),
                    valueRange = 1f..3f,
                    onValueChangeFinished = {
                        // launch some business logic update with the state you hold
                        // viewModel.updateSelectedSliderValue(sliderPosition)
                    },
                )
                Text(text = light_start.toString() + " to " + light_end.toString())
            }

            Text(
                text = "Do you have pets a plant could be toxic to?",
                modifier = Modifier.absolutePadding(30.dp, 50.dp, 0.dp, 0.dp),
                fontSize = 25.sp,
                style = MaterialTheme.typography.headlineSmall
            )

            var toxic_yn by remember { mutableStateOf(false) }
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

            Text(
                text = "Would you like an indoor or outdoor plant?",
                modifier = Modifier.absolutePadding(30.dp, 50.dp, 0.dp, 0.dp),
                fontSize = 25.sp,
                style = MaterialTheme.typography.headlineSmall
            )

            var indoorOutdoor by remember { mutableStateOf(false) }
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
                    Button(
                        onClick = {
                            indoorOutdoor = true
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
                    Button(
                        onClick = {
                            indoorOutdoor = false
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
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxWidth()
                .constrainAs(submitBtnRef) {
                bottom.linkTo(parent.bottom, margin = 5.dp)
            }
        ) {
            Button(
                onClick = { /* TODO */},
                colors = ButtonDefaults.buttonColors(
                    containerColor = purpleColour,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .height(50.dp)
                    .width(300.dp)
            ) {
                Text("Get Results", fontSize = 25.sp)
            }
        }
    }
}