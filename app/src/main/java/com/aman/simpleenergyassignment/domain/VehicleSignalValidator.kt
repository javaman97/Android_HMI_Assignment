package com.aman.simpleenergyassignment.domain


enum class SignalStatus {
    VALID,
    INVALID,
    UNAVAILABLE
}

data class ValidatedSignal<T>(
    val value: T?,
    val status: SignalStatus
)

fun validateSpeedKmh(speed: Float?): ValidatedSignal<Float> {
    if (speed == null) {
        return ValidatedSignal(null, SignalStatus.UNAVAILABLE)
    }

    if (!speed.isFinite() || speed < 0f) {
        return ValidatedSignal(null, SignalStatus.INVALID)
    }

    return ValidatedSignal(speed, SignalStatus.VALID)
}

fun validateBooleanSignal(value: Boolean?): ValidatedSignal<Boolean> {
    return if (value == null) {
        ValidatedSignal(null, SignalStatus.UNAVAILABLE)
    } else {
        ValidatedSignal(value, SignalStatus.VALID)
    }
}
