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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
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
import androidx.navigation.NavController
import com.example.flowerbud.R
import kotlinx.coroutines.selects.select
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
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

    val dataSource = CalendarDataSource()
    var selectedDate by remember { mutableStateOf(dataSource.today) }
    val plantsToWater = mutableListOf<UserPlant>()
    onDayChange(selectedDate, myPlants, plantsToWater)

    val calendarUiModel = dataSource.getData(lastSelectedDate = selectedDate)

    Column(modifier = modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(25.dp))
        Header(
            data = calendarUiModel,
        )
        Content(data = calendarUiModel,
            onDateClick = { newDate ->
                selectedDate = newDate
                onDayChange(selectedDate, myPlants, plantsToWater)
            },
            onPreviousClick = {
                selectedDate = selectedDate.minusWeeks(1)
                onDayChange(selectedDate, myPlants, plantsToWater)
            },
            onNextClick = {
                selectedDate = selectedDate.plusWeeks(1)
                onDayChange(selectedDate, myPlants, plantsToWater)
            })
        if (plantsToWater.isNotEmpty()) {
            WaterTasks(plantsToWater = plantsToWater, plantViewModel = plantViewModel, selectedDate = selectedDate)
        }
        else {
            NoWaterTasks()
        }
    }
}

@Composable
fun WaterTasks(plantsToWater: MutableList<UserPlant>, plantViewModel: PlantViewModel, selectedDate: LocalDate) {
    val lightGreen = colorResource(id = R.color.lightGreen)
    Column {
        Spacer(modifier = Modifier.height(35.dp))
        Row() {
            Text(
                text = "Watering",
                fontSize = 40.sp,
                modifier = Modifier.absolutePadding(35.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Card(
                modifier = Modifier
                    .width(80.dp)
                    .absolutePadding(5.dp, 13.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                colors = CardDefaults.cardColors(
                    containerColor = lightGreen )
            ) {
                Text(
                    text = if (plantsToWater.size > 1) {plantsToWater.size.toString() + " tasks"} else {plantsToWater.size.toString() + " task"},
                    modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
//        Spacer(modifier = Modifier.height(10.dp))
        for (plant in plantsToWater) {
            WaterCard(plant = plant, plantViewModel = plantViewModel, selectedDate = selectedDate)
        }
    }
}

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

fun onDayChange(
    selectedDate: LocalDate,
    myPlants: List<UserPlant>,
    plantsToWater: MutableList<UserPlant>
) {
    plantsToWater.clear()
    var filteredPlants = emptyList<UserPlant>()
    if (selectedDate == LocalDate.now()) {
        filteredPlants =
            myPlants.filter { plant -> plant.nextWaterDay.isBefore(selectedDate) || plant.nextWaterDay == selectedDate || plant.lastWateredDates.contains(selectedDate)}
    }
    else if (selectedDate.isAfter(LocalDate.now())) {
        filteredPlants =
            myPlants.filter { plant -> plant.nextWaterDay == selectedDate }
    }
    else if (selectedDate.isBefore(LocalDate.now())) {
        filteredPlants =
            myPlants.filter { plant -> plant.lastWateredDates.contains(selectedDate) }
    }
    for (plant in filteredPlants) {
        plantsToWater.add(plant)
    }
}

fun isOverdue(selectedDate: LocalDate, plant: UserPlant): Boolean {
    if (selectedDate != LocalDate.now()) {
        return false
    }
    if (plant.nextWaterDay.isBefore(selectedDate)) {
        return true
    }
    return false
}

@Composable
fun WaterCard(plant: UserPlant, plantViewModel: PlantViewModel, selectedDate: LocalDate) {
    val lightGreen = colorResource(id = R.color.lightGreen)
    val middleGreen = colorResource(id = R.color.middleGreen)
    val darkGreen = colorResource(id = R.color.darkGreen)
    val lightGrey = colorResource(id = R.color.lightGrey)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(40.dp, 20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = lightGreen
        )
    ) {
        Row() {
            Image(
                painter = painterResource(id = plant.plantImage),
                contentDescription = plant.plantName,
            )
            Spacer(modifier = Modifier.width(20.dp))
            Column (
                modifier = Modifier.width(250.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = plant.plantName,
                    fontSize = 30.sp
                )
                Spacer(modifier = Modifier.height(20.dp))
                Card(
                    modifier = Modifier.widthIn(max = 220.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isOverdue(selectedDate, plant)) {middleGreen} else lightGreen
                    )
                ) {
                    Text(
                        text = if (!isOverdue(selectedDate, plant)) {
                            "Due date: Today"
                        } else {
                            "Due date: " + plant.nextWaterDay.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
                        },
                        modifier = Modifier.padding(10.dp, 10.dp),
                        fontSize = 17.sp
                    )
                }
            }
            IconButton(
                onClick = { plantViewModel.waterPlant(plant.plantId) },
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(
                        color = if (!selectedDate.isBefore(LocalDate.now()) && !plant.lastWateredDates.contains(selectedDate)) {
                            lightGrey
                        } else {
                            darkGreen
                        },
                    )
                    .align(Alignment.CenterVertically),
                enabled = if (selectedDate == LocalDate.now() && !plant.lastWateredDates.contains(selectedDate)) true else false
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Check Icon",
                    tint = if (!selectedDate.isBefore(LocalDate.now()) && !plant.lastWateredDates.contains(selectedDate)) {
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
                .absolutePadding(0.dp, 15.dp, 10.dp)) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back",
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
                .absolutePadding(10.dp, 15.dp)) {
            Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "Next",
//                tint = darkBlue
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
