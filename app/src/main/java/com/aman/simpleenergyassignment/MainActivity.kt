package com.aman.simpleenergyassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aman.simpleenergyassignment.presentation.HomeScreen
import com.aman.simpleenergyassignment.presentation.viewmodel.VehicleViewModel
import com.aman.simpleenergyassignment.presentation.viewmodel.VehicleViewModelFactory
import com.aman.simpleenergyassignment.ui.theme.SimpleEnergyAssignmentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val vehicleViewModel: VehicleViewModel = viewModel(
                factory = VehicleViewModelFactory()
            )

            SimpleEnergyAssignmentTheme {
                    HomeScreen(
                        viewModel = vehicleViewModel
                    )
            }
        }
    }
}