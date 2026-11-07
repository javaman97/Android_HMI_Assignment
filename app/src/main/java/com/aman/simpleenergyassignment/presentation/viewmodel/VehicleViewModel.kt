package com.aman.simpleenergyassignment.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aman.simpleenergyassignment.data.VehicleDataProvider
import com.aman.simpleenergyassignment.domain.SignalStatus
import com.aman.simpleenergyassignment.domain.VehicleData
import com.aman.simpleenergyassignment.domain.validateBooleanSignal
import com.aman.simpleenergyassignment.domain.validateSpeedKmh
import com.aman.simpleenergyassignment.presentation.VehicleUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class VehicleViewModel(
    private val dataProvider: VehicleDataProvider,
    private val nowMillis: () -> Long = System::currentTimeMillis,
    private val staleTimeoutMillis: Long = 3000L
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        VehicleUiState()
    )

    val uiState: StateFlow<VehicleUiState> =
        _uiState.asStateFlow()

    private var lastReceivedAtMillis: Long? = null

    // 3. Initialize ViewModel after properties
    init {
        require(staleTimeoutMillis > 0) {
            "Stale timeout must be greater than zero"
        }

        observeVehicleData()
        monitorStaleData()
    }

    // 4. Observe vehicle telemetry
    private fun observeVehicleData() {

        viewModelScope.launch {

            dataProvider.observeVehicleData()
                .onEach { vehicleData ->
                    processVehicleData(vehicleData)
                }
                .catch { exception ->

                    // Never swallow coroutine cancellation
                    if (exception is CancellationException) {
                        throw exception
                    }

                    lastReceivedAtMillis = null

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isDataStale = false,
                        errorMessage = exception.message
                            ?: "Unable to receive vehicle data"
                    )
                }
                .collect()
        }
    }

    // 5. Validate incoming vehicle data

    private fun processVehicleData(data: VehicleData) {
        val speed = validateSpeedKmh(data.speedKmh)
        val motor = validateBooleanSignal(data.motorStatus)
        val killSwitch = validateBooleanSignal(data.killSwitchStatus)

        val issues = buildList {
            if (speed.status == SignalStatus.INVALID) {
                add("Invalid speed signal")
            }
            if (speed.status == SignalStatus.UNAVAILABLE) {
                add("Speed unavailable")
            }
            if (motor.status == SignalStatus.UNAVAILABLE) {
                add("Motor signal unavailable")
            }
            if (killSwitch.status == SignalStatus.UNAVAILABLE) {
                add("Kill-switch signal unavailable")
            }
        }

        lastReceivedAtMillis = nowMillis()

        _uiState.value = VehicleUiState(
            speedKmh = speed.value,
            motorOn = motor.value,
            killSwitchActive = killSwitch.value,
            scenario = data.scenario,
            isLoading = false,
            isDataStale = false,
            errorMessage = issues.takeIf { it.isNotEmpty() }?.joinToString(" • "),
            speedStatus = speed.status,
            motorStatus = motor.status,
            killSwitchStatus = killSwitch.status
        )
    }


    // Monitor stale vehicle data
    private fun monitorStaleData() {

        viewModelScope.launch {

            while (true) {

                delay(500L)

                val lastReceived = lastReceivedAtMillis
                    ?: continue

                val elapsed = nowMillis() - lastReceived

                if (elapsed >= staleTimeoutMillis) {

                    _uiState.value = _uiState.value.copy(
                        isDataStale = true,
                        errorMessage = "Vehicle data is stale"
                    )
                }
            }
        }
    }
}