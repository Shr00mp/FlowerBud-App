package com.example.flowerbud.ui

import android.Manifest
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

// Composable for dealing with camera permission
@Composable
fun CameraPermission(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    // Camera permission launcher
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                onPermissionGranted() // Call onPermissionGranted if user is allowed to use camera
            } else {
                onPermissionDenied() // Call onPermissionDenied if user is not allowed to use camera
            }
        }
    )

    // Launch permission request when composable is first launched
    LaunchedEffect(Unit) {
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }
}

// Section for taking a picture
@Composable
fun CameraPreview(
    imageBitmap: Bitmap?,
    onLaunchCamera: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = onLaunchCamera) { // button with 'Take Picture' text; on clicking it, onLaunchCamera is called
            Text("Take Picture")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display the picture taken as an Image composable
        imageBitmap?.let {
            Image(bitmap = it.asImageBitmap(), contentDescription = "Captured Image", modifier = Modifier.size(200.dp))
        }
    }
}

// Section for Image Details, which includes a label "Additional Comments" and TextField
@Composable
fun ImageDetailsSection(
    imageDetails: TextFieldValue,
    onImageDetailsChange: (TextFieldValue) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Additional Comments") // label "Additional Comments"
        BasicTextField(    // TextField where user can enter image details
            value = imageDetails,
            onValueChange = onImageDetailsChange,  // call onImageDetailsChange function whenever the text field value is changed
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(8.dp)
                .border(1.dp, MaterialTheme.colorScheme.primary)
        )
    }
}

// DatePicker popup where user selects the date for the image
@Composable
fun ImageDatePickerDialog(context: Context, onDateSelected: (String) -> Unit, show: Boolean, onHide: () -> Unit) {
    val calendar = Calendar.getInstance()
    if (show) {
        DatePickerDialog(
            context,
            /*
            * function that is called on date change
            * which formats the selected date and saves it
            */
            { _, year, month, dayOfMonth ->
                val formattedDate = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year)
                onDateSelected(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
        onHide()  // toggle 'show' flag after the popup disappears
    }
}

// Section where user can pick a date
@Composable
fun DateSelectionSection(
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    onShowDatePicker: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Button(onClick = onShowDatePicker) {  // button which triggers DatePicker popup
            Text(text = "Select Date")
        }
        Text(text = "${convertDateFormat(selectedDate,"dd/MM/yyyy", "dd MMM, yyyy")}", modifier = Modifier.padding(vertical = 8.dp)) // text showing the selected date
    }
}

// Main Composable for the Screen
@Composable
fun CameraScreen(navController: NavController, plantViewModel: PlantViewModel, modifier: Modifier = Modifier) {
    var selectedDate by remember { mutableStateOf(formatCurrentDate()) }      // state variable 'selectedDate' whose initial value is today's date
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }       // state variable 'imageBitmap' which stores the picture taken
    var imageDetails by remember { mutableStateOf(TextFieldValue("")) }      // state variable 'imageDetails' which stores user's comments about the image
    var hasPermission by remember { mutableStateOf(false) }        // state variable 'hasPermission' which stores where user has camera permission
    val context = LocalContext.current

    // Camera launcher for taking picture
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        imageBitmap = bitmap
    }

    // Camera permission logic
    CameraPermission(
        onPermissionGranted = { hasPermission = true },
        onPermissionDenied = {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    )

    // Handle Take Picture Button click
    val takePicture = {
        if (hasPermission) {
            cameraLauncher.launch(null)
        } else {
            Toast.makeText(context, "Please grant camera permission.", Toast.LENGTH_SHORT).show()
        }
    }
    var show by remember{ mutableStateOf(false) }
    // Function to show DatePicker Dialog
    val showDatePicker = {
        show = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        ImageDatePickerDialog(context, onDateSelected = { date ->
            selectedDate = date
        }, show, { show = false})

        DateSelectionSection( // Section for Picking a date
            selectedDate = selectedDate,
            onDateSelected = { selectedDate = it },
            onShowDatePicker = showDatePicker // Pass the function to show the date picker
        )

        // The "Additional comments" section
        ImageDetailsSection(imageDetails = imageDetails, onImageDetailsChange = { imageDetails = it })

        // Section for taking a picture
        CameraPreview(imageBitmap = imageBitmap, onLaunchCamera = takePicture)

        Spacer(modifier = Modifier.height(16.dp))

        // Submit Button
        Button(onClick = {
            /* Error message if
            - there is not image
            - or date is not selected
            - or "Additional comments" is empty
            */
            if (selectedDate.isEmpty() || imageDetails.text.isEmpty() || imageBitmap == null) {
                Toast.makeText(context, "Please enter all details!", Toast.LENGTH_SHORT).show()
                return@Button
            }

            // Success submission alert
            Toast.makeText(context, "Details Submitted!", Toast.LENGTH_SHORT).show()

            // Save data into viewModel & navigate to JournalPage
            imageBitmap?.let { bitmap ->
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                plantViewModel.addImg(JournalImg(selectedDate, bitmap, imageDetails.text))
                navController.navigate(PlantScreens.Journal.title)
            }
        }) {
            Text("Submit")
        }
    }
}

// Function to get current date with the format "dd/MM/yyyy"
fun formatCurrentDate(): String {
    val today = LocalDate.now();
    // Define the desired format (e.g., "dd/MM/yyyy")
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    // Convert today to a formatted String
    return today.format(formatter);
}