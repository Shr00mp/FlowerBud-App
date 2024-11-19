package com.example.flowerbud.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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

@Composable
fun LoginPage(
    navController: NavController,
    plantViewModel: PlantViewModel,
    modifier: Modifier = Modifier
) {
    val lightGreen = colorResource(id = R.color.lightGreen)
    val darkBlue = colorResource(id = R.color.darkBlue)

    // TabRow to display the tabs
    Column(modifier = Modifier.fillMaxWidth()) {

        Spacer(modifier = Modifier.height(50.dp))
        Image(
            painter = painterResource(id = R.drawable.applogo),
            contentDescription = "No tasks",
            modifier = Modifier
                .height(150.dp)
                .aspectRatio(1f)
                .clip(CircleShape)
                .align(alignment = Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(35.dp))
        Text("Welcome to FlowerBud",
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
            fontSize = 35.sp)

        // State to keep track of the selected tab index
        var selectedTab by remember { mutableStateOf(0) }

        // List of tab titles
        val tabs = listOf("Log in", "Sign up")

        TabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier
                .padding(70.dp, 30.dp)
                .align(alignment = Alignment.CenterHorizontally),
            contentColor = darkBlue,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = darkBlue, // Change the underline color
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            title,
                            fontSize = 20.sp,
                            modifier = Modifier.absolutePadding(0.dp, 0.dp, 0.dp, 5.dp)
                        )
                    }
                )
            }
        }

        // Content for each tab, specified using the Tab Index
        when (selectedTab) {
            0 -> LoginContent(navController = navController, plantViewModel = plantViewModel)
            1 -> SignupContent(navController = navController, plantViewModel = plantViewModel)
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(navController: NavController, plantViewModel: PlantViewModel) {
    val lightGreen = colorResource(id = R.color.lightGreen)
    val darkGreen = colorResource(id = R.color.darkGreen)
    val darkBlue = colorResource(id = R.color.darkBlue)

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = username,
            onValueChange = {username = it},
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = "person")
            },
            label = {Text(text = "Username")},
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .width(400.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = darkBlue,
//                unfocusedBorderColor = darkGreen,
                focusedLabelColor = darkBlue,
//                unfocusedLabelColor = darkGreen,
                focusedLeadingIconColor = darkBlue,
//                unfocusedLeadingIconColor = darkGreen
            ),
        )
        Spacer(modifier = Modifier.height(15.dp))
        OutlinedTextField(
            value = password,
            onValueChange = {password = it},
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = "lock")
            },
            label = {Text(text = "Password")},
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .width(400.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = darkBlue,
//                unfocusedBorderColor = darkGreen,
                focusedLabelColor = darkBlue,
//                unfocusedLabelColor = darkGreen,
                focusedLeadingIconColor = darkBlue,
//                unfocusedLeadingIconColor = darkGreen
            ),
        )
        var errorMessage by remember { mutableStateOf("") }
        ErrorMessage(errorMessage = errorMessage)
        Spacer(modifier = Modifier.height(50.dp))
        Button(
            onClick = {
                errorMessage = getErrorMessage(
                    type = "Log in",
                    username = username,
                    password = password,
                    confirmPassword = "")
                if (errorMessage == "") {
                    navController.navigate(route = PlantScreens.Home.title)
                    plantViewModel.addUser(username)
                }
                // Check for unentered fields, if username password match, if there is user with this username
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = darkBlue,
                contentColor = darkBlue
            ),
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .height(50.dp)
                .width(200.dp)
        ) {
            Text("Log in", fontSize = 25.sp, color = Color.White)
        }
    }

}

fun getErrorMessage(type: String, username: String, password: String, confirmPassword: String): String {
    var errorMessage = ""
    if (username == "") {
        errorMessage = "Please enter a username."
    }
    else if (password == "") {
        errorMessage = "Please enter a password"
    }
    else if (type == "Sign up") {
        if (password != confirmPassword) {
            errorMessage = "Passwords do not match"
        }
    }
    return errorMessage
    // For login: Check if there is existing user with username and if username and passwords match
}

@Composable
fun ErrorMessage(errorMessage: String) {
    val red = colorResource(id = R.color.red)
    if (errorMessage != "") {
        Spacer(modifier = Modifier.height(50.dp))
        Column (modifier = Modifier.fillMaxWidth()) {
            Card (
                modifier = Modifier
                    .width(400.dp)
                    .align(alignment = Alignment.CenterHorizontally),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                colors = CardDefaults.cardColors(
                    containerColor = red
                )
            ) {
                Row(modifier = Modifier.padding(20.dp, 20.dp)) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "lock",
                        tint = Color.White,
                        modifier = Modifier.size(25.dp))
                    Text(
                        text = errorMessage,
                        color = Color.White,
                        fontSize = 20.sp,
                        modifier = Modifier.absolutePadding(13.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupContent(navController: NavController, plantViewModel: PlantViewModel) {
    val lightGreen = colorResource(id = R.color.lightGreen)
    val darkGreen = colorResource(id = R.color.darkGreen)
    val darkBlue = colorResource(id = R.color.darkBlue)

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = username,
            onValueChange = {username = it},
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = "person")
            },
            label = {Text(text = "Username")},
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .width(400.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = darkBlue,
//                unfocusedBorderColor = darkGreen,
                focusedLabelColor = darkBlue,
//                unfocusedLabelColor = darkGreen,
                focusedLeadingIconColor = darkBlue,
//                unfocusedLeadingIconColor = darkGreen
            ),
        )
        Spacer(modifier = Modifier.height(15.dp))
        OutlinedTextField(
            value = password,
            onValueChange = {password = it},
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = "lock")
            },
            label = {Text(text = "Password")},
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .width(400.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = darkBlue,
//                unfocusedBorderColor = darkGreen,
                focusedLabelColor = darkBlue,
//                unfocusedLabelColor = darkGreen,
                focusedLeadingIconColor = darkBlue,
//                unfocusedLeadingIconColor = darkGreen
            ),
        )
        Spacer(modifier = Modifier.height(15.dp))
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {confirmPassword = it},
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = "lock")
            },
            label = {Text(text = "Confirm password")},
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .width(400.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = darkBlue,
//                unfocusedBorderColor = darkGreen,
                focusedLabelColor = darkBlue,
//                unfocusedLabelColor = darkGreen,
                focusedLeadingIconColor = darkBlue,
//                unfocusedLeadingIconColor = darkGreen
            ),
        )
        var errorMessage by remember { mutableStateOf("") }
        ErrorMessage(errorMessage = errorMessage)
        Spacer(modifier = Modifier.height(50.dp))
        Button(
            onClick = {
                errorMessage = getErrorMessage(
                    type = "Sign up",
                    username = username,
                    password = password,
                    confirmPassword = confirmPassword)
                if (errorMessage == "") {
                    navController.navigate(route = PlantScreens.Home.title)
                    plantViewModel.addUser(username)
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = darkBlue,
                contentColor = darkBlue
            ),
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .height(50.dp)
                .width(200.dp)
        ) {
            Text("Sign up", fontSize = 25.sp, color = Color.White)
        }
    }
}