package com.aman.simpleenergyassignment.presentation

import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aman.simpleenergyassignment.domain.VehicleScenario
import com.aman.simpleenergyassignment.presentation.viewmodel.VehicleViewModel

@Composable
fun HomeScreen(
    viewModel: VehicleViewModel
) {

    val uiState by viewModel.uiState
        .collectAsStateWithLifecycle()

    val backgroundColor = Color(0xFF101114)
    val cardColor = Color(0xFF202329)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {

            // Dashboard header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "ELECTRIC VEHICLE",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = uiState.scenario?.name
                        ?.replace("_", " ")
                        ?: "INITIALIZING",
                    color = when (uiState.scenario) {
                        VehicleScenario.PARKED -> Color(0xFF9E9E9E)
                        VehicleScenario.READY -> Color(0xFF00D084)
                        VehicleScenario.RIDING -> Color(0xFF2196F3)
                        VehicleScenario.ACCELERATING -> Color(0xFFFF9800)
                        VehicleScenario.MOTOR_DISABLED -> Color(0xFFFF5252)
                        VehicleScenario.COASTING -> Color(0xFFFFC107)
                        VehicleScenario.STOPPED -> Color(0xFFB0BEC5)
                        null -> Color(0xFF00D084)
                    },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Main dashboard content
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Speedometer section
                Card(
                    modifier = Modifier
                        .weight(1.5f)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = cardColor
                    )
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Text(
                            text = "VEHICLE SPEED",
                            color = Color.LightGray,
                            fontSize = 16.sp
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        Text(
                            text = uiState.speedKmh
                                ?.toInt()
                                ?.toString() ?: "--",
                            color = Color.White,
                            fontSize = 88.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "km/h",
                            color = Color.Gray,
                            fontSize = 20.sp
                        )
                    }
                }

                // Vehicle status section
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    StatusCard(
                        title = "MOTOR STATUS",
                        value = when (uiState.motorOn) {
                            true -> "ON"
                            false -> "OFF"
                            null -> "UNKNOWN"
                        },
                        active = uiState.motorOn,
                        cardColor = cardColor,
                        modifier = Modifier.weight(1f)
                    )

                    StatusCard(
                        title = "KILL SWITCH",
                        value = when (uiState.killSwitchActive) {
                            true -> "ACTIVE"
                            false -> "INACTIVE"
                            null -> "UNKNOWN"
                        },
                        active = uiState.killSwitchActive,
                        cardColor = cardColor,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Data availability indicator
            if (uiState.isDataStale ||
                uiState.errorMessage != null
            ) {

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = uiState.errorMessage
                        ?: "Vehicle data unavailable",
                    color = Color(0xFFFF5252),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun StatusCard(
    title: String,
    value: String,
    active: Boolean?,
    cardColor: Color,
    modifier: Modifier = Modifier
) {

    val statusColor = when (active) {
        true -> Color(0xFF00D084)
        false -> Color(0xFFFF5252)
        null -> Color.Gray
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = title,
                color = Color.LightGray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = value,
                color = statusColor,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}