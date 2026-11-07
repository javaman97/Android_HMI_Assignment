package com.aman.simpleenergyassignment.domain

data class VehicleData(
    val speedKmh:Float?,
    val motorStatus: Boolean?,
    val killSwitchStatus: Boolean?,
    val timestamp: Long,
    val scenario: VehicleScenario
)

enum class VehicleScenario{
    PARKED,
    READY,
    RIDING,
    ACCELERATING,
    MOTOR_DISABLED,
    COASTING,
    STOPPED
}