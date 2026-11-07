package com.aman.simpleenergyassignment.presentation

import com.aman.simpleenergyassignment.domain.SignalStatus
import com.aman.simpleenergyassignment.domain.VehicleScenario

data class VehicleUiState(
    val speedKmh: Float? = null,
    val motorOn: Boolean? = null,
    val killSwitchActive: Boolean? = null,
    val scenario: VehicleScenario? = null,
    val isLoading: Boolean = true,
    val isDataStale: Boolean = false,
    val errorMessage: String? = null,

    val speedStatus: SignalStatus = SignalStatus.UNAVAILABLE,
    val motorStatus: SignalStatus = SignalStatus.UNAVAILABLE,
    val killSwitchStatus: SignalStatus = SignalStatus.UNAVAILABLE
)

