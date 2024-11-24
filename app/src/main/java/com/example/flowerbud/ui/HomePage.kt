package com.example.flowerbud.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.flowerbud.R
import kotlinx.coroutines.NonDisposableHandle.parent
import kotlinx.coroutines.selects.select
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.stream.Collectors
import java.util.stream.Stream

@Composable
fun HomePage(
    navController: NavController,
    plantViewModel: PlantViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by plantViewModel.uiState.collectAsState()
    // // myPlants stores all <Plant>s in My Plants
    val myPlants: List<UserPlant> = uiState.myPlants

    val dataSource = CalendarDataSource() // Create calendar data source
    var selectedDate by remember { mutableStateOf(dataSource.today) } // Date selected by user on calendar
    val plantsToWater = mutableListOf<UserPlant>() // List of plants that need to watered
    onDayChange(selectedDate, myPlants, plantsToWater) // Populate list of plants that need to watered

    val calendarUiModel = dataSource.getData(lastSelectedDate = selectedDate)

    ConstraintLayout {
        val searchPageRef = createRef() // For the floating Add more plants button
        val darkBlue = colorResource(id = R.color.darkBlue)

        Column(modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            Spacer(modifier = Modifier.height(25.dp))
            Header(
                data = calendarUiModel,
            )
            Content(data = calendarUiModel,
                // When a different date is clicked:
                onDateClick = { newDate ->
                    selectedDate = newDate // Selected date updates
                    onDayChange(selectedDate, myPlants, plantsToWater) // List of plants to be watered is changed
                },
                // When the back arrow is clicked:
                onPreviousClick = {
                    selectedDate = selectedDate.minusWeeks(1) // Selected date goes back a week
                    onDayChange(selectedDate, myPlants, plantsToWater) // List of plants to be watered is changed
                },
                // When the front arrow is clicked:
                onNextClick = {
                    selectedDate = selectedDate.plusWeeks(1) // Selected date goes forwards a week
                    onDayChange(selectedDate, myPlants, plantsToWater) // List of plants to be watered is changed
                })
            if (plantsToWater.isNotEmpty()) {
                WaterTasks(
                    plantsToWater = plantsToWater,
                    plantViewModel = plantViewModel,
                    selectedDate = selectedDate,
                    navController = navController,
                )
            } else {
                NoWaterTasks()
            }
        }

        // Row to contain floating Add more plants button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxWidth()
                .constrainAs(searchPageRef) {
                    bottom.linkTo(parent.bottom, margin = 5.dp) // Ensures button is floating
                }
        ) {
            // Add more plants button
            Button(
                onClick = {
                    // Navigates to search page when clicked
                    navController.navigate(route = PlantScreens.Search.title)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = darkBlue,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .height(60.dp)
                    .width(350.dp)
            ) {
                Text("Add more plants", fontSize = 25.sp)
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

// This function shows all the watering tasks for the day (when there are tasks)
@Composable
fun WaterTasks(
    plantsToWater: MutableList<UserPlant>,
    plantViewModel: PlantViewModel,
    selectedDate: LocalDate,
    navController: NavController,
) {
    val lightGreen = colorResource(id = R.color.lightGreen)
    Column {
        Spacer(modifier = Modifier.height(35.dp))
        Row() {
            // Title text
            Text(
                text = "Watering",
                fontSize = 40.sp,
                modifier = Modifier.absolutePadding(35.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            // Card showing number of tasks, i.e. number of plants to water
            Card(
                modifier = Modifier
                    .width(80.dp)
                    .absolutePadding(5.dp, 13.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                colors = CardDefaults.cardColors(
                    containerColor = lightGreen
                )
            ) {
                Text(
                    // Number tasks, i.e. number of plants to water = Size of list containing all plants to water
                    text = if (plantsToWater.size > 1) {
                        plantsToWater.size.toString() + " tasks"
                    } else {
                        plantsToWater.size.toString() + " task"
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }

        // For each plant that needs watering, show the card for it
        for (plant in plantsToWater) {
            WaterCard(plant = plant, plantViewModel = plantViewModel, selectedDate = selectedDate, navController = navController)
        }
        Spacer(modifier = Modifier.height(80.dp))
    }
}

// This function shows when there is no watering tasks scheduled for that day
@Composable
fun NoWaterTasks() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center, // Center the content vertically
            modifier = Modifier.align(Alignment.Center)
        ) {
            Spacer(modifier = Modifier.height(230.dp))
            Image(
                painter = painterResource(id = R.drawable.watering),
                contentDescription = "No tasks",
                modifier = Modifier.height(150.dp)
            )
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "No watering tasks scheduled on this day.",
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

// This helper function re-populates the list of plants that need to be watered when user selects a different date
fun onDayChange(
    selectedDate: LocalDate,
    myPlants: List<UserPlant>,
    plantsToWater: MutableList<UserPlant>
) {
    plantsToWater.clear()
    var filteredPlants = emptyList<UserPlant>()
    if (selectedDate == LocalDate.now()) { // If date selected is today
        filteredPlants =
            myPlants.filter { plant ->
                plant.nextWaterDay.isBefore(selectedDate) || plant.nextWaterDay == selectedDate || plant.lastWateredDates.contains(
                    selectedDate // Show overdue plants, plants to be watered today and plants watered today
                )
            }
    } else if (selectedDate.isAfter(LocalDate.now())) { // If date selected is in future
        filteredPlants =
            myPlants.filter { plant -> plant.nextWaterDay == selectedDate } // Show plants to be watered on that day
    } else if (selectedDate.isBefore(LocalDate.now())) { // If date selected is in the past
        filteredPlants =
            myPlants.filter { plant -> plant.lastWateredDates.contains(selectedDate) } // Show plants watered on that day
    }
    for (plant in filteredPlants) {
        plantsToWater.add(plant)
    }
}

// This function checks if the watering for a specific plant is overdue (on the selected date)
// Note that overdue means the watering task is past it's due date
fun isOverdue(selectedDate: LocalDate, plant: UserPlant): Boolean {
    // If the selected date is not today, then there is no possibility of there being overdue plants
    if (selectedDate != LocalDate.now()) {
        return false
    }
    // If the next day for watering the plant is before today, then the plant is overdue
    if (plant.nextWaterDay.isBefore(selectedDate)) {
        return true
    }
    return false
}


@Composable
fun WaterCard(plant: UserPlant, plantViewModel: PlantViewModel, selectedDate: LocalDate, navController: NavController) {
    val lightGreen = colorResource(id = R.color.lightGreen)
    val middleGreen = colorResource(id = R.color.middleGreen)
    val darkGreen = colorResource(id = R.color.darkGreen)
    val lightGrey = colorResource(id = R.color.lightGrey)
    val red = colorResource(id = R.color.red)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .absolutePadding(40.dp, 20.dp, 40.dp, 10.dp)
            .clickable {
                // When clicked, navigate to PlantDetailsPage(id = plant.plantId)
                navController.navigate(route = "details/${plant.plantId}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = lightGreen
        )
    ) {
        Row() {

            // Image of plant
            Image(
                painter = painterResource(id = plant.plantImage),
                contentDescription = plant.plantName,
            )
            Spacer(modifier = Modifier.width(20.dp))
            Column(
                modifier = Modifier.width(250.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                // Plant name
                Text(
                    text = plant.plantName,
                    fontSize = 30.sp,
                    style = androidx.compose.ui.text.TextStyle(
                        lineHeight = 40.sp
                    )
                )
                Spacer(modifier = Modifier.height(20.dp))

                // Card specifying the due date for the plant
                Card(
                    modifier = Modifier.widthIn(max = 220.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isOverdue(selectedDate, plant)) {
                            red // Card is red if overdue
                        } else lightGreen // Otherwise it is light green
                    )
                ) {
                    Text(
                        text = if (!isOverdue(selectedDate, plant)) { // If plant is not overdue
                            if (!plant.lastWateredDates.contains(selectedDate)) { //If plant has not been watered
                                if (selectedDate.isAfter(LocalDate.now())) { // If watering task is in the future
                                    "Due on this day"
                                }
                                else "Due date: Today" // If watering task is today
                            }
                            else {
                                "Watering completed" // If watering task has been completed
                            }
                        } else { // If plant is overdue
                            // User is told how many days overdue it is
                            // Number of days overdue is calculated by calculating difference between today and watering day
                            if (ChronoUnit.DAYS.between(plant.nextWaterDay, LocalDate.now()) == 1L) {
                                "Due 1 day ago"
                            }
                            else {"Due " + ChronoUnit.DAYS.between(plant.nextWaterDay, LocalDate.now()) + " days ago"}
                        },
                        modifier = Modifier.padding(10.dp, 10.dp),
                        fontSize = 17.sp,
                        color = if (!isOverdue(selectedDate, plant)) Color.Black else Color.White
                    )
                }
            }

            // Tick button for user to click when they have watered the plant
            IconButton(
                onClick = { plantViewModel.waterPlant(plant.plantId) }, // When clicked, data points for plant change (see waterPlant function for more info)
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(
                        // If the selected date is not in the past and the plant has not been watered on that day, then button is grey (task not completed)
                        // Otherwise, if the selected date is in the past or has been watered on that day, then the button is dark green (task completed)
                        color = if (!selectedDate.isBefore(LocalDate.now()) && !plant.lastWateredDates.contains(
                                selectedDate
                            )
                        ) {
                            lightGrey
                        } else {
                            darkGreen
                        },
                    )
                    .align(Alignment.CenterVertically),
                // Button is only enabled if the selected date is today and the plant has not been yet watered
                enabled = if (selectedDate == LocalDate.now() && !plant.lastWateredDates.contains(
                        selectedDate
                    )
                ) true else false
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Check Icon",
                    // If the selected date is not in the past and the plant has not been watered on that day, then tick icon is dark green (task not completed)
                    // Otherwise, if the selected date is in the past or has been watered on that day, then the tick icon is light grey (task completed)
                    tint = if (!selectedDate.isBefore(LocalDate.now()) && !plant.lastWateredDates.contains(
                            selectedDate
                        )
                    ) {
                        darkGreen
                    } else {
                        lightGrey
                    },
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}


@Composable
fun Header(data: CalendarUiModel) {
    Row {
        Text(
            text = if (data.selectedDate.isToday) {
                "Today"
            } else {
                data.selectedDate.date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
            },
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
                .absolutePadding(65.dp, 0.dp, 0.dp, 10.dp),
            fontSize = 20.sp
        )
    }
}

@Composable
fun Content(
    data: CalendarUiModel,
    onDateClick: (LocalDate) -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit
) {
    val lightGreen = colorResource(id = R.color.lightGreen)
    val middleGreen = colorResource(id = R.color.middleGreen)
    val darkGreen = colorResource(id = R.color.darkGreen)
    val darkBlue = colorResource(id = R.color.darkBlue)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = onPreviousClick,
            modifier = Modifier
                .size(40.dp)
                .absolutePadding(0.dp, 15.dp, 10.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack, contentDescription = "Back",
//                tint = darkBlue
            )
        }
        data.visibleDates.forEach { date ->
            ContentItem(date = date, onDateClick = onDateClick)
        }
        IconButton(
            onClick = onNextClick,
            modifier = Modifier
                .size(40.dp)
                .absolutePadding(10.dp, 15.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowForward, contentDescription = "Next",
            )
        }
    }
}

@Composable
fun ContentItem(date: CalendarUiModel.Date, onDateClick: (LocalDate) -> Unit) {
    val lightGreen = colorResource(id = R.color.lightGreen)
    val middleGreen = colorResource(id = R.color.middleGreen)
    val darkGreen = colorResource(id = R.color.darkGreen)
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 4.dp)
            .width(60.dp)
            .clickable { onDateClick(date.date) },
        colors = CardDefaults.cardColors(
            containerColor = if (date.isSelected) middleGreen else lightGreen
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Text(
                text = date.day,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
            Text(
                text = date.date.dayOfMonth.toString(),
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
        }
    }
}

data class CalendarUiModel(
    val selectedDate: Date,
    val visibleDates: List<Date>
) {
    data class Date(
        val date: LocalDate,
        val isSelected: Boolean,
        val isToday: Boolean
    ) {
        val day: String = date.format(DateTimeFormatter.ofPattern("E"))
    }
}

class CalendarDataSource {

    val today: LocalDate get() = LocalDate.now()

    fun getData(startDate: LocalDate = today, lastSelectedDate: LocalDate): CalendarUiModel {
        val firstDayOfWeek = lastSelectedDate.with(java.time.DayOfWeek.MONDAY)
        val endDayOfWeek = firstDayOfWeek.plusDays(7)
        val visibleDates = getDatesBetween(firstDayOfWeek, endDayOfWeek)
        return toUiModel(visibleDates, lastSelectedDate)
    }

    private fun getDatesBetween(startDate: LocalDate, endDate: LocalDate): List<LocalDate> {
        val numOfDays = ChronoUnit.DAYS.between(startDate, endDate)
        return Stream.iterate(startDate) { it.plusDays(1) }
            .limit(numOfDays)
            .collect(Collectors.toList())
    }

    private fun toUiModel(dateList: List<LocalDate>, lastSelectedDate: LocalDate): CalendarUiModel {
        return CalendarUiModel(
            selectedDate = toItemUiModel(lastSelectedDate, true),
            visibleDates = dateList.map { toItemUiModel(it, it.isEqual(lastSelectedDate)) }
        )
    }

    private fun toItemUiModel(date: LocalDate, isSelectedDate: Boolean) = CalendarUiModel.Date(
        isSelected = isSelectedDate,
        isToday = date.isEqual(today),
        date = date
    )
}
