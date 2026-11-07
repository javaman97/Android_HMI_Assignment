package com.aman.simpleenergyassignment.data

import com.aman.simpleenergyassignment.domain.VehicleData
import kotlinx.coroutines.flow.Flow

interface VehicleDataProvider {

    fun observeVehicleData(): Flow<VehicleData>
}