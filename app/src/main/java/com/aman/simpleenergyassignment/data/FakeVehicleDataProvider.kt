package com.aman.simpleenergyassignment.data

import com.aman.simpleenergyassignment.domain.VehicleData
import com.aman.simpleenergyassignment.domain.VehicleScenario
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeVehicleDataProvider(
    private val intervalMillis: Long = 1000L,
    private val clock: () -> Long = { System.currentTimeMillis() },
) : VehicleDataProvider {

    init {
        require(intervalMillis > 0) {
            "Interval must be greater than zero"
        }
    }

    private val scenario = listOf(
        VehicleData(
            speedKmh = 0f,
            motorStatus = false,
            killSwitchStatus = false,
            timestamp = 0L,
            scenario = VehicleScenario.PARKED
        ),
        VehicleData(
            speedKmh = 0f,
            motorStatus = true,
            killSwitchStatus = false,
            timestamp = 0L,
            scenario = VehicleScenario.READY
        ),
        VehicleData(
            speedKmh = 25f,
            motorStatus = true,
            killSwitchStatus = false,
            timestamp = 0L,
            scenario = VehicleScenario.RIDING
        ),
        VehicleData(
            speedKmh = 50f,
            motorStatus = true,
            killSwitchStatus = false,
            timestamp = 0L,
            scenario = VehicleScenario.ACCELERATING
        ),
        VehicleData(
            speedKmh = 25f,
            motorStatus = false,
            killSwitchStatus = true,
            timestamp = 0L,
            scenario = VehicleScenario.MOTOR_DISABLED
        ),
        VehicleData(
            speedKmh = 15f,
            motorStatus = false,
            killSwitchStatus = true,
            timestamp = 0L,
            scenario = VehicleScenario.COASTING
        ),
        VehicleData(
            speedKmh = 5f,
            motorStatus = false,
            killSwitchStatus = true,
            timestamp = 0L,
            scenario = VehicleScenario.COASTING
        ),
        VehicleData(
            speedKmh = 0f,
            motorStatus = false,
            killSwitchStatus = true,
            timestamp = 0L,
            scenario = VehicleScenario.STOPPED
        )
    )

    override fun observeVehicleData(): Flow<VehicleData> = flow {

        while (true) {
            scenario.forEach { vehicleData ->

                emit(
                    vehicleData.copy(
                        timestamp = clock()
                    )
                )
                delay(intervalMillis)
            }
        }

    }

}