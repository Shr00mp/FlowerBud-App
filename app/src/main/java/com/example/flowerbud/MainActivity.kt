package com.example.flowerbud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.flowerbud.ui.PlantApp
import com.example.flowerbud.ui.theme.FlowerBudTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlowerBudTheme {
                PlantApp()
            }
        }
    }
}