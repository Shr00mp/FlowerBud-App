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
    val darkBlue = colorResource(id = R.color.darkBlue)

    // TabRow to display the tabs
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(50.dp))
        // Image for the app logo
        Image(
            painter = painterResource(id = R.drawable.applogo),
            contentDescription = "App logo",
            modifier = Modifier
                .height(150.dp)
                .aspectRatio(1f)
                .clip(CircleShape)
                .align(alignment = Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(35.dp))
        // Welcome to FlowerBud text below app logo
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
            tabs.forEachIndexed { index, title -> // For each tab in the list:
                Tab(
                    selected = selectedTab == index, // Determines whether or not tab is selected
                    onClick = { selectedTab = index }, // When clicked, the tab is selected
                    text = { // Displays the tab title
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

// Displayed when the Login tab is selected
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(navController: NavController, plantViewModel: PlantViewModel) {
    val darkBlue = colorResource(id = R.color.darkBlue)

    var username by remember { mutableStateOf("") } // Stores entered username
    var password by remember { mutableStateOf("") } // Stores entered password
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(20.dp))

        // Username text field
        OutlinedTextField(
            value = username,
            onValueChange = {username = it}, // As user types, the username variable also updates
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = "person") // Icon at the front of the text field is a person
            },
            label = {Text(text = "Username")},
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .width(400.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = darkBlue,
                focusedLabelColor = darkBlue,
                focusedLeadingIconColor = darkBlue,
            ),
        )
        Spacer(modifier = Modifier.height(15.dp))

        // Password text field
        OutlinedTextField(
            value = password,
            onValueChange = {password = it}, // As user types, the password variable also updates
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = "lock") // Icon at the front of the text field is a lock
            },
            label = {Text(text = "Password")},
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .width(400.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = darkBlue,
                focusedLabelColor = darkBlue,
                focusedLeadingIconColor = darkBlue,
            ),
        )

        var errorMessage by remember { mutableStateOf("") } // At first, there is no error message
        ErrorMessage(errorMessage = errorMessage) // Show error message
        Spacer(modifier = Modifier.height(50.dp))

        // Login button
        Button(
            onClick = {
                errorMessage = getErrorMessage( // Get the right error message
                    type = "Log in",
                    username = username,
                    password = password,
                    confirmPassword = "")
                if (errorMessage == "") { // If there is no error
                    plantViewModel.addUser(username) // Update the viewmodel with the username to be displayed on the profile page
                    navController.navigate(route = PlantScreens.Home.title) // Navigate to the home page
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
            Text("Log in", fontSize = 25.sp, color = Color.White)
        }
    }

}

// Helper function to determine what the error message should be
fun getErrorMessage(type: String, username: String, password: String, confirmPassword: String): String {
    var errorMessage = ""
    if (username == "") { // If username has not been entered
        errorMessage = "Please enter a username."
    }
    else if (password == "") { // If passowrd has not been enetered
        errorMessage = "Please enter a password"
    }
    else if (type == "Sign up") { // If the sign up tab is selected and
        if (password != confirmPassword) { // the passwords do not match
            errorMessage = "Passwords do not match"
        }
    }
    return errorMessage
}

// Function for displaying the error message
@Composable
fun ErrorMessage(errorMessage: String) {
    val red = colorResource(id = R.color.red)
    if (errorMessage != "") { // If error message is empty, nothing shows
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

// Displayed when the Login tab is selected
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupContent(navController: NavController, plantViewModel: PlantViewModel) {

    val darkBlue = colorResource(id = R.color.darkBlue)

    var username by remember { mutableStateOf("") } // Stores entered username
    var password by remember { mutableStateOf("") } // Stores entered password
    var confirmPassword by remember { mutableStateOf("") } // Stores entered second password (for confirmation)

    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = username,
            onValueChange = {username = it}, // As user types, the username also updates
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = "person")
            },
            label = {Text(text = "Username")},
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .width(400.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = darkBlue,
                focusedLabelColor = darkBlue,
                focusedLeadingIconColor = darkBlue,
            ),
        )
        Spacer(modifier = Modifier.height(15.dp))
        OutlinedTextField(
            value = password,
            onValueChange = {password = it}, // As the user types, the password also updates
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = "lock")
            },
            label = {Text(text = "Password")},
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .width(400.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = darkBlue,
                focusedLabelColor = darkBlue,
                focusedLeadingIconColor = darkBlue,
            ),
        )
        Spacer(modifier = Modifier.height(15.dp))
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {confirmPassword = it}, // As the user types, the confirmation password also updates
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = "lock")
            },
            label = {Text(text = "Confirm password")},
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .width(400.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = darkBlue,
                focusedLabelColor = darkBlue,
                focusedLeadingIconColor = darkBlue,
            ),
        )

        var errorMessage by remember { mutableStateOf("") } // There is no error message at first
        ErrorMessage(errorMessage = errorMessage) // Display the error message
        Spacer(modifier = Modifier.height(50.dp))

        // Sign up button
        Button(
            onClick = {
                errorMessage = getErrorMessage( // Get the right error message
                    type = "Sign up",
                    username = username,
                    password = password,
                    confirmPassword = confirmPassword)
                if (errorMessage == "") { // If there is no error message
                    plantViewModel.addUser(username) // Update the viewmodel with the new username so that it can be displayed on the profile page
                    navController.navigate(route = PlantScreens.Home.title) // Navigate to the home page
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