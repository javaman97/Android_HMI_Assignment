package com.aman.simpleenergyassignment.presentation.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aman.simpleenergyassignment.data.FakeVehicleDataProvider


class VehicleViewModelFactory :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(
                VehicleViewModel::class.java
            )
        ) {

            val provider = FakeVehicleDataProvider()

            @Suppress("UNCHECKED_CAST")
            return VehicleViewModel(provider) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}