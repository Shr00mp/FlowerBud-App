package com.example.flowerbud.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flowerbud.R
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
        Column() {
            for (plant in plantsToWater) {
                WaterCard(plant = plant)
            }
        }
    }
}

fun onDayChange(
    selectedDate: LocalDate,
    myPlants: List<UserPlant>,
    plantsToWater: MutableList<UserPlant>
) {
    plantsToWater.clear()
    val filteredPlants =
        myPlants.filter { plant -> plant.nextWaterDay.isBefore(selectedDate) || plant.nextWaterDay == selectedDate }
    for (plant in filteredPlants) {
        plantsToWater.add(plant)
    }
}

@Composable
fun WaterCard(plant: UserPlant) {
    val lightGreen = colorResource(id = R.color.lightGreen)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(20.dp, 20.dp),
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
        }
    }
}


@Composable
fun Header(data: CalendarUiModel) {
    Row {
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = if (data.selectedDate.isToday) {
                "Today"
            } else {
                data.selectedDate.date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
            },
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
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
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = onPreviousClick) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
        }
        data.visibleDates.forEach { date ->
            ContentItem(date = date, onDateClick = onDateClick)
        }
        IconButton(onClick = onNextClick) {
            Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "Next")
        }
    }
}

@Composable
fun ContentItem(date: CalendarUiModel.Date, onDateClick: (LocalDate) -> Unit) {
    val lightGreen = colorResource(id = R.color.lightGreen)
    val darkGreen = colorResource(id = R.color.darkGreen)
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 4.dp)
            .width(60.dp)
            .clickable { onDateClick(date.date) },
        colors = CardDefaults.cardColors(
            containerColor = if (date.isSelected) darkGreen else lightGreen
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
