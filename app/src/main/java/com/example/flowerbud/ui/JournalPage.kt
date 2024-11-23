package com.example.flowerbud.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.flowerbud.R


// Function for journal page, including all user's journal images and a button to take new pictures
@Composable
fun JournalPage(navController: NavController, plantViewModel: PlantViewModel, modifier: Modifier = Modifier) {
    val uiState by plantViewModel.uiState.collectAsState()      // get uiState from viewModel

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // display a text if user has no journal images
        if (uiState.imgs.isEmpty()) {
            Text(
                "No Journal Image available.",
                fontSize = 20.sp,
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(0.dp, 15.dp))
        }

        // for each journal image, display an ImgCard
        uiState.imgs.map{img -> ImgCard(img, modifier = modifier)}

        // button to navigate to the page where user can take a journal picture
        Button(
            onClick = { navController.navigate(PlantScreens.CameraScreen.title)}) {
            Text("+")
        }
    }
}

// Function to display user's journal image card
@Composable
fun ImgCard(
    img: JournalImg,
    modifier: Modifier = Modifier
) {
    val lightGreen = colorResource(id = R.color.lightGreen)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(20.dp, 10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = lightGreen
        )
    ) {
        Row(modifier = Modifier.align(Alignment.Start)) {
            img.bitmap?.let {                   // display the image if available
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .size(200.dp)
                )
            } ?: run {              // if image is not available, display the text "No image available"
                Text(
                    text = "No image available",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            Column() {
                Text(                   // display image details the user has entered when submitting the picture
                    text = img.imageDetails,
                    fontSize = 30.sp,
                    modifier = Modifier.absolutePadding(20.dp, 20.dp)
                )
                Text(                   // display the date when the image has been taken
                    text = "Taken on ${convertDateFormat(img.selectedDate, "dd/MM/yyyy", "dd MMM, yyyy")}",
                    fontSize = 30.sp,
                    modifier = Modifier.absolutePadding(20.dp, 20.dp)
                )
            }
        }
    }
}


